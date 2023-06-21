package com.enzulode.network.model.transport;

import java.io.Serializable;

/**
 * This interface declares a default behaviour for frame inside the interconnection strategy
 *
 */
public interface Frame extends Serializable
{
	/**
	 * This method retrieves data bytes from the frame
	 *
	 * @return byte array of frame stored data
	 */
	byte[] data();

	/**
	 * This method verifies is the frame the last frame in the frame sequence
	 *
	 * @return true if the frame is last and false otherwise
	 */
	boolean last();
}
