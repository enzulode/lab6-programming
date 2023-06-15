package com.enzulode.common.command.impl;

import com.enzulode.common.command.ElementTicketCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
import com.enzulode.common.dao.exception.DaoException;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(force = true)
public class RemoveLowerCommand extends ElementTicketCommand
{
	public RemoveLowerCommand(List<String> args)
	{
		super(0, args);
	}

	@Override
	public ExecutionResult execute()
	{
		if (element == null)
			return new ExecutionResult(ExecutionStatus.FAILED, "No element was provided");

		try
		{
			ticketDao.removeLower(element);
			return new ExecutionResult(ExecutionStatus.SUCCEED, "Successfully removed element");
		}
		catch (DaoException e)
		{
			return new ExecutionResult(ExecutionStatus.FAILED, e.getMessage());
		}
	}
}
