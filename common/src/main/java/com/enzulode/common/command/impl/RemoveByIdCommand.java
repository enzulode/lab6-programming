package com.enzulode.common.command.impl;

import com.enzulode.common.command.SimpleTicketCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
import com.enzulode.common.dao.exception.DaoException;
import com.enzulode.common.parser.TypesParser;
import com.enzulode.common.parser.exception.ParsingException;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(force = true)
public class RemoveByIdCommand extends SimpleTicketCommand
{
	public RemoveByIdCommand(List<String> args)
	{
		super(1, args);
	}

	@Override
	public ExecutionResult execute()
	{
		try
		{
			Integer idToRemove = TypesParser.parseInteger(args.get(0));
			if (idToRemove == null)
				return new ExecutionResult(ExecutionStatus.FAILED, "Invalid command argument provided");

			ticketDao.delete(idToRemove);
			return new ExecutionResult(ExecutionStatus.SUCCEED, "Successfully removed element");
		}
		catch (DaoException e)
		{
			return new ExecutionResult(ExecutionStatus.FAILED, e.getMessage());
		}
		catch (ParsingException e)
		{
			return new ExecutionResult(ExecutionStatus.FAILED, "Invalid command argument detected");
		}
	}
}
