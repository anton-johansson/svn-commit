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
package com.antonjohansson.svncommit;

import com.antonjohansson.svncommit.core.view.ViewAssertUtils;

import static com.google.common.util.concurrent.Runnables.doNothing;

import org.junit.After;
import org.junit.Before;
import org.testfx.api.FxToolkit;
import org.testfx.toolkit.ApplicationFixture;
import org.testfx.toolkit.impl.ApplicationAdapter;
import org.testfx.toolkit.impl.ApplicationServiceImpl;

import javafx.application.Application;

/**
 * Abstract skeleton for {@link Application} tests.
 *
 * @author Anton Johansson
 */
public abstract class AbstractSvnCommitApplicationTest extends ViewAssertUtils
{
	private final String[] arguments;
    private ApplicationFixture applicationFixture;

    protected AbstractSvnCommitApplicationTest(String... arguments)
	{
		this.arguments = arguments;
		SvnCommitApplication.setExit(doNothing());
	}

	@Before
	public final void internalBefore() throws Exception
	{
		setUp();

		Application application = new SvnCommitApplication();
		new ApplicationServiceImpl().registerApplicationParameters(application, arguments);

		applicationFixture = new ApplicationAdapter(application);

		FxToolkit.registerPrimaryStage();
		FxToolkit.setupApplication(applicationFixture);
	}

	@After
	public final void internalAfter() throws Exception
	{
		FxToolkit.cleanupApplication(applicationFixture);
		tearDown();
	}

	/**
	 * Sets up the unit test.
	 */
	protected void setUp()
	{
	}

	/**
	 * Tears down the unit test.
	 */
	protected void tearDown()
	{
	}
}
