package com.enzulode.common.command.impl;

import com.enzulode.common.command.SimpleTicketCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
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
		return new ExecutionResult(ExecutionStatus.SUCCEED)
				.append("Type: ArrayList collection")
				.append("Init date: " + dao.getCreationDate())
				.append("Stored elements amount: " + dao.size());
	}
}
