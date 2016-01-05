/**
 * Copyright 2015 Anton Johansson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.antonjohansson.svncommit.core.utils;

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
