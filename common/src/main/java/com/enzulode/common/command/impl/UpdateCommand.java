package com.enzulode.common.command.impl;

import com.enzulode.common.command.ElementTicketCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
import com.enzulode.common.dao.exception.DaoException;
import com.enzulode.common.parser.TypesParser;
import com.enzulode.common.parser.exception.ParsingException;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(force = true)
public class UpdateCommand extends ElementTicketCommand
{

	public UpdateCommand(List<String> args)
	{
		super(1, args);
	}

	@Override
	public ExecutionResult execute()
	{
		if (element == null)
			return new ExecutionResult(ExecutionStatus.FAILED, "Element should be provided");

		try
		{
			Integer updatedId = TypesParser.parseInteger(args.get(0));

			if (updatedId == null)
				return new ExecutionResult(ExecutionStatus.FAILED, "Id should be an integer");

			ticketDao.update(updatedId, element);
			return new ExecutionResult(ExecutionStatus.SUCCEED, "Element successfully updated");
		}
		catch (ParsingException | DaoException e)
		{
			return new ExecutionResult(ExecutionStatus.FAILED, e.getMessage());
		}
	}
}
