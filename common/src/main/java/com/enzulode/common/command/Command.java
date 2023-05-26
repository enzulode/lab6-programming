package com.enzulode.common.command;

import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.common.dao.Dao;
import com.enzulode.common.execution.ExecutionService;
import com.enzulode.common.filesystem.FileManipulationService;
import com.enzulode.common.resolution.ResolutionService;
import lombok.*;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Setter
@Getter
public abstract class Command<T> implements Externalizable
{
	private final int argsExpected;

	protected List<String> args;

	protected Dao<T> dao;
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
