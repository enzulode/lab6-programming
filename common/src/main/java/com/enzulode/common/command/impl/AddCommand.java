package com.enzulode.common.command.impl;

import com.enzulode.common.command.ElementTicketCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
import com.enzulode.common.dao.exception.DaoException;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(force = true)
public class AddCommand extends ElementTicketCommand
{
	public AddCommand(List<String> args)
	{
		super(0, args);
	}

	@Override
	public ExecutionResult execute()
	{
		if (element == null)
			return new ExecutionResult(ExecutionStatus.FAILED, "Element should be provided");

		try
		{
			ticketDao.create(element);
			return new ExecutionResult(ExecutionStatus.SUCCEED, "Element successfully added");
		}
		catch (DaoException e)
		{
			return new ExecutionResult(ExecutionStatus.FAILED, e.getMessage());
		}
	}
}
