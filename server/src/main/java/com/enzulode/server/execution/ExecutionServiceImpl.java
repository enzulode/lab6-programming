package com.enzulode.server.execution;

import com.enzulode.common.command.Command;
import com.enzulode.common.command.impl.ExecuteScriptCommand;
import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.command.util.ExecutionStatus;
import com.enzulode.common.dao.Dao;
import com.enzulode.common.execution.ExecutionService;
import com.enzulode.common.filesystem.FileManipulationService;
import com.enzulode.models.Ticket;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implementation of the ExecutionService interface
 *
 */
@RequiredArgsConstructor
public class ExecutionServiceImpl implements ExecutionService<Ticket>
{
	/**
	 * Currently executed scripts are stored there
	 *
	 */
	public static final Set<String> RUNNING_SCRIPTS = new HashSet<>();

	/**
	 * Dao instance
	 *
	 */
	private final Dao<Ticket> dao;

	/**
	 * FileManipulationService instance
	 *
	 */
	private final FileManipulationService fms;

	/**
	 * This method executes a single command
	 *
	 * @param command command being executed
	 * @return command execution result
	 */
	@Override
	public ExecutionResult execute(Command<Ticket> command)
	{
		command.setDao(dao);

		if (command instanceof ExecuteScriptCommand)
		{
			command.setFms(fms);
			command.setExecutionService(this);
		}

		return command.execute();
	}

	/**
	 * This method executes a list of commands from the script
	 *
	 * @param filename script filename
	 * @param commands a list of commands to be executed
	 * @return an execution result of script execution
	 */
	@Override
	public ExecutionResult execute(String filename, List<Command<Ticket>> commands)
	{
		if (RUNNING_SCRIPTS.contains(filename))
			return new ExecutionResult(ExecutionStatus.FAILED)
					.append("Execution failed because of detected recursion");

		RUNNING_SCRIPTS.add(filename);

		ExecutionResult result = new ExecutionResult(ExecutionStatus.SUCCEED);

		for (Command<Ticket> command : commands)
		{
			command.setDao(dao);

			if (command instanceof ExecuteScriptCommand)
			{
				command.setFms(fms);
				command.setExecutionService(this);
			}

			try
			{
				ExecutionResult commandResult = command.execute();
				result.append(commandResult.getMessage());
			}
			catch (Exception e)
			{
				RUNNING_SCRIPTS.remove(filename);
				return new ExecutionResult(ExecutionStatus.FAILED)
						.append("Failed to execute '" + filename + "' script:")
						.append(e.getMessage());
			}
		}

		RUNNING_SCRIPTS.remove(filename);
		return result;
	}
}
