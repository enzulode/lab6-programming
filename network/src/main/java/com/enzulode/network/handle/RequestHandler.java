package com.enzulode.network.handle;

import com.enzulode.network.model.interconnection.request.Request;
import com.enzulode.network.model.interconnection.response.Response;

/**
 * Request handling functional interface
 *
 */
@FunctionalInterface
public interface RequestHandler
{
	/**
	 * This method contains request handling logic
	 *
	 * @param request request instance
	 * @return response instance
	 */
	Response handle(Request request);
}
