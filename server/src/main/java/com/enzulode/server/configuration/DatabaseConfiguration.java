package com.enzulode.server.configuration;

import com.enzulode.common.filesystem.FileManipulationService;
import com.enzulode.database.DatabaseConnectionPool;
import com.enzulode.database.exception.DatabaseConnectionPoolException;
import com.enzulode.database.util.DatabaseConfig;
import com.enzulode.server.dao.TicketDaoImpl;
import com.enzulode.server.database.TicketDatabaseImpl;
import com.enzulode.server.database.TicketDatabaseSavingService;
import com.enzulode.server.util.EnvUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Database layer configuration
 *
 */
@Configuration
@Slf4j
public class DatabaseConfiguration
{

	/**
	 * Database configuration bean
	 *
	 * @param host database host
	 * @param port database port
	 * @param database database name
	 * @param schema database schema
	 * @param autocommit database autocommit status
	 * @return database config instance
	 */
	@Bean(name = "databaseConfig")
	public DatabaseConfig databaseConfigBean(
			@Value("${settings.db.host}") String host,
			@Value("${settings.db.port}") int port,
			@Value("${settings.db.database}") String database,
			@Value("${settings.db.schema}") String schema,
			@Value("${settings.db.autocommit}") boolean autocommit
	)
	{
		try
		{
			Properties properties = new Properties();
			properties.load(new BufferedReader(new FileReader("local.properties")));
			String username = properties.getProperty("db.username");
			String password = properties.getProperty("db.password");

			return new DatabaseConfig(host, port, username, password, database, schema, autocommit);
		}
		catch (IOException e)
		{
			log.error("Failed to load properties from the local.properties file", e);
			throw new BeanCreationException(
					"databaseConfigBean",
					"Failed to load properties from the local.properties file",
					e
			);
		}
	}

	/**
	 * Database connection pool bean
	 *
	 * @param config database configuration
	 * @return database connection pool instance
	 */
	@Bean(name = "databaseConnectionPool")
	public DatabaseConnectionPool databaseConnectionPoolBean(DatabaseConfig config)
	{
		try
		{
			return new DatabaseConnectionPool(config);
		}
		catch (DatabaseConnectionPoolException e)
		{
			log.error("Failed to init a connection pool");
			throw new BeanCreationException("databaseConnectionPool", "Failed to init a connection pool", e);
		}
	}

	@Bean(name = "ticketDatabaseSavingService")
	public TicketDatabaseSavingService ticketDatabaseSavingServiceBean(
			FileManipulationService fms,
			Marshaller xmlMarshaller,
			Unmarshaller xmlUnmarshaller
	)
	{
		return new TicketDatabaseSavingService(fms, xmlMarshaller, xmlUnmarshaller);
	}

	@Bean(name = "ticketDatabase")
	public TicketDatabaseImpl ticketDatabaseBean(
			TicketDatabaseSavingService savingService,
			FileManipulationService fms,
			@Value("${settings.variable.name}") String envVariableName
	)
	{
		File file = EnvUtil.retrieveFileFromEnv(envVariableName);
		return new TicketDatabaseImpl(savingService, fms, file);
	}

	@Bean(name = "ticketDaoImpl")
	public TicketDaoImpl ticketDaoImplBean(TicketDatabaseImpl database)
	{
		return new TicketDaoImpl(database);
	}
}
