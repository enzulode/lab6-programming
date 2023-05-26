package com.enzulode.common.command.impl;

import com.enzulode.common.command.SimpleTicketCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(force = true)
public class ExitCommand extends SimpleTicketCommand
{
	public ExitCommand(List<String> args)
	{
		super(0, args);
	}

	@Override
	public ExecutionResult execute()
	{
		System.exit(0);
		return new ExecutionResult(ExecutionStatus.SUCCEED, "Terminating current connection side");
	}
}
