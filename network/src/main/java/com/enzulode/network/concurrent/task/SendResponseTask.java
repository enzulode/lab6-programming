package com.enzulode.network.concurrent.task;

import com.enzulode.network.exception.FailedResponseWithNoOverheadException;
import com.enzulode.network.exception.FailedResponseWithOverheadException;
import com.enzulode.network.exception.MappingException;
import com.enzulode.network.exception.NetworkException;
import com.enzulode.network.model.interconnection.response.Response;
import com.enzulode.network.model.transport.Frame;
import com.enzulode.network.model.transport.UDPFrame;
import com.enzulode.network.util.FrameMapper;
import com.enzulode.network.util.NetworkUtils;
import com.enzulode.network.util.ResponseMapper;
import lombok.NonNull;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This task sends a response
 *
 */
public class SendResponseTask implements Runnable
{
	/**
	 * DatagramSocket instance
	 *
	 */
	private final DatagramSocket socket;

	/**
	 * Response to be sent
	 *
	 */
	private final Response response;

	/**
	 * Lock instance
	 *
	 */
	private final Lock lock;

	/**
	 * SendResponseTask constructor
	 *
	 * @param socket datagram socket instance
	 * @param response response instance
	 */
	public SendResponseTask(@NonNull DatagramSocket socket, @NonNull Response response)
	{
		this.socket = socket;
		this.response = response;
		lock = new ReentrantLock();
	}

	/**
	 * Main response sending logic
	 *
	 */
	@Override
	public void run()
	{
		System.out.println("10");
		try
		{
			byte[] responseBytes = ResponseMapper.mapFromInstanceToBytes(response);

			System.out.println("11");
			if (responseBytes.length > NetworkUtils.RESPONSE_SIZE)
				sendResponseWithOverhead(responseBytes, response.getTo());
			else
				sendResponseNoOverhead(responseBytes, response.getTo());

			System.out.println("12");
		}
		catch (NetworkException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Method for sending response without an overhead
	 *
	 * @param responseBytes response bytes array
	 * @param destination response destination
	 * @throws FailedResponseWithNoOverheadException if it's failed to send the response
	 */
	private void sendResponseNoOverhead(byte[] responseBytes, @NonNull SocketAddress destination) throws FailedResponseWithNoOverheadException
	{
//		Wrap raw response bytes with UDPFrame
		Frame udpFrame = new UDPFrame(responseBytes, true);

		try
		{
//			Map UDPFrame to bytes
			byte[] udpFrameBytes = FrameMapper.mapFromInstanceToBytes(udpFrame);

//			Wrap UDPFrame bytes with DatagramPacket
			DatagramPacket responsePacket = new DatagramPacket(udpFrameBytes, udpFrameBytes.length, destination);

//			Sending response to the client
			lock.lock();
			socket.send(responsePacket);
			lock.unlock();
		}
		catch (MappingException e)
		{
			throw new FailedResponseWithNoOverheadException("Failed to map frame to bytes", e);
		}
		catch (IOException e)
		{
			throw new FailedResponseWithNoOverheadException("Failed to send response to the client", e);
		}
	}

	/**
	 * Method for sending the response with an overhead
	 *
	 * @param responseBytes response byte array
	 * @param destination response destination
	 * @throws FailedResponseWithOverheadException if it's failed to send the response
	 */
	private void sendResponseWithOverhead(byte[] responseBytes, @NonNull SocketAddress destination) throws FailedResponseWithOverheadException
	{
		try
		{
			List<DatagramPacket> responsePackets = NetworkUtils.getPacketsForOverheadedResponseBytes(responseBytes, destination);

			for (DatagramPacket packet : responsePackets)
			{
				NetworkUtils.timeout(10);
				lock.lock();
				socket.send(packet);
				lock.unlock();
			}
		}
		catch (NetworkException | IOException e)
		{
			throw new FailedResponseWithOverheadException("Failed to send the overheaded response", e);
		}
	}
}
