package com.enzulode.common.network.request;

import com.enzulode.network.model.interconnection.request.Request;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serial;

/**
 * This class is an id checking request
 *
 */
@RequiredArgsConstructor
@Getter
public class IdRequest extends Request
{
	/**
	 * Serial version UID
	 *
	 */
	@Serial
	private static final long serialVersionUID = -3346580676163311238L;

	/**
	 * An id to be checked
	 *
	 */
	private final int id;
}
