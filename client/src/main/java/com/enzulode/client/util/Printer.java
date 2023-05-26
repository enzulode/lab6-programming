package com.enzulode.client.util;

import java.util.List;

/**
 * Application printer
 *
 */
public interface Printer
{
    /**
     * A method that prints any object as a string if it's possible in the current line
     *
     * @param printable object to be printed
     */
    void print(Object printable);

    /**
     * A method that prints any object as a string if it's possible in the next line
     *
     * @param printable object to be printed
     */
    void println(Object printable);

    /**
     * A method that prints any a formatted string
     *
     * @param printable parametrized string to be printed
     * @param args string parameters
     */
    void printf(String printable, Object... args);

    /**
     * A method that prints a list of elements using toString() method for each element
     *
     * @param printable a list of printable elements
     */
    void printList(List<?> printable);
}
