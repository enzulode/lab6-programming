package com.enzulode.server.ui;

import com.enzulode.common.command.Command;
import com.enzulode.common.command.impl.ExitCommand;
import com.enzulode.common.command.impl.SaveCommand;
import com.enzulode.common.execution.ExecutionService;
import com.enzulode.common.resolution.ResolutionService;
import com.enzulode.common.resolution.exception.CommandResolutionException;
import com.enzulode.models.Ticket;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Server-side ui thread
 *
 */
public class ServerUIThread extends Thread
{
	/**
	 * Console reader instance
	 *
	 */
	private final BufferedReader cliReader;

	/**
	 * Logger instance
	 *
	 */
	private final Logger logger;

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
	 * @param logger logger instance
	 */
	public ServerUIThread(
			BufferedReader cliReader,
			ResolutionService resolutionService,
			ExecutionService<Ticket> executionService,
			Logger logger
	)
	{
		super("server-ui-thread");

		this.cliReader = cliReader;
		this.resolutionService = resolutionService;
		this.executionService = executionService;
		this.logger = logger;
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

						if (!(command instanceof SaveCommand || command instanceof ExitCommand))
						{
							logger.warn("This command is not supported on the server side");
							continue;
						}

						logger.info("Got command from server console: " + command.getClass().getSimpleName());
						logger.info(executionService.execute(command).getMessage());
					}
				}
				catch (CommandResolutionException e)
				{
					logger.warn("Such command does not exist");
				}
				catch (IOException e)
				{
					logger.error("Something went wrong with stdin on the server side", e);
				}
			}

	}
}
