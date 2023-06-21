package com.enzulode.server;

import com.enzulode.network.UDPServer;
import com.enzulode.network.exception.NetworkException;
import com.enzulode.network.handle.RequestHandler;
import com.enzulode.server.cli.factory.ServerCLIThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import sun.misc.Signal;

@SpringBootApplication
@Slf4j
public class Server
{
	public static void main(String[] args)
	{
//		Server application context initialization
		ApplicationContext context = SpringApplication.run(Server.class, args);
		log.info("Application context successfully initialized");

		UDPServer server = context.getBean(UDPServer.class);
		log.info("UPD server successfully started");

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
		var serverCLIFactory = context.getBean(ServerCLIThreadFactory.class);
		serverCLIFactory.newServerCLIThread().start();

//		Handling the request
		try
		{
			server.start();
		}
		catch (NetworkException e)
		{
			log.error("Failed to process request", e);
		}
	}
}
