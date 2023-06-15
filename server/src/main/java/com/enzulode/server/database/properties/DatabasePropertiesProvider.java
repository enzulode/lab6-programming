package com.enzulode.server.database.properties;

import com.enzulode.server.database.properties.exception.DatabasePropertyException;
import com.enzulode.server.database.properties.util.DatabaseProperties;

/**
 * This interface declares methods that every property provider should have
 *
 */
public interface DatabasePropertiesProvider
{
	/**
	 * This method provides properties from the preferred source
	 *
	 * @return a database properties instance
	 * @throws DatabasePropertyException if database properties fetching failed
	 */
	DatabaseProperties provide() throws DatabasePropertyException;
}
