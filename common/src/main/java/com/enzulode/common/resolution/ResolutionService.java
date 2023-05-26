package com.enzulode.common.resolution;

import com.enzulode.common.command.Command;
import com.enzulode.common.resolution.exception.CommandResolutionException;
import com.enzulode.models.Ticket;
import lombok.NonNull;

import java.io.BufferedReader;
import java.util.List;

public interface ResolutionService
{
	<T extends Command<Ticket>> T resolveCommand(@NonNull String commandLine) throws CommandResolutionException;
	List<Command<Ticket>> resolveScript(@NonNull BufferedReader reader, @NonNull List<String> scriptLines) throws CommandResolutionException;
}
