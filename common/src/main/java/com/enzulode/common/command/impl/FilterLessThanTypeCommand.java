package com.enzulode.common.command.impl;

import com.enzulode.common.command.SimpleTicketCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
import com.enzulode.common.parser.TypesParser;
import com.enzulode.common.parser.exception.ParsingException;
import com.enzulode.models.util.TicketType;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(force = true)
public class FilterLessThanTypeCommand extends SimpleTicketCommand
{
	public FilterLessThanTypeCommand(List<String> args)
	{
		super(1, args);
	}

	@Override
	public ExecutionResult execute()
	{
		try
		{
			TicketType type = TypesParser.parseTicketType(args.get(0));

			if (type == null)
				return new ExecutionResult(ExecutionStatus.FAILED)
						.append("You should use a correct ticket type as a command argument");

			ExecutionResult result = new ExecutionResult(ExecutionStatus.SUCCEED);
			ticketDao.findLessThanType(type).forEach((el) -> result.append(el.toString()));
			return result;
		}
		catch (ParsingException e)
		{
			return new ExecutionResult(ExecutionStatus.FAILED, e.getMessage());
		}
	}
}
