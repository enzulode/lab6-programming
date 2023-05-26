package com.enzulode.server;

import com.enzulode.common.command.Command;
import com.enzulode.common.execution.ExecutionService;
import com.enzulode.common.network.request.CommandRequest;
import com.enzulode.common.network.request.IdRequest;
import com.enzulode.common.network.response.CommandResponse;
import com.enzulode.common.network.response.IdResponse;
import com.enzulode.common.resolution.ResolutionServiceImpl;
import com.enzulode.common.resolution.exception.CommandResolutionException;
import com.enzulode.models.Ticket;
import com.enzulode.network.UDPSocketServer;
import com.enzulode.network.exception.NetworkException;
import com.enzulode.network.model.interconnection.util.ResponseCode;
import com.enzulode.server.dao.TicketDaoImpl;
import com.enzulode.server.database.TicketDatabaseImpl;
import com.enzulode.server.database.exception.DatabaseException;
import com.enzulode.server.execution.ExecutionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import sun.misc.Signal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@SpringBootApplication
public class Server
{
	public static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

	public static void main(String[] args)
	{
//		Server application context initialization
		ApplicationContext context = SpringApplication.run(Server.class, args);
		LOGGER.info("Application context successfully initialized");

//		Trying to load the database: if it fails - initializing an empty database
		TicketDatabaseImpl database = context.getBean(TicketDatabaseImpl.class);
		try
		{
			database.load();
			LOGGER.info("Database loading succeed");
		}
		catch (DatabaseException e)
		{
			LOGGER.warn("Failed to load the database. Initializing an empty database", e);
			database.initEmptyDb();
		}

		UDPSocketServer server = context.getBean(UDPSocketServer.class);
		LOGGER.info("UPDServer successfully started at port: " + server.getServerAddress().getPort());

//		Prepare instances to handle commands from server command line
		BufferedReader cliReader = new BufferedReader(new InputStreamReader(System.in));
		ResolutionServiceImpl resolutionService = context.getBean(ResolutionServiceImpl.class);
		ExecutionService<Ticket> executionService = context.getBean(ExecutionServiceImpl.class);
		TicketDaoImpl dao = context.getBean(TicketDaoImpl.class);

//		Registered signal handler
	    Signal.handle(new Signal("INT"), (signal) -> {
			LOGGER.info("Caught an interruption signal: CTRL + C was pressed");
			LOGGER.info("Server application shutdown initiated");
			System.exit(0);
	    });

//		Adding request handler
		server.addRequestHandler((request) -> {

			if (request instanceof CommandRequest req)
			{
				req.getCommand().setResolutionService(resolutionService);
				req.getCommand().setExecutionService(executionService);

				LOGGER.info("Proceeding new request: " + req.getClass().getSimpleName());
				return new CommandResponse(
						ResponseCode.SUCCEED,
						executionService.execute(req.getCommand())
				);
			}

			if (request instanceof IdRequest req)
			{
				ResponseCode code = (dao.checkIdExistence(req.getId())) ? ResponseCode.SUCCEED : ResponseCode.FAILED;

				if (code == ResponseCode.FAILED)
					return new IdResponse(code, "No such id found");
				else
					return new IdResponse(code, "Id was found");
			}

			return null;
		});

		while (true)
		{
//			Resolving commands from the server cli
			try
			{
				if (cliReader.ready())
				{
					Command<Ticket> command = resolutionService.resolveCommand(cliReader.readLine());
					LOGGER.info("Got command from server console: " + command.getClass().getSimpleName());
					LOGGER.info(executionService.execute(command).getMessage());
				}
			}
			catch (CommandResolutionException e)
			{
				LOGGER.warn("Such command does not exist");
			}
			catch (IOException e)
			{
				LOGGER.error("Something went wrong with stdin on the server side", e);
			}

//			Handling the request
			try
			{
				server.handleRequest();
			}
			catch (NetworkException e)
			{
				LOGGER.error("Failed to process request", e);
			}
		}
	}
}
