package com.enzulode.server.database;

import com.enzulode.server.database.connection.DatabaseConnectionManager;
import com.enzulode.server.database.exception.DatabaseException;
import com.enzulode.server.database.loading.LoadingService;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDatabase<T> implements SimpleDatabase<T>
{
	protected final DatabaseConnectionManager connectionManager;
	protected final LoadingService<T> loadingService;

	@Getter
	protected final List<T> elements;

	@Setter
	protected LocalDateTime creationDate;

	public AbstractDatabase(
			@NonNull DatabaseConnectionManager connectionManager,
			@NonNull LoadingService<T> loadingService
	)
	{
		this.connectionManager = connectionManager;
		this.loadingService = loadingService;
		elements = new ArrayList<>();
		creationDate = LocalDateTime.now();
	}

	public abstract void initDDLIfNotExists() throws DatabaseException;
	public abstract int size() throws DatabaseException;
	public abstract LocalDateTime getCreationDate() throws DatabaseException;
	public abstract void clear() throws DatabaseException;
	public abstract void load() throws DatabaseException;
}
