package com.enzulode.common.command;

import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.models.Ticket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.List;

@NoArgsConstructor(force = true)
@Setter
@Getter
public abstract class ElementTicketCommand extends Command<Ticket>
{
	protected Ticket element;

	public ElementTicketCommand(int argsExpected)
	{
		super(argsExpected);
	}

	public ElementTicketCommand(int argsExpected, List<String> args)
	{
		super(argsExpected, args);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
	{
		super.readExternal(in);
		this.element = (Ticket) in.readObject();
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		super.writeExternal(out);
		out.writeObject(element);
	}

	public abstract ExecutionResult execute();
}
