/**
 * Copyright (c) Anton Johansson <antoon.johansson@gmail.com>
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
package com.antonjohansson.svncommit.core.view;

import java.lang.reflect.ParameterizedType;

/**
 * Provides utilities for the unit tests.
 *
 * @author Anton Johansson
 */
class Utils
{
	/**
	 * Gets the first generic argument of given class.
	 * 
	 * @param clazz The class to get generic argument from.
	 * @return Returns the first generic argument.
	 */
	static <T> Class<T> getParameterClass(Class<?> clazz)
	{
		return getParameterClass(clazz, 0);
	}
	
	@SuppressWarnings("unchecked")
	private static <T> Class<T> getParameterClass(Class<?> clazz, int ordinal)
	{
		ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
		return (Class<T>) parameterizedType.getActualTypeArguments()[ordinal];
	}
}
