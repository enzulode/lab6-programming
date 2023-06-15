package com.enzulode.server.database.properties.util;

import lombok.NonNull;

/**
 * General database properties
 *
 * @param url database url
 * @param username database username
 * @param password database password
 * @param schema database schema
 */
public record DatabaseProperties(
		@NonNull String url,
		@NonNull String username,
		@NonNull String password,
		@NonNull String schema
)
{
}
