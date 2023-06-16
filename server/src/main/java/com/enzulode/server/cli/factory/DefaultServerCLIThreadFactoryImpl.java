package com.enzulode.server.cli.factory;

import com.enzulode.common.execution.ExecutionService;
import com.enzulode.common.resolution.ResolutionService;
import com.enzulode.models.Ticket;
import com.enzulode.server.cli.ServerUIThread;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;

/**
 * This is a default implementation of a server cli thread factory
 *
 */
@RequiredArgsConstructor
public class DefaultServerCLIThreadFactoryImpl implements ServerCLIThreadFactory
{
	/**
	 * Buffered cli reader instance
	 *
	 */
	private final BufferedReader cliReader;

	/**
	 * Resolution service instance
	 *
	 */
	private final ResolutionService resolutionService;

	/**
	 * Execution service instance
	 *
	 */
	private final ExecutionService<Ticket> executionService;

	/**
	 * This method initiates new server cli thread
	 *
	 * @return new server cli thread instance
	 */
	@Override
	public Thread newServerCLIThread()
	{
		return new ServerUIThread(cliReader, resolutionService, executionService);
	}
}
