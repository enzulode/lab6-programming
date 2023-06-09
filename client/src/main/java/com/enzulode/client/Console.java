package com.enzulode.client;

import com.enzulode.client.parser.cli.CLIParser;
import com.enzulode.client.util.Printer;
import com.enzulode.common.command.Command;
import com.enzulode.common.command.ElementTicketCommand;
import com.enzulode.common.command.impl.ExitCommand;
import com.enzulode.common.command.impl.UpdateCommand;
import com.enzulode.common.network.request.CommandRequest;
import com.enzulode.common.network.request.IdRequest;
import com.enzulode.common.network.response.CommandResponse;
import com.enzulode.common.network.response.IdResponse;
import com.enzulode.common.parser.TypesParser;
import com.enzulode.common.parser.exception.ParsingException;
import com.enzulode.common.resolution.ResolutionService;
import com.enzulode.models.Ticket;
import com.enzulode.network.UDPChannelClient;
import com.enzulode.network.exception.NetworkException;
import com.enzulode.network.exception.ServerNotAvailableException;
import com.enzulode.network.model.interconnection.util.ResponseCode;
import lombok.RequiredArgsConstructor;
import sun.misc.Signal;

import java.util.Scanner;

/**
 * Main application class
 *
 */
@RequiredArgsConstructor
public class Console
{
    /**
     * Scanner instance
     *
     */
    private final Scanner scanner;

    /**
     * Printer instance
     *
     */
    private final Printer printer;

    /**
     * ResolvingService instance
     *
     */
    private final ResolutionService resolutionService;

	/**
	 * Ticket CLI parser
	 *
	 */
	private final CLIParser<Ticket> cliParser;

    /**
     * Application initialisation method
     *
     */
    public void run()
    {
//		Registered signal handler
	    Signal.handle(new Signal("INT"), (signal) -> {
			printer.println("Caught an interruption signal: CTRL + C was pressed");
			printer.println("Client application shutdown initiated");
			System.exit(0);
	    });

//		Starting client application server
		try(UDPChannelClient client = new UDPChannelClient())
		{
//			User loop
			while (true)
			{
	            printer.print("Enter command: ");

//				Handle 'End of input stream' and turn the application off (CTRL + D)
	            if (!scanner.hasNextLine())
				{
	                printer.println("Input stream ended");
					printer.println("Application shutdown initiated");
	                System.exit(0);
	            }

	            String line = scanner.nextLine();

	            try
	            {
//					Resolve command
	                Command<Ticket> command = resolutionService.resolveCommand(line);

					if (command instanceof ExitCommand)
					{
						printer.println("Exit command detected: terminating current client");
						System.exit(0);
					}

					if (command instanceof UpdateCommand cmd)
					{
						Integer id = TypesParser.parseInteger(cmd.getArgs().get(0));
						if (id == null)
							throw new ParsingException("Updatable id should be provided");

						IdRequest request = new IdRequest(id);
						IdResponse response = client.sendRequestAndWaitResponse(request);

						if (response.getCode() != ResponseCode.SUCCEED)
						{
							printer.println(response.getMessage());
							continue;
						}
					}

		            if (command instanceof ElementTicketCommand cmd)
						cmd.setElement(cliParser.parse());

					CommandRequest request = new CommandRequest(command);
					CommandResponse response = client.sendRequestAndWaitResponse(request);

					printer.println(response.getResult().getMessage());
	            }
				catch (ServerNotAvailableException e)
				{
					printer.println("Failed: " + e.getMessage());
				}
				catch (Exception e)
				{
	                printer.println(e.getMessage());
	            }
	        }
		}
		catch (NetworkException e)
		{
			printer.println(e.getMessage());
		}
    }
}
