package com.enzulode.client;

import com.enzulode.client.parser.cli.CoordinatesCLIParser;
import com.enzulode.client.parser.cli.TicketCLIParser;
import com.enzulode.client.parser.cli.VenueCLIParser;
import com.enzulode.client.util.ConsolePrinter;
import com.enzulode.client.util.Printer;
import com.enzulode.client.resolution.ClientCommandFactory;
import com.enzulode.common.resolution.CommandFactory;
import com.enzulode.common.resolution.ResolutionService;
import com.enzulode.common.resolution.ResolutionServiceImpl;
import com.enzulode.models.Ticket;

import java.util.Scanner;

public class Client
{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		Printer printer = new ConsolePrinter();
		CommandFactory<Ticket> commandFactory = new ClientCommandFactory();
		ResolutionService resolutionService = new ResolutionServiceImpl(commandFactory);

		CoordinatesCLIParser coordinatesParser = new CoordinatesCLIParser(printer, scanner);
		VenueCLIParser venueParser = new VenueCLIParser(printer, scanner);
		TicketCLIParser ticketParser = new TicketCLIParser(printer, scanner, coordinatesParser, venueParser);

		Console console = new Console(scanner, printer, resolutionService, ticketParser);
		console.run();
	}
}
