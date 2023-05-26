package com.enzulode.common.network.response;

import com.enzulode.common.command.util.ExecutionResult;
import com.enzulode.network.model.interconnection.Response;
import com.enzulode.network.model.interconnection.util.ResponseCode;
import lombok.Getter;
import lombok.NonNull;

import java.net.InetSocketAddress;

/**
 * This class represents simple command response
 *
 */
@Getter
public final class CommandResponse extends Response
{

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
			@NonNull InetSocketAddress from,
			@NonNull InetSocketAddress to,
			@NonNull ResponseCode code,
			@NonNull ExecutionResult result
	)
	{
		super(from, to, code);
		this.result = result;
	}
}
