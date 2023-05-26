package com.enzulode.common.resolution;

import com.enzulode.common.command.Command;
import com.enzulode.common.command.impl.*;
import com.enzulode.common.resolution.exception.CommandResolutionException;
import com.enzulode.models.Ticket;
import lombok.NonNull;

import java.util.List;

/**
 * Command instance factory
 *
 */
public class CommandFactory
{
	/**
	 * Command instance factory method
	 *
	 * @param commandString command word
	 * @param args command arguments
	 * @return command instance
	 * @param <T> command type parameter
	 * @throws CommandResolutionException if it's failed to resolve a command
	 */
	@SuppressWarnings({"unchecked"})
	public static <T extends Command<Ticket>> T getCommand(
			@NonNull String commandString,
			@NonNull List<String> args
	) throws CommandResolutionException
	{
		Command<Ticket> command = switch (commandString)
		{
			case "execute_script" -> new ExecuteScriptCommand(args);
			case "add" -> new AddCommand(args);
			case "clear" -> new ClearCommand(args);
			case "exit" -> new ExitCommand(args);
			case "filter_less_than_type" -> new FilterLessThanTypeCommand(args);
			case "help" -> new HelpCommand(args);
			case "info" -> new InfoCommand(args);
			case "print_field_descending_price" -> new PrintFieldDescendingPriceCommand(args);
			case "remove_any_by_refundable" -> new RemoveAnyByRefundableCommand(args);
			case "remove_by_id" -> new RemoveByIdCommand(args);
			case "remove_first" -> new RemoveFirstCommand(args);
			case "remove_last" -> new RemoveLastCommand(args);
			case "remove_lower" -> new RemoveLowerCommand(args);
			case "save" -> new SaveCommand(args);
			case "show" -> new ShowCommand(args);
			case "update" -> new UpdateCommand(args);

			default -> throw new CommandResolutionException("Unable resolve command");
		};

		return (T) command;
	}
}
