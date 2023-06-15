package com.enzulode.common.resolution;

import com.enzulode.common.command.Command;
import com.enzulode.common.resolution.exception.CommandResolutionException;
import lombok.NonNull;

import java.util.List;

/**
 * This interface declares command factory methods
 *
 * @param <T> command operating types
 */
public interface CommandFactory<T>
{
	/**
	 * This method instantiates new command instance
	 *
	 * @return a command instance
	 */
	Command<T> getCommand(@NonNull String commandString, @NonNull List<String> args) throws CommandResolutionException;
}
