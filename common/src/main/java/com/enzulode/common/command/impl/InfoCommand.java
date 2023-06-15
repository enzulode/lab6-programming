package com.enzulode.common.command.impl;

import com.enzulode.common.command.SimpleTicketCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
import com.enzulode.common.dao.exception.DaoException;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(force = true)
public class InfoCommand extends SimpleTicketCommand
{
	public InfoCommand(List<String> args)
	{
		super(0, args);
	}

	@Override
	public ExecutionResult execute()
	{
		try
		{
			return new ExecutionResult(ExecutionStatus.SUCCEED)
				.append("Type: ArrayList collection")
				.append("Init date: " + ticketDao.getCreationDate())
				.append("Stored elements amount: " + ticketDao.size());
		}
		catch (DaoException e)
		{
			return new ExecutionResult(ExecutionStatus.FAILED, "Failed to retrieve database metadata");
		}
	}
}
