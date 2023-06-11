package com.enzulode.client.parser.cli;

import com.enzulode.client.parser.exception.UserInterruptionException;
import com.enzulode.client.util.Printer;
import com.enzulode.common.parser.ParserMode;
import lombok.NonNull;
import lombok.Setter;

import java.util.Scanner;

/**
 * Parser abstraction
 *
 * @param <T> type that parser is going to parse
 */
public abstract class CLIParser<T>
{
	/**
     * Printer instance
     *
     */
	protected final Printer printer;

	/**
	 * Scanner instance
	 *
	 */
	protected final Scanner scanner;

	/**
	 * Parser current resolving mode
	 *
	 */
	@Setter
	protected ParserMode mode;

	/**
	 * CLIParser constructor
	 *
	 * @param printer printer instance
	 * @param scanner scanner instance
	 */
	public CLIParser(@NonNull Printer printer, @NonNull Scanner scanner)
	{
		this.printer = printer;
		this.scanner = scanner;
	}

	/**
	 * This method parses an object from the cli
	 *
	 * @return parsed object
	 */
	public abstract T parse() throws UserInterruptionException;

	/**
     * Offers an ability to interrupt data input
     *
     * @throws UserInterruptionException if got not y/Y from user
     */
    protected void proposeContinue() throws UserInterruptionException
    {
        printer.print("Do you want to continue? [y/Y - for yes, other - for no]: ");
        String userInput = scanner.nextLine().trim();
        if (!"Y".equalsIgnoreCase(userInput))
            throw new UserInterruptionException("Data input successfully interrupted");
    }
}
