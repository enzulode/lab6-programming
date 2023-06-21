package com.enzulode.network.util;

import com.enzulode.network.exception.MappingException;
import com.enzulode.network.model.interconnection.response.Response;
import lombok.NonNull;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;

/**
 * This class converts element response instance into a byte array and in an opposite way
 *
 */
public final class ResponseMapper
{
    /**
	 * This method maps response instance into raw response bytes
	 *
	 * @param response response instance
	 * @return response raw bytes
	 * @throws MappingException if serialization not succeed
	 */
	public static byte[] mapFromInstanceToBytes(@NonNull Response response) throws MappingException
	{
		try
		{
			return SerializationUtils.serialize(response);
		}
		catch (SerializationException e)
		{
			throw new MappingException("Failed to map Response instance to bytes", e);
		}
	}
	/**
	 * This method maps raw response bytes into response instance
	 *
	 * @param bytes raw response bytes
	 * @param <T> response type param
	 * @return response instance
	 * @throws MappingException if deserialization not succeed
	 */
	public static <T extends Response> T mapFromBytesToInstance(byte[] bytes) throws MappingException
	{
		try
		{
			return SerializationUtils.deserialize(bytes);
		}
		catch (SerializationException e)
		{
			throw new MappingException("Failed to map Response bytes to instance", e);
		}
	}
}
