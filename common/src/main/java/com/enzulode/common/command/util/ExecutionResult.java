package com.enzulode.common.command.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

@NoArgsConstructor(force = true)
public final class ExecutionResult implements Externalizable
{
	@Getter
	private ExecutionStatus status;
	@Getter
	private String message;
	private StringBuilder sb;

	public ExecutionResult(@NonNull ExecutionStatus status)
	{
		this.status = status;
		this.sb = new StringBuilder();
	}

	public ExecutionResult(@NonNull ExecutionStatus status, @NonNull String str)
	{
		this.status = status;
		this.sb = new StringBuilder();
		append(str);
	}

	public ExecutionResult append(String line)
	{
		sb.append(line).append('\n');
		this.message = sb.toString();
		return this;
	}

	@Override
	public String toString()
	{
		return message;
	}


	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeObject(status);
		out.writeObject(message);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		this.status = (ExecutionStatus) in.readObject();
		this.message = (String) in.readObject();
	}
}
