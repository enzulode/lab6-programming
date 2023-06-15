package com.enzulode.common.command;

import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.dao.TicketDao;
import com.enzulode.common.execution.ExecutionService;
import com.enzulode.common.filesystem.FileManipulationService;
import com.enzulode.common.resolution.ResolutionService;
import lombok.*;

import java.io.*;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Setter
@Getter
public abstract class Command<T> implements Externalizable
{
	@Serial
	private static final long serialVersionUID = -6810630369479732525L;

	private final int argsExpected;

	protected List<String> args;

	protected TicketDao ticketDao;
	protected ExecutionService<T> executionService;
	protected ResolutionService resolutionService;
	protected FileManipulationService fms;

	public Command(int argsExpected)
	{
		this(argsExpected, Collections.emptyList());
	}

	public Command(int argsExpected, List<String> args)
	{
		this.argsExpected = argsExpected;
		this.args = args;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeObject(args);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		this.args = (List<String>) in.readObject();
	}

	public abstract ExecutionResult execute();
}
