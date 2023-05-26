package com.enzulode.common.command.impl;

import com.enzulode.common.command.Command;
import com.enzulode.common.command.SimpleTicketCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
import com.enzulode.common.filesystem.exception.FileException;
import com.enzulode.common.resolution.exception.CommandResolutionException;
import com.enzulode.models.Ticket;
import lombok.NoArgsConstructor;

import java.io.BufferedReader;
import java.util.List;

@NoArgsConstructor(force = true)
public final class ExecuteScriptCommand extends SimpleTicketCommand
{
	public ExecuteScriptCommand(List<String> args)
	{
		super(1, args);
	}

	@Override
	public ExecutionResult execute()
	{
		String scriptFilename = args.get(0);

		try
		{
//			Trying to get file reader instance for elements reading
			BufferedReader reader = fms.getReaderByName(scriptFilename);

//			Reading script lines
			List<String> scriptLines = fms.readFileLines(fms.getFileByName(scriptFilename));

//			Parsing commands from script
			List<Command<Ticket>> scriptCommands = resolutionService.resolveScript(reader, scriptLines);

			return executionService.execute(scriptFilename, scriptCommands);
		}
		catch (CommandResolutionException e)
		{
			return new ExecutionResult(ExecutionStatus.FAILED, "Failed to resolve script");
		}
		catch (FileException e)
		{
			return new ExecutionResult(ExecutionStatus.FAILED)
					.append("Failed to execute script: " + e.getMessage());
		}
	}
}
