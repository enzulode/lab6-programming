package com.enzulode.network;

import com.enzulode.network.concurrent.task.RequestHandleRecursiveAction;
import com.enzulode.network.concurrent.task.RequestReceiveRecursiveAction;
import com.enzulode.network.exception.RequestListeningFailedException;
import com.enzulode.network.exception.ServerInitializationFailedException;
import com.enzulode.network.handle.RequestHandler;
import com.enzulode.network.model.interconnection.request.Request;
import lombok.NonNull;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.*;

/**
 * A socket-based server implementation
 *
 */
public class UDPSocketServerImpl implements UDPServer
{
	/**
	 * The socket that server is using
	 *
	 */
	private final DatagramSocket socket;

	/**
	 * Current server socket address
	 *
	 */
	private final SocketAddress serverAddress;

	/**
	 * A list of request handlers
	 *
	 */
	private RequestHandler handler;

	/**
	 * This thread pool receives requests
	 *
	 */
	private final ForkJoinPool receivingPool;

	/**
	 * This thread pool handles requests
	 *
	 */
	private final ForkJoinPool handlingPool;

	/**
	 * This thread pool responds
	 *
	 */
	private final ExecutorService respondingPool;

	/**
	 * This map contains completed requests
	 *
	 */
	private final ConcurrentMap<SocketAddress, Request> requestMap;


	/**
	 * General UDPSocketServerImpl constructor
	 *
	 * @param bindingAddress server address
	 * @throws ServerInitializationFailedException if server init failed
	 */
	public UDPSocketServerImpl(@NonNull SocketAddress bindingAddress) throws ServerInitializationFailedException
	{
		try
		{
			socket = new DatagramSocket(bindingAddress);
			serverAddress = new InetSocketAddress(socket.getLocalAddress(), socket.getLocalPort());

			socket.setReuseAddress(true);
//			socket.setSoTimeout(1000);

			receivingPool = new ForkJoinPool();
			handlingPool = new ForkJoinPool();
			respondingPool = Executors.newCachedThreadPool();
			requestMap = new ConcurrentHashMap<>();
		}
		catch (IOException e)
		{
			throw new ServerInitializationFailedException("Failed to init server", e);
		}
	}

	/**
	 * This method sets a request handler
	 *
	 * @param handler request handler instance
	 */
	@Override
	public void subscribe(@NonNull RequestHandler handler)
	{
		this.handler = handler;
	}

	/**
	 * This method initiates requests listening
	 *
	 * @throws RequestListeningFailedException if there is no request handler provided
	 */
	@Override
	public void start() throws RequestListeningFailedException
	{
		if (handler == null)
			throw new RequestListeningFailedException("Failed to listen requests: no request handlers provided");

		receivingPool.execute(new RequestReceiveRecursiveAction(socket, requestMap));

		while (true)
		{
			if (requestMap.isEmpty()) continue;

			for (var i = requestMap.values().iterator(); i.hasNext();)
			{
				var req = i.next();
				i.remove();

				var requestHandlingAction = new RequestHandleRecursiveAction(
						socket,
						req,
						handler,
						respondingPool
				);
				handlingPool.execute(requestHandlingAction);
			}
		}
	}

	/**
	 * This method closes existing socket
	 *
	 */
	@Override
	public void close()
	{
		socket.close();
	}
}
