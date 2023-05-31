package com.enzulode.common.command.impl;

import com.enzulode.common.command.SimpleTicketCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
import com.enzulode.models.Ticket;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(force = true)
public class ShowCommand extends SimpleTicketCommand
{

	public ShowCommand(List<String> args)
	{
		super(0, args);
	}

	@Override
	public ExecutionResult execute()
	{
		if (args.size() != 0)
			return new ExecutionResult(ExecutionStatus.FAILED, "Command does not consume arguments");

		ExecutionResult result = new ExecutionResult(ExecutionStatus.SUCCEED);

		List<Ticket> tickets = dao.findAll();
		if (tickets.size() == 0)
			return new ExecutionResult(ExecutionStatus.SUCCEED, "No elements found");

		tickets.forEach((el) -> result.append(el.toString()));

		return result;
	}
}
