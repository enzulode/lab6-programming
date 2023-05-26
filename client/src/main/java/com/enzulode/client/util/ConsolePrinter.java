package com.enzulode.client.util;

import java.util.List;

/**
 * Application console printer implementation
 *
 */
public class ConsolePrinter implements Printer
{
	/**
	 * A method that prints any object as a string if it's possible in the current line
	 *
	 * @param printable object to be printed
	 */
	@Override
	public void print(Object printable)
	{
		System.out.print(printable.toString());
	}

	/**
	 * A method that prints any object as a string if it's possible in the next line
	 *
	 * @param printable object to be printed
	 */
	@Override
	public void println(Object printable)
	{
		System.out.println(printable.toString());
	}

	/**
	 * A method that prints any a formatted string
	 *
	 * @param printable parametrized string to be printed
	 * @param args      string parameters
	 */
	@Override
	public void printf(String printable, Object... args)
	{
		System.out.printf(printable, args);
	}

	/**
	 * A method that prints a list of elements using toString() method for each element
	 *
	 * @param printable a list of printable elements
	 */
	@Override
	public void printList(List<?> printable)
	{
		if (printable.size() == 0)
		{
			println("Empty list");
			return;
		}

		printable.forEach(this::println);
	}
}
