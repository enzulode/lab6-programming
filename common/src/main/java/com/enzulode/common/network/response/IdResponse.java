package com.enzulode.common.network.response;

import com.enzulode.network.model.interconnection.Response;
import com.enzulode.network.model.interconnection.util.ResponseCode;
import lombok.Getter;

import java.io.Serial;

/**
 * The response code represents id existence
 *
 */
@Getter
public class IdResponse extends Response
{
	/**
	 * Serial version UID
	 *
	 */
	@Serial
	private static final long serialVersionUID = 5301898658957513399L;

	/**
	 * Response message
	 *
	 */
	private final String message;

	/**
	 * Response constructor without source and destination address provided
	 *
	 * @param code response code
	 */
	public IdResponse(ResponseCode code, String message)
	{
		super(code);
		this.message = message;
	}
}
