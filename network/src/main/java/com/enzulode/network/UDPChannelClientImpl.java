package com.enzulode.network;

import com.enzulode.network.exception.*;
import com.enzulode.network.model.interconnection.request.Request;
import com.enzulode.network.model.interconnection.request.impl.PingRequest;
import com.enzulode.network.model.interconnection.response.Response;
import com.enzulode.network.model.interconnection.response.impl.PongResponse;
import com.enzulode.network.model.transport.Frame;
import com.enzulode.network.model.transport.UDPFrame;
import com.enzulode.network.util.FrameMapper;
import com.enzulode.network.util.NetworkUtils;
import com.enzulode.network.util.RequestMapper;
import com.enzulode.network.util.ResponseMapper;
import lombok.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.List;

public class UDPChannelClientImpl implements UDPClient
{

	/**
	 * Local address instance
	 *
	 */
	private final SocketAddress localAddress;

	/**
	 * Server address instance
	 *
	 */
	private final SocketAddress serverAddress;

	/**
	 * Datagram channel instance
	 *
	 */
	private final DatagramChannel channel;

	/**
	 * UDPChannel client constructor with default params
	 *
	 * @throws ClientInitFailedException if it's failed to open a channel
	 */
	public UDPChannelClientImpl() throws ClientInitFailedException
	{
		this(0, "127.0.0.1", 8080);
	}

	/**
	 * UDPSocket client constructor.
	 *
	 * @param localPort the port, UDPSocket will be bind to (0 - any available port automatically / provide your own port)
	 * @param serverHost the remote server host
	 * @param serverPort the remote server port
	 * @throws ClientInitFailedException if it's failed to open a datagram channel
	 */
	public UDPChannelClientImpl(
			int localPort,
			@NonNull String serverHost,
			int serverPort
	) throws ClientInitFailedException
	{
		try
		{
			this.channel = DatagramChannel.open();

			if (localPort == 0)
			{
				this.channel.bind(new InetSocketAddress("127.0.0.1", localPort));
				this.localAddress = new InetSocketAddress("127.0.0.1", this.channel.socket().getLocalPort());
			}
			else
			{
				this.localAddress = new InetSocketAddress("127.0.0.1", localPort);
				this.channel.bind(localAddress);
			}

			this.serverAddress = new InetSocketAddress(serverHost, serverPort);

//			Configure channel
			this.channel.configureBlocking(false);
			this.channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
			this.channel.setOption(StandardSocketOptions.SO_REUSEPORT, true);
		}
		catch (IOException e)
		{
			throw new ClientInitFailedException("Failed to open datagram channel", e);
		}
	}

	/**
	 * This method allows you to send a request and receive a response for it
	 *
	 * @param <T> means the expected type of response
	 * @param request request to be sent
	 * @return a response instance
	 * @throws RequestFailedException if it failed to send the response to the server,
	 * if the server response data was corrupted, if it failed to receive response from the server or
	 * request mapping failed
	 * @throws ServerNotAvailableException if server is not currently available
	 */
	public <T extends Response> T sendRequestAndWaitResponse(@NonNull Request request) throws RequestFailedException, ServerNotAvailableException
	{
//		Readjusting request addresses
		request.setFrom(localAddress);
		request.setTo(serverAddress);

		try
		{
//			Map request instance to bytes array
			byte[] requestBytes = RequestMapper.mapFromInstanceToBytes(request);

//			If request size is more than default buffer size - send with overhead : else - send without overhead
			if (requestBytes.length > NetworkUtils.REQUEST_SIZE)
				sendRequestWithOverhead(requestBytes);
			else
				sendRequestNoOverhead(requestBytes);

			return waitForResponse();
		}
		catch (FailedToWaitResponseCorrectlyException | MappingException e)
		{
			throw new RequestFailedException("Failed to send request and receive a response", e);
		}
	}

	/**
	 * This method sends request with overhead after separation
	 *
	 * @param requestBytes raw request bytes
	 * @throws FailedRequestWithOverheadException if it's failed to send a frame
	 * @throws ServerNotAvailableException if server timeout exception was caught
	 */
	private void sendRequestWithOverhead(byte[] requestBytes) throws FailedRequestWithOverheadException, ServerNotAvailableException
	{
//		Get response chunks from rew response bytes
		List<byte[]> requestChunks = NetworkUtils.splitIntoChunks(requestBytes, NetworkUtils.RESPONSE_BUFFER_SIZE);

//		Wrap chunks with UDPFrames
		List<Frame> udpFrames = NetworkUtils.wrapChunksWithUDPFrames(requestChunks);

//		Sending all request frames to the server
		try
		{
//			Map UDOFrames to bytes
			List<byte[]> framesBytes = NetworkUtils.udpFramesToBytes(udpFrames);

			long idx = 0;
			for (byte[] frameBytes : framesBytes)
			{
				checkServerConnection();
				channel.send(ByteBuffer.wrap(frameBytes), serverAddress);
			}
		}
		catch (SocketTimeoutException e)
		{
			throw new ServerNotAvailableException("Server is not currently available", e);
		}
		catch (IOException | FramesToBytesFailedException e)
		{
			throw new FailedRequestWithOverheadException("Failed to send response with an overhead", e);
		}
	}

	/**
	 * This method sends request without overhead
	 *
	 * @param requestBytes raw request bytes
	 * @throws FailedRequestNoOverheadException if it's failed to send request
	 * @throws ServerNotAvailableException if server timeout exception was caught
	 */
	private void sendRequestNoOverhead(byte[] requestBytes) throws FailedRequestNoOverheadException, ServerNotAvailableException
	{
		try
		{
//			Wrap raw bytes with UDPFrame
			Frame udpFrame = new UDPFrame(requestBytes, true);

//			Get UDPFrameBytes from UDPFrame instance
			byte[] udpFrameBytes = FrameMapper.mapFromInstanceToBytes(udpFrame);

//			Trying to send the request
			checkServerConnection();
			channel.send(ByteBuffer.wrap(udpFrameBytes), serverAddress);
		}
		catch (SocketTimeoutException e)
		{
			throw new ServerNotAvailableException("Server is not currently available", e);
		}
		catch (IOException | MappingException e)
		{
			throw new FailedRequestNoOverheadException("Failed to map UDPFrame to raw bytes", e);
		}
	}

	/**
	 * Method checks the server availability
	 *
	 * @throws ServerNotAvailableException if server is not currently available
	 */
	private void checkServerConnection() throws ServerNotAvailableException
	{
		try
		{
//			Creating PING request
			Request request = new PingRequest(localAddress, serverAddress);

//			Mapping PING request into bytes
			byte[] pingRequestBytes = RequestMapper.mapFromInstanceToBytes(request);

//			Wrapping request bytes with udp frame
			Frame frame = new UDPFrame(pingRequestBytes, true);

//			Mapping pingFrame into bytes
			byte[] pingFrameBytes = FrameMapper.mapFromInstanceToBytes(frame);

//			Sending ping request
			channel.send(ByteBuffer.wrap(pingFrameBytes), serverAddress);
			ByteBuffer pingResponseBuffer = ByteBuffer.allocate(NetworkUtils.RESPONSE_BUFFER_SIZE * 2);

			long startTime = System.currentTimeMillis();
			int timeout = 5000;
			while (true)
			{
				SocketAddress addr = channel.receive(pingResponseBuffer);

				if (System.currentTimeMillis() > startTime + timeout)
					throw new ServerNotAvailableException("Server is not available");

				if (addr == null) continue;

				byte[] currentFrameBytes = new byte[pingResponseBuffer.position()];
				pingResponseBuffer.rewind();
				pingResponseBuffer.get(currentFrameBytes);
				UDPFrame responseFrame = FrameMapper.mapFromBytesToInstance(currentFrameBytes);
				Response response = ResponseMapper.mapFromBytesToInstance(responseFrame.data());
				if (!(response instanceof PongResponse)) throw new ServerNotAvailableException("Server is not available");
				break;
			}
		} catch (IOException | MappingException e)
		{
			throw new ServerNotAvailableException("Failed to send ping request", e);
		}

	}

	/**
	 * Method waits for response
	 *
	 * @param <T> response type param
	 * @return response instance
	 * @throws FailedToWaitResponseCorrectlyException if it's failed to receive response from the server
	 * @throws ServerNotAvailableException if server is not currently available
	 */
	private <T extends Response> T waitForResponse() throws FailedToWaitResponseCorrectlyException, ServerNotAvailableException
	{
		ByteBuffer responseBuffer = ByteBuffer.allocate(NetworkUtils.RESPONSE_BUFFER_SIZE * 2);

		try(ByteArrayOutputStream baos = new ByteArrayOutputStream())
		{
			boolean gotAll = false;

			do
			{
//				Receiving incoming byte buffer
				responseBuffer.clear();

				SocketAddress addr = channel.receive(responseBuffer);

//				Skip current iteration if nothing was got in receive
				if (addr == null) continue;

//				Retrieving current frame bytes from incoming byte buffer
				byte[] currentFrameBytes = new byte[responseBuffer.position()];
				responseBuffer.rewind();
				responseBuffer.get(currentFrameBytes);

//				Mapping UDPFrame from raw bytes
				UDPFrame currentFrame = FrameMapper.mapFromBytesToInstance(currentFrameBytes);

//				Enriching response bytes with new bytes
				baos.writeBytes(currentFrame.data());

//				Change gotAll state if got the last UDPFrame
				if (currentFrame.last()) gotAll = true;

			} while (!gotAll);

//			Mapping request instance from raw request bytes
			byte[] responseBytes = baos.toByteArray();
			return ResponseMapper.mapFromBytesToInstance(responseBytes);
		}
		catch (IOException | MappingException e)
		{
			throw new FailedToWaitResponseCorrectlyException("Failed to receive response", e);
		}
	}

	/**
	 * Method forced by {@link AutoCloseable} interface.
	 * Allows to use this class in the try-with-resources construction
	 * Automatically closes datagram channel
	 *
	 * @throws FailedToCloseClientException if close failed
	 */
	@Override
	public void close() throws FailedToCloseClientException
	{
		try
		{
			channel.close();
		}
		catch (IOException e)
		{
			throw new FailedToCloseClientException("Failed to close datagram channel", e);
		}
	}

}
