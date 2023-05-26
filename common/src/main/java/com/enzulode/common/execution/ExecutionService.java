package com.enzulode.common.execution;

import com.enzulode.common.command.Command;
import com.enzulode.common.command.util.ExecutionResult;

import java.util.List;

/**
 * Execution service abstraction
 *
 * @param <T> defines commands, execution service will interact with
 */
public interface ExecutionService<T>
{
	/**
	 * This method executes a single command
	 *
	 * @param command command being executed
	 * @return command execution result
	 */
	ExecutionResult execute(Command<T> command);

	/**
	 * This method executes a list of commands from the script
	 *
	 * @param filename script filename
	 * @param commands a list of commands to be executed
	 * @return an execution result of script execution
	 */
	ExecutionResult execute(String filename, List<Command<T>> commands);
}
