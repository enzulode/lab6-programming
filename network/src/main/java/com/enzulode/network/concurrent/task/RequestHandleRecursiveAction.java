package com.enzulode.network.concurrent.task;

import com.enzulode.network.handle.RequestHandler;
import com.enzulode.network.model.interconnection.request.Request;
import com.enzulode.network.model.interconnection.request.impl.PingRequest;
import com.enzulode.network.model.interconnection.response.Response;
import com.enzulode.network.model.interconnection.response.ResponseCode;
import com.enzulode.network.model.interconnection.response.impl.PongResponse;
import lombok.NonNull;

import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RecursiveAction;

/**
 * This recursive task handles the request and composes a response
 *
 */
public class RequestHandleRecursiveAction extends RecursiveAction
{
	/**
	 * Datagram socket instance
	 *
	 */
	private final DatagramSocket socket;

	/**
	 * Request to be handled
	 *
	 */
	private final Request request;

	/**
	 * Request handler instance
	 *
	 */
	private final RequestHandler handler;

	/**
	 * Specific executor service instance
	 *
	 */
	private final ExecutorService responseSendingThreadPool;

	/**
	 * This task handles a request and executes the response sending
	 *
	 * @param socket socket instance
	 * @param request request instance
	 * @param handler request handler instance
	 * @param responseSendingThreadPool responding thread pool instance
	 */
	public RequestHandleRecursiveAction(
			@NonNull DatagramSocket socket,
			@NonNull Request request,
			@NonNull RequestHandler handler,
			@NonNull ExecutorService responseSendingThreadPool
	)
	{
		super();

		this.socket = socket;
		this.request = request;
		this.handler = handler;
		this.responseSendingThreadPool = responseSendingThreadPool;
	}

	/**
	 * The main computation performed by this task
	 *
	 */
	@Override
	protected void compute()
	{
		Response response;
		if (request instanceof PingRequest)
			response = new PongResponse(ResponseCode.SUCCEED);
		else
			response = handler.handle(request);

		response.setFrom(request.getTo());
		response.setTo(request.getFrom());

		responseSendingThreadPool.submit(new SendResponseTask(socket, response));
	}
}
