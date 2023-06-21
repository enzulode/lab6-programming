package com.enzulode.network.util;

import com.enzulode.network.exception.MappingException;
import com.enzulode.network.model.transport.Frame;
import lombok.NonNull;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

/**
 * This class converts UDPFrame instance into a byte array and in an opposite way
 *
 */
public final class FrameMapper
{
	/**
	 * This method maps {@link Frame} instance into raw response bytes
	 *
	 * @param udpFrame {@link Frame} instance
	 * @return frame raw bytes
	 * @throws MappingException if serialization not succeed
	 */
	public static byte[] mapFromInstanceToBytes(@NonNull Frame udpFrame) throws MappingException
	{
		try
		{
			return SerializationUtils.serialize(udpFrame);
		}
		catch (SerializationException e)
		{
			throw new MappingException("Failed to map UDPFrame to bytes", e);
		}
	}

	/**
	 *
	 *
	 * @param udpFrameBytes raw {@link Frame} bytes
	 * @return {@link Frame} instance
	 * @throws MappingException if deserialization not succeed
	 */
	public static <T extends Frame> T mapFromBytesToInstance(byte[] udpFrameBytes) throws MappingException
	{
		try
		{
			return SerializationUtils.deserialize(udpFrameBytes);
		}
		catch (SerializationException e)
		{
			throw new MappingException("Failed to map UDPFrame bytes to instance", e);
		}
	}
}
