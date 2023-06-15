package com.enzulode.server.database.properties;

import com.enzulode.server.database.properties.exception.DatabasePropertyException;
import com.enzulode.server.database.properties.util.DatabaseProperties;
import lombok.NonNull;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * This class provides database properties via file
 *
 */
public class FileDatabasePropertyProviderImpl implements DatabasePropertiesProvider
{
	/**
	 * Database property source
	 *
	 */
	private final File propertiesFile;

	/**
	 * File database property provider
	 *
	 * @param filename properties file filename
	 */
	public FileDatabasePropertyProviderImpl(@NonNull String filename)
	{
		this.propertiesFile = new File(filename);
	}

	/**
	 * File database property provider constructor with exact file
	 *
	 * @param file database property file provider
	 */
	public FileDatabasePropertyProviderImpl(@NonNull File file)
	{
		this.propertiesFile = file;
	}

	/**
	 * This method retrieves database properties from file and wraps them into DatabaseProperties instance
	 *
	 * @return database properties instance
	 * @throws DatabasePropertyException if database property fetching failed
	 */
	@Override
	public DatabaseProperties provide() throws DatabasePropertyException
	{
		try
		{
			Properties properties = new Properties();
			properties.load(new FileReader(propertiesFile));

			return new DatabaseProperties(
					properties.getProperty("db.url"),
					properties.getProperty("db.username"),
					properties.getProperty("db.password"),
					properties.getProperty("db.schema")
			);
		}
		catch (IOException e)
		{
			throw new DatabasePropertyException("Failed to load properties from the specified file", e);
		}
	}
}
