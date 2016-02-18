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

import com.antonjohansson.svncommit.core.concurrent.Worker;
import com.antonjohansson.svncommit.core.config.Configuration;
import com.antonjohansson.svncommit.core.controller.Controller;
import com.antonjohansson.svncommit.core.utils.ForcedExit;
import com.antonjohansson.svncommit.core.utils.UtilityModule;
import com.antonjohansson.svncommit.core.view.View;

import static com.antonjohansson.svncommit.CLI.APPLICATION;
import static com.antonjohansson.svncommit.CLI.CONFIGURATION;
import static com.antonjohansson.svncommit.CLI.OPTIONS;
import static com.antonjohansson.svncommit.CLI.PATH;
import static com.antonjohansson.svncommit.CLI.VERSION;
import static com.google.inject.name.Names.named;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * The main application.
 *
 * @author Anton Johansson
 */
public class SvnCommitApplication extends Application
{
	/** {@inheritDoc} */
	@Override
	public void start(Stage stage) throws Exception
	{
		CommandLine command = getCommand();

		if (command.hasOption(VERSION))
		{
			printVersion();
			ForcedExit.exit();
			return;
		}

		if (command.hasOption(APPLICATION))
		{
			Configuration configuration = getConfiguration(command);

			String application = command.getOptionValue(APPLICATION);
			File path = getPath(command);
			configure(stage, application, path, configuration);
			return;
		}

		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("svn-commit", OPTIONS);
		ForcedExit.exit();
	}

	private File getPath(CommandLine command)
	{
		String pathName = command.getOptionValue(PATH, EMPTY);
		return new File(pathName);
	}

	private Configuration getConfiguration(CommandLine command)
	{
		if (command.hasOption(CONFIGURATION))
		{
			String configurationFilePath = command.getOptionValue(CONFIGURATION);
			File configurationFile = new File(configurationFilePath);
			return new Configuration(configurationFile);
		}
		return new Configuration();
	}

	private void configure(Stage stage, String application, File path, Configuration configuration)
	{
		Module applicationModule = new ApplicationModule();
		Module utilityModule = new UtilityModule();
		Module configurationModule = (binder) ->
		{
			binder.bind(File.class).toInstance(path);
			binder.bind(Configuration.class).toInstance(configuration);
		};

		Injector injector = Guice.createInjector(applicationModule, utilityModule, configurationModule);
		Worker worker = injector.getInstance(Worker.class);
		Controller controller = injector.getInstance(Key.get(Controller.class, named(application)));
		controller.initialize();
		View view = controller.getView();

		stage.setScene(new Scene(view.getParent()));
		stage.setTitle("svn-commit");
		stage.setWidth(1200);
		stage.setHeight(400);
		stage.getIcons().add(new Image("svn.png"));
		stage.setOnCloseRequest(e -> worker.shutdown());
		stage.show();
	}

	private CommandLine getCommand()
	{
		String[] arguments = getParameters().getRaw().toArray(new String[0]);
		try
		{
			return new DefaultParser().parse(OPTIONS, arguments);
		}
		catch (ParseException e)
		{
			throw new RuntimeException(e);
		}
	}

	private void printVersion()
	{
		String version = SvnCommitApplication.class.getPackage().getImplementationVersion();
		System.out.println(defaultIfBlank(version, "Development"));
	}
}
