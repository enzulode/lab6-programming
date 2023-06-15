package com.enzulode.common.command.impl;

import com.enzulode.common.command.SimpleTicketCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
import com.enzulode.common.dao.exception.DaoException;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(force = true)
public class ClearCommand extends SimpleTicketCommand
{
	public ClearCommand(List<String> args)
	{
		super(0, args);
	}

	@Override
	public ExecutionResult execute()
	{
		try
		{
			ticketDao.clear();
			return new ExecutionResult(ExecutionStatus.SUCCEED, "The database successfully cleared");
		}
		catch (DaoException e)
		{
			return new ExecutionResult(ExecutionStatus.FAILED, e.getMessage());
		}
	}
}
