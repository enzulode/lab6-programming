package com.enzulode.common.command.impl;

import com.enzulode.common.command.SimpleTicketCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
import com.enzulode.common.dao.exception.DaoException;
import com.enzulode.models.Ticket;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(force = true)
public class PrintFieldDescendingPriceCommand extends SimpleTicketCommand
{
	public PrintFieldDescendingPriceCommand(List<String> args)
	{
		super(0, args);
	}

	@Override
	public ExecutionResult execute()
	{
		try
		{
			List<Float> prices = ticketDao
					.findAll()
					.stream()
					.map(Ticket::getPrice)
					.sorted((el1, el2) -> Float.compare(el2, el1))
					.toList();

			ExecutionResult result = new ExecutionResult(ExecutionStatus.SUCCEED);
			prices.forEach((price) -> result.append(price.toString()));
			return result;
		}
		catch (DaoException e)
		{
			return new ExecutionResult(ExecutionStatus.FAILED, "Failed to perform operation");
		}
	}
}
