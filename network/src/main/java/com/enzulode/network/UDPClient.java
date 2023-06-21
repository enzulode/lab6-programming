package com.enzulode.network;

import com.enzulode.network.exception.FailedToCloseClientException;
import com.enzulode.network.exception.NetworkException;
import com.enzulode.network.model.interconnection.request.Request;
import com.enzulode.network.model.interconnection.response.Response;
import lombok.NonNull;

public interface UDPClient extends AutoCloseable
{
	<T extends Response> T sendRequestAndWaitResponse(@NonNull Request request) throws NetworkException;

	@Override
	void close() throws FailedToCloseClientException;
}
