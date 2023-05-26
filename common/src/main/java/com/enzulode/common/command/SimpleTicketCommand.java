package com.enzulode.common.command;

import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.models.Ticket;
import lombok.*;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

@NoArgsConstructor(force = true)
@Setter
@Getter
public abstract class SimpleTicketCommand extends Command<Ticket>
{
	public SimpleTicketCommand(int argsExpected)
	{
		super(argsExpected);
	}

	public SimpleTicketCommand(int argsExpected, List<String> args)
	{
		super(argsExpected, args);
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		super.writeExternal(out);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		super.readExternal(in);
	}

	public abstract ExecutionResult execute();
}
