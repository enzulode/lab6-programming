package com.enzulode.server.util;

import java.io.File;

public class EnvUtil
{
	public static File retrieveFileFromEnv(String envVariable)
	{
		String fileName = System.getenv(envVariable.trim());
		return new File(fileName);
	}
}
