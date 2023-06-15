package com.enzulode.server.configuration;

import com.enzulode.common.dao.TicketDao;
import com.enzulode.common.execution.ExecutionService;
import com.enzulode.common.filesystem.FileManipulationService;
import com.enzulode.common.resolution.CommandFactory;
import com.enzulode.common.resolution.ResolutionService;
import com.enzulode.common.resolution.ResolutionServiceImpl;
import com.enzulode.models.Ticket;
import com.enzulode.network.UDPSocketServer;
import com.enzulode.network.exception.NetworkException;
import com.enzulode.network.handling.RequestHandler;
import com.enzulode.server.cli.resolution.ServerCommandFactory;
import com.enzulode.server.execution.ExecutionServiceImpl;
import com.enzulode.server.handling.DefaultApplicationRequestHandlerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Application configuration
 *
 */
@Configuration
@Slf4j
public class ServerConfiguration
{
	/**
	 * File manipulation service bean declaration
	 *
	 * @return file manipulation service instance
	 */
	@Bean(name = "fileManipulationService")
	public FileManipulationService fileManipulationServiceBean()
	{
		return new FileManipulationService();
	}

	/**
	 * Command factory bean declaration
	 *
	 * @return command factory instance
	 */
	@Bean(name = "commandFactory")
	public CommandFactory<Ticket> commandFactoryBean()
	{
		return new ServerCommandFactory();
	}

	/**
	 * Command resolution service bean declaration
	 *
	 * @param commandFactory command factory instance
	 * @return command resolution service implementation
	 */
	@Bean(name = "commandResolutionService")
	public ResolutionService commandResolutionServiceBean(CommandFactory<Ticket> commandFactory)
	{
		return new ResolutionServiceImpl(commandFactory);
	}

	/**
	 * UDP Server bean declaration
	 *
	 * @return udp server bean declaration
	 */
	@Bean(name = "udpSocketServer")
	public UDPSocketServer udpServerBean()
	{
		try
		{
			return new UDPSocketServer();
		}
		catch (NetworkException e)
		{
			log.error("Failed to init socket server");
			throw new BeanCreationException("udpSocketServer", "Failed to init socket server", e);
		}
	}

	/**
	 * Execution service bean declaration
	 *
	 * @param dao ticket dao instance
	 * @param fms file manipulation service instance
	 * @return execution service instance
	 */
	@Bean(name = "executionServiceImpl")
	public ExecutionService<Ticket> executionServiceImplBean(TicketDao dao, FileManipulationService fms)
	{
		return new ExecutionServiceImpl(dao, fms);
	}

	/**
	 * Request handler bean declaration
	 *
	 * @param dao ticket dao instance
	 * @param resolutionService command resolution service instance
	 * @param executionService command execution service instance
	 * @return request handler instance
	 */
	@Bean(name = "applicationRequestHandler")
	public RequestHandler applicationRequestHandlerBean(
			TicketDao dao,
			ResolutionService resolutionService,
			ExecutionService<Ticket> executionService
	)
	{
		return new DefaultApplicationRequestHandlerImpl(dao, resolutionService, executionService);
	}
}
