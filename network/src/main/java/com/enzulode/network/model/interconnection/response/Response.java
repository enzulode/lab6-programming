package com.enzulode.network.model.interconnection.response;

import lombok.Getter;
import lombok.NonNull;

import java.io.Serial;
import java.io.Serializable;
import java.net.SocketAddress;

/**
 * This class declares base response state and behaviour
 *
 */
@Getter
public abstract class Response implements Serializable
{
	/**
	 * Response serial version uid
	 *
	 */
	@Serial
	private static final long serialVersionUID = 724958772899600505L;

	/**
	 * Response source address
	 *
	 */
	private SocketAddress from;

	/**
	 * Response destination address
	 *
	 */
	private SocketAddress to;

	/**
	 * Response code
	 *
	 */
	private final ResponseCode responseCode;

	/**
	 * This constructor allows to instantiate a response with provided response code
	 *
	 * @param responseCode response code
	 */
	public Response(@NonNull ResponseCode responseCode)
	{
		this.responseCode = responseCode;
	}

	/**
	 * Complete response constructor
	 *
	 * @param responseCode response code
	 * @param from source socket address instance
	 * @param to destination socket address instance
	 */
	public Response(@NonNull ResponseCode responseCode, @NonNull SocketAddress from, @NonNull SocketAddress to)
	{
		this.responseCode = responseCode;
		this.from = from;
		this.to = to;
	}

	/**
	 * Response source address setter
	 *
	 * @param from response source address instance
	 */
	public void setFrom(@NonNull SocketAddress from)
	{
		this.from = from;
	}

	/**
	 * Response destination address setter
	 *
	 * @param to response destination address instance
	 */
	public void setTo(@NonNull SocketAddress to)
	{
		this.to = to;
	}
}
