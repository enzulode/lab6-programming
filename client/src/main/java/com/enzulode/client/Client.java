package com.enzulode.client;

import com.enzulode.client.parser.cli.CoordinatesCLIParser;
import com.enzulode.client.parser.cli.TicketCLIParser;
import com.enzulode.client.parser.cli.VenueCLIParser;
import com.enzulode.client.util.ConsolePrinter;
import com.enzulode.client.util.Printer;
import com.enzulode.common.resolution.ResolutionService;
import com.enzulode.common.resolution.ResolutionServiceImpl;

import java.util.Scanner;

public class Client
{
	public static void main(String[] args)
	{
		Scanner scanner = new Scanner(System.in);
		Printer printer = new ConsolePrinter();
		ResolutionService resolutionService = new ResolutionServiceImpl();

		CoordinatesCLIParser coordinatesParser = new CoordinatesCLIParser(printer, scanner);
		VenueCLIParser venueParser = new VenueCLIParser(printer, scanner);
		TicketCLIParser ticketParser = new TicketCLIParser(printer, scanner, coordinatesParser, venueParser);

		Console console = new Console(scanner, printer, resolutionService, ticketParser);
		console.run();
	}
}
