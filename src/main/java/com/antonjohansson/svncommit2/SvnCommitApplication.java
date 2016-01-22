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
package com.antonjohansson.svncommit2;

import com.antonjohansson.svncommit.EntryPoint;
import com.antonjohansson.svncommit2.core.controller.Controller;
import com.antonjohansson.svncommit2.core.utils.UtilityModule;
import com.antonjohansson.svncommit2.core.view.View;

import static com.google.inject.name.Names.named;
import static java.lang.System.exit;
import static org.apache.commons.cli.Option.builder;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
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
	private static final Options OPTIONS = new Options()
			.addOption(builder("c")
					.longOpt("configuration")
					.desc("the location of the configuration file")
					.hasArg()
					.argName("PATH")
					.build())
			.addOption(builder("a")
					.longOpt("application")
					.desc("the application to run, e. g. 'commit'")
					.hasArg()
					.argName("APP")
					.build())
			.addOption(builder("p")
					.longOpt("path")
					.desc("the path to execute SVN commands in")
					.hasArg()
					.argName("PATH")
					.build())
			.addOption(builder("v")
					.longOpt("version")
					.desc("prints the version of the application")
					.build());

	@Override
	public void start(Stage stage) throws Exception
	{
		CommandLine command = getCommand();

		if (command.hasOption("version"))
		{
			printVersion();
			exit(0);
			return;
		}

		if (command.hasOption("application"))
		{
			String application = command.getOptionValue("application");
			String path = command.getOptionValue("path");
			configure(stage, application, new File(path));
			return;
		}

		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("svn-commit", OPTIONS);
		exit(1);
	}

	private void configure(Stage stage, String application, File path)
	{
		Module applicationModule = new ApplicationModule();
		Module utilityModule = new UtilityModule();
		Module configurationModule = (binder) ->
		{
			binder.bind(File.class).toInstance(path);
		};

		Injector injector = Guice.createInjector(applicationModule, utilityModule, configurationModule);
		Controller controller = injector.getInstance(Key.get(Controller.class, named(application)));
		controller.initialize();
		View view = controller.getView();

		stage.setScene(new Scene(view.getParent()));
		stage.setTitle("svn-commit");
		stage.setWidth(1200);
		stage.setHeight(400);
		stage.getIcons().add(new Image("svn.png"));
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
		String version = EntryPoint.class.getPackage().getImplementationVersion();
		System.out.println(defaultIfBlank(version, "Development"));
	}
}
