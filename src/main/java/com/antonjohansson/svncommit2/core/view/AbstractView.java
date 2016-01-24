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
package com.antonjohansson.svncommit2.core.view;

import javafx.scene.Parent;

/**
 * Abstract skeleton for view implementations.
 *
 * @author Anton Johansson
 */
public abstract class AbstractView implements View
{
	private Parent parent;

	/** {@inheritDoc} */
	@Override
	public final Parent getParent()
	{
		return parent;
	}

	/** {@inheritDoc} */
	@Override
	public final void setParent(Parent parent)
	{
		this.parent = parent;
	}
}