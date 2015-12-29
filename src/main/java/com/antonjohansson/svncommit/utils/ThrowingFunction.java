package com.antonjohansson.svncommit.utils;

import java.util.function.Function;

/**
 * Functional interface that mimics Java 8's {@link Function} interface, but
 * it can also throw a specific checked exception.
 *
 * @param <T> The type of the input to the function
 * @param <R> The type of the result of the function
 * @param <E> The type of the checked exception that can be thrown
 *
 * @author Anton Johansson
 */
@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception>
{
    /**
     * Applies this function to the given argument.
     *
     * @param argument The function argument.
     * @return Returns the function result.
     */
	R apply(T argument) throws E;
}
