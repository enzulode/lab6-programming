package com.enzulode.network;

import com.enzulode.network.exception.RequestListeningFailedException;
import com.enzulode.network.handle.RequestHandler;
import lombok.NonNull;


/**
 * This interface declares default behavior for udp servers
 *
 */
public interface UDPServer extends AutoCloseable
{
	/**
	 * Default server port
	 *
	 */
	int DEFAULT_PORT = 8080;

	/**
	 * Server events subscription method
	 *
	 * @param handler request handler instance
	 */
	void subscribe(@NonNull RequestHandler handler);

	/**
	 * This method enables server incoming requests listening
	 *
	 * @throws RequestListeningFailedException if it's failed to start request listening
	 */
	void start() throws RequestListeningFailedException;
}
