package com.enzulode.server.cli.factory;

/**
 * This interface declares specific methods for server cli thread factories
 *
 */
public interface ServerCLIThreadFactory
{
	/**
	 * This method initiates new server cli thread
	 *
	 * @return new server cli thread instance
	 */
	Thread newServerCLIThread();
}
