package com.enzulode.common.resolution;

import com.enzulode.common.command.Command;
import com.enzulode.common.command.impl.AddCommand;
import com.enzulode.common.command.impl.ExecuteScriptCommand;
import com.enzulode.common.command.impl.UpdateCommand;
import com.enzulode.common.parser.ParserMode;
import com.enzulode.common.parser.exception.ParsingException;
import com.enzulode.common.parser.script.TicketScriptParser;
import com.enzulode.common.resolution.exception.CommandResolutionException;
import com.enzulode.common.validation.TicketValidator;
import com.enzulode.common.validation.exception.ValidationException;
import com.enzulode.models.Ticket;
import lombok.NonNull;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ResolutionServiceImpl implements ResolutionService
{
	@Override
	@SuppressWarnings("unchecked")
	public <T extends Command<Ticket>> T resolveCommand(@NonNull String commandLine) throws CommandResolutionException
	{
		String[] splittedCommand = commandLine.strip().split(" ");
		List<String> args = Arrays.asList(Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length));


		Command<Ticket> command = CommandFactory.getCommand(splittedCommand[0], args);

		if (command.getArgsExpected() != command.getArgs().size())
			throw new CommandResolutionException("Invalid amount of arguments");

		if (command instanceof ExecuteScriptCommand)
			command.setResolutionService(this);

		return (T) command;
	}

	@Override
	public List<Command<Ticket>> resolveScript(@NonNull BufferedReader reader, @NonNull List<String> scriptLines) throws CommandResolutionException
	{
		List<Command<Ticket>> commands = new ArrayList<>();

		try(Scanner scanner = new Scanner(reader))
		{
			while (scanner.hasNextLine())
			{
				Command<Ticket> command = resolveCommand(scanner.nextLine().strip());

				if (command.getArgs().size() != command.getArgsExpected())
					throw new CommandResolutionException("Command got invalid amount of arguments");

				if (command instanceof AddCommand cmd)
				{
					Ticket ticket = TicketScriptParser.parseTicket(scanner, ParserMode.CREATE);
					TicketValidator.validateTicket(ticket);
					cmd.setElement(ticket);
				}

				if (command instanceof UpdateCommand cmd)
				{
					Ticket ticket = TicketScriptParser.parseTicket(scanner, ParserMode.UPDATE);
					TicketValidator.validateTicket(ticket);
					cmd.setElement(ticket);
				}

				if (command instanceof ExecuteScriptCommand)
					command.setResolutionService(this);

				commands.add(command);
			}
		}
		catch (ValidationException | ParsingException e)
		{
			throw new CommandResolutionException("Failed to resolve script: " + e.getMessage());
		}

		return commands;
	}
}
