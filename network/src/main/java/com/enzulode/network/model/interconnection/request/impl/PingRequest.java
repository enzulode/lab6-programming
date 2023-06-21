package com.enzulode.network.model.interconnection.request.impl;

import com.enzulode.network.model.interconnection.request.Request;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serial;
import java.net.SocketAddress;

/**
 * This request is made for remote side status checking
 *
 */
@NoArgsConstructor
public final class PingRequest extends Request
{
	/**
	 * Ping request serial version uid
	 *
	 */
	@Serial
	private static final long serialVersionUID = 1699685396516441836L;

	/**
	 * This constructor allows to instantiate a ping request via source and destination addresses
	 *
	 * @param from request source address
	 * @param to request destination address
	 */
	public PingRequest(@NonNull SocketAddress from, @NonNull SocketAddress to)
	{
		super(from, to);
	}
}
