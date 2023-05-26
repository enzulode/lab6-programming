package com.enzulode.common.command.impl;

import com.enzulode.common.command.SimpleTicketCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@NoArgsConstructor(force = true)
public class HelpCommand extends SimpleTicketCommand
{

	public HelpCommand(List<String> args)
	{
		super(0, args);
	}

	@Override
	public ExecutionResult execute()
	{
		ClassLoader classLoader = HelpCommand.class.getClassLoader();
		try(InputStream helpStream = classLoader.getResourceAsStream("help.txt"))
		{
			if (helpStream == null)
				return new ExecutionResult(ExecutionStatus.FAILED, "failed to open help");

			return new ExecutionResult(ExecutionStatus.SUCCEED, new String(helpStream.readAllBytes()));
		}
		catch (IOException e)
		{
			return new ExecutionResult(ExecutionStatus.FAILED, "Failed to read help")
					.append(e.getMessage());
		}
	}
}
