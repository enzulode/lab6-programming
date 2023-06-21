package com.enzulode.network.util;

import com.enzulode.network.exception.MappingException;
import com.enzulode.network.model.interconnection.request.Request;
import lombok.NonNull;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

/**
 * This class converts element request instance into a byte array and in an opposite way
 *
 */
public final class RequestMapper
{
    /**
	 * This method maps request instance into raw request bytes
	 *
	 * @param request request instance
	 * @return request raw bytes
	 * @throws MappingException if serialization not succeed
	 */
	public static byte[] mapFromInstanceToBytes(@NonNull Request request) throws MappingException
	{
		try
		{
			return SerializationUtils.serialize(request);
		}
		catch (SerializationException e)
		{
			throw new MappingException("Failed to map Request instance to bytes", e);
		}
	}

	/**
	 * This method maps raw request bytes into request instance
	 *
	 * @param bytes raw request bytes
	 * @param <T> request type param
	 * @return request instance
	 * @throws MappingException if deserialization not succeed
	 */
	public static <T extends Request> T mapFromBytesToInstance(byte[] bytes) throws MappingException
	{
		try
		{
			return SerializationUtils.deserialize(bytes);
		}
		catch (SerializationException e)
		{
			throw new MappingException("Failed to map Request bytes to instance", e);
		}
	}
}