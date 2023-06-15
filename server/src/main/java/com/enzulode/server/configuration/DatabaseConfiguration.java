package com.enzulode.server.configuration;

import com.enzulode.common.dao.TicketDao;
import com.enzulode.models.Ticket;
import com.enzulode.server.dao.TicketDAOImpl;
import com.enzulode.server.database.TicketDatabaseImpl;
import com.enzulode.server.database.TicketDatabase;
import com.enzulode.server.database.connection.DatabaseConnectionManager;
import com.enzulode.server.database.connection.HikariCPDatabaseConnectionManagerImpl;
import com.enzulode.server.database.exception.DatabaseException;
import com.enzulode.server.database.loading.LoadingService;
import com.enzulode.server.database.loading.TicketDatabaseLoadingService;
import com.enzulode.server.database.properties.FileDatabasePropertyProviderImpl;
import com.enzulode.server.database.properties.exception.DatabasePropertyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Database layer configuration
 *
 */
@Configuration
@Slf4j
public class DatabaseConfiguration
{
	/**
	 * Database connection manager bean declaration
	 *
	 * @return database connection manager instance
	 */
	@Bean(name = "databaseConnectionManager")
	public DatabaseConnectionManager databaseConnectionManagerBean()
	{
		try
		{
			var dpp = new FileDatabasePropertyProviderImpl("local.properties");
			var props = dpp.provide();

			return new HikariCPDatabaseConnectionManagerImpl(props);
		}
		catch (DatabasePropertyException e)
		{
			throw new BeanCreationException("databaseConnectionManager", "Failed to load database props", e);
		}
	}

	/**
	 * Database loading service bean declaration
	 *
	 * @param connectionManager database connection manager instance
	 * @return database loading service instance
	 */
	@Bean(name = "loadingService")
	public LoadingService<Ticket> loadingServiceBean(DatabaseConnectionManager connectionManager)
	{
		return new TicketDatabaseLoadingService(connectionManager);
	}

	/**
	 * Ticket database bean declaration
	 *
	 * @param connectionManager database connection manager instance
	 * @param loadingService database loading service instance
	 * @return ticket database instance
	 */
	@Bean(name = "ticketDatabase")
	public TicketDatabase ticketDatabaseBean(
			DatabaseConnectionManager connectionManager,
			LoadingService<Ticket> loadingService
	)
	{
		try
		{
			return new TicketDatabaseImpl(connectionManager, loadingService);
		}
		catch (DatabaseException e)
		{
			throw new BeanCreationException("normalTicketDatabase", "Failed to create bean", e);
		}
	}

	/**
	 * Ticket dao bean declaration
	 *
	 * @param database ticket database instance
	 * @return ticket dao instance
	 */
	@Bean(name = "ticketDao")
	public TicketDao ticketDaoBean(TicketDatabase database)
	{
		return new TicketDAOImpl(database);
	}
}
