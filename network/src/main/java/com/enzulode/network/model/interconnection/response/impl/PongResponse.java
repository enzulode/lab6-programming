package com.enzulode.network.model.interconnection.response.impl;

import com.enzulode.network.model.interconnection.response.Response;
import com.enzulode.network.model.interconnection.response.ResponseCode;
import lombok.NonNull;

import java.io.Serial;

/**
 * This response gives the destination side information about the response-sending side status
 *
 */
public final class PongResponse extends Response
{
	/**
	 * Pong response serial version uid
	 *
	 */
	@Serial
	private static final long serialVersionUID = 3749300938078758835L;

	/**
	 * This constructor allows to instantiate a pong response with provided response code
	 *
	 * @param responseCode response code
	 */
	public PongResponse(@NonNull ResponseCode responseCode)
	{
		super(responseCode);
	}
}
