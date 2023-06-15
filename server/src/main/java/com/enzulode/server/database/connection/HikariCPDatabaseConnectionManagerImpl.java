package com.enzulode.server.database.connection;

import com.enzulode.server.database.connection.exception.DatabaseConnectionException;
import com.enzulode.server.database.properties.util.DatabaseProperties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NonNull;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Hikari connection pool connection manager implementation
 *
 */
public class HikariCPDatabaseConnectionManagerImpl implements DatabaseConnectionManager
{
	/**
	 * Hikari data source instance
	 *
	 */
	private final HikariDataSource hikariDataSource;

	/**
	 * Hikari database connection manager constructor
	 *
	 * @param properties database properties instance
	 */
	public HikariCPDatabaseConnectionManagerImpl(@NonNull DatabaseProperties properties)
	{
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(properties.url());
		config.setSchema(properties.schema());
		config.setUsername(properties.username());
		config.setPassword(properties.password());

		hikariDataSource = new HikariDataSource(config);
	}

	/**
	 * This method retrieves a connection for the database
	 *
	 * @return connection instance
	 * @throws DatabaseConnectionException if it's failed to retrieve a connection
	 */
	@Override
	public Connection getConnection() throws DatabaseConnectionException
	{
		hikariDataSource.setMaximumPoolSize(10);

		try
		{
			return hikariDataSource.getConnection();
		}
		catch (SQLException e)
		{
			throw new DatabaseConnectionException(
					"Failed to retrieve a database connection from connection pool", e
			);
		}
	}

	/**
	 * This method terminates a connection manager and existing connection pool
	 *
	 */
	@Override
	public void close()
	{
		hikariDataSource.close();
	}
}
