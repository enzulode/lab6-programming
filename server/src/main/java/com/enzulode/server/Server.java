package com.enzulode.server;

import com.enzulode.common.execution.ExecutionService;
import com.enzulode.common.resolution.ResolutionService;
import com.enzulode.common.resolution.ResolutionServiceImpl;
import com.enzulode.models.Ticket;
import com.enzulode.network.UDPSocketServer;
import com.enzulode.network.exception.NetworkException;
import com.enzulode.network.handling.RequestHandler;
import com.enzulode.server.cli.ServerUIThread;
import com.enzulode.server.execution.ExecutionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import sun.misc.Signal;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@SpringBootApplication
@Slf4j
public class Server
{
	public static void main(String[] args)
	{
//		Server application context initialization
		ApplicationContext context = SpringApplication.run(Server.class, args);
		log.info("Application context successfully initialized");

		UDPSocketServer server = context.getBean(UDPSocketServer.class);
		log.info("UPDServer successfully started at port: " + server.getServerAddress().getPort());

//		Prepare instances to handle commands from server command line
		BufferedReader cliReader = new BufferedReader(new InputStreamReader(System.in));
		ResolutionService resolutionService = context.getBean(ResolutionServiceImpl.class);
		ExecutionService<Ticket> executionService = context.getBean(ExecutionServiceImpl.class);

//		Registered signal handler
	    Signal.handle(
				new Signal("INT"),
			    (signal) -> {
					log.info("Caught an interruption signal: CTRL + C was pressed");
					log.info("Server application shutdown initiated");
					System.exit(0);
	            }
		);

//		Adding request handler
		RequestHandler handler = context.getBean(RequestHandler.class);
		server.subscribe(handler);

//		Starting server ui thread
		(new ServerUIThread(cliReader, resolutionService, executionService, log)).start();

//		Handling the request
		try
		{
			server.handleIncomingRequests();
		}
		catch (NetworkException e)
		{
			log.error("Failed to process request", e);
		}
	}
}
