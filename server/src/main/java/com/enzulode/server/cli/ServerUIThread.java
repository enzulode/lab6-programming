package com.enzulode.server.cli;

import com.enzulode.common.command.Command;
import com.enzulode.common.command.impl.ExitCommand;
import com.enzulode.common.execution.ExecutionService;
import com.enzulode.common.resolution.ResolutionService;
import com.enzulode.common.resolution.exception.CommandResolutionException;
import com.enzulode.models.Ticket;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Server-side ui thread
 *
 */
@Slf4j
public class ServerUIThread extends Thread
{
	/**
	 * Console reader instance
	 *
	 */
	private final BufferedReader cliReader;

	/**
	 * Command resolution service
	 *
	 */
	private final ResolutionService resolutionService;

	/**
	 * Command execution service
	 *
	 */
	private final ExecutionService<Ticket> executionService;

	/**
	 * Server ui thread constructor
	 *
	 * @param cliReader console reader instance
	 * @param resolutionService resolution service instance
	 * @param executionService execution service instance
	 */
	public ServerUIThread(
			BufferedReader cliReader,
			ResolutionService resolutionService,
			ExecutionService<Ticket> executionService
	)
	{
		super("server-ui-thread");

		this.cliReader = cliReader;
		this.resolutionService = resolutionService;
		this.executionService = executionService;
	}

	/**
	 * The server ui thread body
	 *
	 */
	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				if (cliReader.ready())
				{
					Command<Ticket> command = resolutionService.resolveCommand(cliReader.readLine());

					if (!(command instanceof ExitCommand))
					{
						log.warn("This command is not supported on the server side");
						continue;
					}

					log.info("Got command from server console: " + command.getClass().getSimpleName());
					log.info(executionService.execute(command).getMessage());
				}
			}
			catch (CommandResolutionException e)
			{
				log.warn("Such command does not exist");
			}
			catch (IOException e)
			{
				log.error("Something went wrong with stdin on the server side", e);
			}
		}
	}
}
