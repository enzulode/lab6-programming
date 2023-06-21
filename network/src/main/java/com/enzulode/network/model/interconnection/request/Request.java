package com.enzulode.network.model.interconnection.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.net.SocketAddress;

/**
 * This class declares base request state and behaviour
 *
 */
@NoArgsConstructor
@Getter
public abstract class Request implements Serializable
{
	/**
	 * Request serial version uid
	 *
	 */
	@Serial
	private static final long serialVersionUID = -5190475676371712986L;

	/**
	 * Request source address
	 *
	 */
	private SocketAddress from;

	/**
	 * Request destination address
	 *
	 */
	private SocketAddress to;

	/**
	 * This constructor allows to instantiate a request via source and destination addresses
	 *
	 * @param from request source address
	 * @param to request destination address
	 */
	public Request(@NonNull SocketAddress from, @NonNull SocketAddress to)
	{
		this.from = from;
		this.to = to;
	}

	/**
	 * Request source address setter
	 *
	 * @param from request source address instance
	 */
	public void setFrom(@NonNull SocketAddress from)
	{
		this.from = from;
	}

	/**
	 * Request destination address setter
	 *
	 * @param to request destination address instance
	 */
	public void setTo(@NonNull SocketAddress to)
	{
		this.to = to;
	}
}
