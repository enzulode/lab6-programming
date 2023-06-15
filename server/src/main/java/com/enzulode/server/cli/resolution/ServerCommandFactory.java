package com.enzulode.server.cli.resolution;

import com.enzulode.common.command.Command;
import com.enzulode.common.command.impl.ExitCommand;
import com.enzulode.common.resolution.CommandFactory;
import com.enzulode.common.resolution.exception.CommandResolutionException;
import com.enzulode.models.Ticket;
import lombok.NonNull;

import java.util.List;

/**
 * Server-side command factory
 *
 */
public class ServerCommandFactory implements CommandFactory<Ticket>
{
	/**
	 * This method instantiates new command instance
	 *
	 * @param commandString command string
	 * @param args command arguments
	 * @return command instance
	 */
	@Override
	public Command<Ticket> getCommand(
			@NonNull String commandString,
			@NonNull List<String> args
	) throws CommandResolutionException
	{
		return switch (commandString)
		{
			case "exit" -> new ExitCommand(args);

			default -> throw new CommandResolutionException("Failed to resolve server command");
		};
	}
}
