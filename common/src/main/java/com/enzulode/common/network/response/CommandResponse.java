package com.enzulode.common.network.response;

import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.network.model.interconnection.response.Response;
import com.enzulode.network.model.interconnection.response.ResponseCode;
import lombok.Getter;
import lombok.NonNull;

import java.io.Serial;
import java.net.SocketAddress;

/**
 * This class represents simple command response
 *
 */
@Getter
public final class CommandResponse extends Response
{

	/**
	 * Serial version UID
	 *
	 */
	@Serial
	private static final long serialVersionUID = -1065876023989594121L;
	/**
	 * Command execution result
	 *
	 */
	private final ExecutionResult result;

	/**
	 * SimpleCommandResponse constructor without source and destination address provided
	 *
	 * @param code response code
	 */
	public CommandResponse(@NonNull ResponseCode code, @NonNull ExecutionResult result)
	{
		super(code);
		this.result = result;
	}

	/**
	 * SimpleCommand response constructor
	 *
	 * @param from source address
	 * @param to destination address
	 * @param code response code
	 * @param result command execution result
	 */
	public CommandResponse(
			@NonNull SocketAddress from,
			@NonNull SocketAddress to,
			@NonNull ResponseCode code,
			@NonNull ExecutionResult result
	)
	{
		super(code, from, to);
		this.result = result;
	}
}
