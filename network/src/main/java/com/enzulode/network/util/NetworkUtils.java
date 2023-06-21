package com.enzulode.network.util;

import com.enzulode.network.exception.*;
import com.enzulode.network.model.interconnection.request.Request;
import com.enzulode.network.model.transport.Frame;
import com.enzulode.network.model.transport.UDPFrame;
import lombok.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NetworkUtils
{
	public static final int REQUEST_SIZE = 1024 * 4;
	public static final int REQUEST_BUFFER_SIZE = REQUEST_SIZE * 2;

	public static final int RESPONSE_SIZE = 1024 * 4;
	public static final int RESPONSE_BUFFER_SIZE = RESPONSE_SIZE * 2;

	/**
	 * This method maps the request from UDPFrames to a java instance
	 *
	 * @param frames list of udp frames
	 * @return a request instance
	 * @param <T> request type param
	 * @throws RequestFromFramesProcedureFailedException if something went wrong during byte array output stream
	 * operations or during mapping operations
	 */
	public static <T extends Request> T requestFromFrames(@NonNull List<Frame> frames) throws RequestFromFramesProcedureFailedException
	{
		try(ByteArrayOutputStream baos = new ByteArrayOutputStream();)
		{
//			Byte arrays concatenation
			for (Frame frame : frames)
				baos.writeBytes(frame.data());

			return RequestMapper.mapFromBytesToInstance(baos.toByteArray());
		}
		catch (IOException e)
		{
			throw new RequestFromFramesProcedureFailedException("Failed to close byte array output stream", e);
		}
		catch (MappingException e)
		{
			throw new RequestFromFramesProcedureFailedException("Failed to map request", e);
		}
	}

	/**
	 * This method divides an array of bytes into separate chunks
	 *
	 * @param array byte array to be divided
	 * @param chunk chunk size
	 * @return list of chunks
	 */
	public static List<byte[]> splitIntoChunks(byte[] array, int chunk)
	{
		List<byte[]> chunks = new ArrayList<>();

//		Prevent excessive operations if chunk size is 1
		if (chunk == 1)
		{
			for (byte item : array)
				chunks.add(new byte[] { item });

			return chunks;
		}

//		Array should be simply wrapped with a list if chunk size is equal to array length
		if (chunk == array.length)
			return List.of(array);

//		Pointer initialization
		int pointer = 0;

		while(pointer <= array.length)
		{
			if (pointer == array.length)
				chunks.add(new byte[] {array[array.length - 1]});

			if (pointer < array.length && pointer + chunk > array.length)
				chunks.add(Arrays.copyOfRange(array, pointer, pointer + (array.length - pointer)));
			else
				chunks.add(Arrays.copyOfRange(array, pointer, pointer + chunk));

			pointer += chunk;
		}

		return chunks;
	}

	/**
	 * This method wraps chunks with frames ({@link Frame})
	 *
	 * @param chunks request chunks
	 * @return list of {@link Frame}
	 */
	public static List<Frame> wrapChunksWithUDPFrames(@NonNull List<byte[]> chunks)
	{
//		Getting request chunks from raw bytes
		List<Frame> frames = new ArrayList<>();

//		Wrapping separate chunks into separate frames
		for (int i = 0; i < chunks.size() - 1; i++)
			frames.add(new UDPFrame(chunks.get(i), false));

		frames.add(new UDPFrame(chunks.get(chunks.size() - 1), true));
		return frames;
	}

	/**
	 * This method wraps every frame ({@link Frame}) with {@link DatagramPacket}
	 *
	 * @param frames frames to be wrapped
	 * @param destination packet destination
	 * @return list of {@link DatagramPacket}
	 * @throws FailedToWrapFramesWithDatagramPacketsException if it's failed to map frames to bytes
	 */
	public static List<DatagramPacket> wrapUDPFramesWithDatagramPackets(
			@NonNull List<Frame> frames,
			@NonNull SocketAddress destination
	) throws FailedToWrapFramesWithDatagramPacketsException
	{
		List<DatagramPacket> packets = new ArrayList<>();

		try
		{
			for (Frame frame : frames)
			{
//				Mapping every frame to raw bytes
				byte[] frameBytes = FrameMapper.mapFromInstanceToBytes(frame);
//				Wrapping every frame byte array with datagram packet
				packets.add(new DatagramPacket(frameBytes, frameBytes.length, destination));
			}

			return packets;
		}
		catch (MappingException e)
		{
			throw new FailedToWrapFramesWithDatagramPacketsException(
					"Failed to wrap UDPFrames with datagram packets: mapping exception occurred", e
			);
		}
	}

	/**
	 * This method concatenates two byte arrays
	 *
	 * @param first first array to be concatenated
	 * @param second second array to be concatenated
	 * @return concatenation result
	 */
	public static byte[] concatTwoByteArrays(byte[] first, byte[] second)
	{
		byte[] result = new byte[first.length + second.length];
		System.arraycopy(first, 0, result, 0, first.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

	/**
	 * Remap UDPFrames list to list of byte arrays
	 *
	 * @param frames list of UDPFrames to be unwrapped
	 * @return a list of byte arrays
	 * @throws FramesToBytesFailedException if some of the frames was not mapped to bytes
	 */
	public static List<byte[]> udpFramesToBytes(@NonNull List<Frame> frames) throws FramesToBytesFailedException
	{
		List<byte[]> bytes = new ArrayList<>();

		try
		{
			for (Frame frame : frames)
				bytes.add(FrameMapper.mapFromInstanceToBytes(frame));
		}
		catch (MappingException e)
		{
			throw new FramesToBytesFailedException("Failed to map frame to bytes", e);
		}

		return bytes;
	}

	/**
	 * This method makes a thread sleeping for a specified period of time
	 *
	 * @param timeout timeout in milliseconds
	 */
	public static void timeout(long timeout)
	{
		try
		{
			TimeUnit.MILLISECONDS.sleep(timeout);
		}
		catch (InterruptedException ignored)
		{
		}
	}

	/**
	 * This method wraps overheaded response bytes with datagram packets
	 *
	 * @param responseBytes overheaded response bytes
	 * @param destination response destination
	 * @return list of datagram packets
	 * @throws OverheadedRequestToBytesFailedException if it's failed to wrap response frames with datagram packets
	 */
	public static List<DatagramPacket> getPacketsForOverheadedResponseBytes(
			byte[] responseBytes,
			@NonNull SocketAddress destination
	) throws OverheadedRequestToBytesFailedException
	{
		try
		{
//			Get response chunks from rew response bytes
			List<byte[]> responseChunks = NetworkUtils.splitIntoChunks(responseBytes, NetworkUtils.RESPONSE_BUFFER_SIZE);

	//		Wrap chunks with UDPFrames
			List<Frame> udpFrames = NetworkUtils.wrapChunksWithUDPFrames(responseChunks);

			return NetworkUtils.wrapUDPFramesWithDatagramPackets(udpFrames, destination);
		} catch (FailedToWrapFramesWithDatagramPacketsException e)
		{
			throw new OverheadedRequestToBytesFailedException("Failed to process overheaded response packets from bytes", e);
		}
	}


}
