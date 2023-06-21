package com.enzulode.common.network.response;

import com.enzulode.network.model.interconnection.response.Response;
import com.enzulode.network.model.interconnection.response.ResponseCode;
import lombok.Getter;
import lombok.NonNull;

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
	public IdResponse(@NonNull ResponseCode code, @NonNull String message)
	{
		super(code);
		this.message = message;
	}
}
