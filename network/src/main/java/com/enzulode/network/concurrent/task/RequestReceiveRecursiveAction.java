package com.enzulode.network.concurrent.task;

import com.enzulode.network.concurrent.structures.ConcurrentFrameReceivingMap;
import com.enzulode.network.concurrent.structures.Pair;
import com.enzulode.network.exception.NetworkException;
import com.enzulode.network.model.interconnection.request.Request;
import com.enzulode.network.model.transport.Frame;
import com.enzulode.network.util.FrameMapper;
import com.enzulode.network.util.NetworkUtils;
import lombok.NonNull;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This action collects all requests from the clients
 *
 */
public class RequestReceiveRecursiveAction extends RecursiveAction
{

	/**
	 * Lock instance
	 *
	 */
	private final Lock lock;

	/**
	 * Datagram socket instance
	 *
	 */
	private final DatagramSocket socket;

	/**
	 * Concurrent frame receiving map instance
	 *
	 */
	private final ConcurrentFrameReceivingMap map;

	/**
	 * Request-storing concurrent map instance
	 *
	 */
	private final ConcurrentMap<SocketAddress, Request> requestMap;

	/**
	 * Request receiving action constructor
	 *
	 * @param socket datagram socket instance
	 * @param requestMap request map instance
	 */
	public RequestReceiveRecursiveAction(
			@NonNull DatagramSocket socket,
			@NonNull ConcurrentMap<SocketAddress, Request> requestMap
	)
	{
		super();

		this.socket = socket;
		this.requestMap = requestMap;
		lock = new ReentrantLock();
		map = new ConcurrentFrameReceivingMap();
	}

	/**
	 * The main computation performed by this task.
	 */
	@Override
	protected void compute()
	{

//		Declaring incoming request bytes buffer
		byte[] incomingFrameBytes = new byte[NetworkUtils.REQUEST_BUFFER_SIZE];
		DatagramPacket incomingRequestPacket = new DatagramPacket(incomingFrameBytes, incomingFrameBytes.length);

		while (true)
		{
			try
			{
				if (socket.isClosed())
					break;

				if (!lock.tryLock())
					continue;

				try
				{
					socket.receive(incomingRequestPacket);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					lock.unlock();
					continue;
				}
				finally
				{
					lock.unlock();
				}

	//			Mapping a current frame to instance from bytes
				Frame currentFrame = FrameMapper.mapFromBytesToInstance(incomingRequestPacket.getData());

	//			Adding a frame into the frames map
				map.add(incomingRequestPacket.getSocketAddress(), currentFrame);

				for (Pair<SocketAddress, List<Frame>> completedRequestFrameList : map.findCompletedRequestsFrameLists())
				{
					Request request = NetworkUtils.requestFromFrames(completedRequestFrameList.value());

	//				Put complete request into the completed requests map
					requestMap.put(completedRequestFrameList.key(), request);
				}
			}
			catch (NetworkException e)
			{
				e.printStackTrace();
			}
		}

	}
}
