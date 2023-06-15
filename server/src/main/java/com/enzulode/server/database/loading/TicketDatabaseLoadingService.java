package com.enzulode.server.database.loading;

import com.enzulode.models.Ticket;
import com.enzulode.server.database.AbstractDatabase;
import com.enzulode.server.database.connection.DatabaseConnectionManager;
import com.enzulode.server.database.exception.DatabaseException;
import com.enzulode.server.database.loading.exception.LoadingException;
import com.enzulode.server.database.util.DatabaseUtils;
import lombok.NonNull;

import java.sql.SQLException;

/**
 * This class saves and loads the database
 *
 */
public class TicketDatabaseLoadingService implements LoadingService<Ticket>
{
	/**
	 * Database connection manager instance
	 *
	 */
	private final DatabaseConnectionManager connectionManager;

	/**
	 * Ticket database loading service constructor
	 *
	 * @param connectionManager database connection manager instance
	 */
	public TicketDatabaseLoadingService(@NonNull DatabaseConnectionManager connectionManager)
	{
		this.connectionManager = connectionManager;
	}

	/**
	 * This method loads the database from a specific source
	 *
	 * @param database the database instance
	 * @throws LoadingException if something went wrong during loading
	 */
	@Override
	public void load(AbstractDatabase<Ticket> database) throws LoadingException
	{
		try(var conn = connectionManager.getConnection())
		{
			var databaseElements = database.getElements();
			var res = DatabaseUtils.selectAllTickets(conn);

			databaseElements.clear();
			databaseElements.addAll(res);
		}
		catch (DatabaseException e)
		{
			throw new LoadingException("Failed to retrieve tickets", e);
		}
		catch (SQLException e)
		{
			throw new LoadingException("Failed to close a connection", e);
		}
	}
}
