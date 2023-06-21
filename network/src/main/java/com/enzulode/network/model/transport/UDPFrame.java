package com.enzulode.network.model.transport;

import lombok.RequiredArgsConstructor;

import java.io.Serial;

/**
 * This class represents the smallest piece of data sent through the network
 *
 */
@RequiredArgsConstructor
public class UDPFrame implements Frame
{
	/**
	 * UDPFrame serial version uid
	 *
	 */
	@Serial
	private static final long serialVersionUID = 7966568754756710670L;

	/**
	 * This property stores the frame carried data in the byte representation
	 *
	 */
	private final byte[] data;

	/**
	 * This property defines is the frame last frame in the sequence or not
	 *
	 */
	private final boolean last;

	/**
	 * This method retrieves data bytes from the frame
	 *
	 * @return byte array of frame stored data
	 */
	@Override
	public byte[] data()
	{
		return data;
	}

	/**
	 * This method verifies is the frame the last frame in the frame sequence
	 *
	 * @return true if the frame is last and false otherwise
	 */
	@Override
	public boolean last()
	{
		return last;
	}
}
