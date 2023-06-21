package com.enzulode.common.network.request;

import com.enzulode.common.command.Command;
import com.enzulode.common.command.SimpleTicketCommand;
import com.enzulode.models.Ticket;
import com.enzulode.network.model.interconnection.request.Request;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serial;
import java.net.SocketAddress;

/**
 * This class represents command request
 *
 */
@Getter
public class CommandRequest extends Request
{
	/**
	 * Serial version UID
	 *
	 */
	@Serial
	private static final long serialVersionUID = 8700465110700560527L;
	/**
	 * SimpleTicketCommand instance
	 *
	 */
	private final Command<Ticket> command;

	/**
	 * SimpleCommandRequest constructor without source and destination addresses provided
	 *
	 * @param command simple ticket command to be executed
	 */
	public CommandRequest(@NonNull Command<Ticket> command)
	{
		super();
		this.command = command;
	}

	/**
	 * SimpleCommandRequest constructor
	 *
	 * @param from request source address
	 * @param to request destination address
	 * @param command command to be executed
	 */
	public CommandRequest(
			@NonNull SocketAddress from,
			@NonNull SocketAddress to,
			@NonNull SimpleTicketCommand command
	)
	{
		super(from, to);
		this.command = command;
	}
}
