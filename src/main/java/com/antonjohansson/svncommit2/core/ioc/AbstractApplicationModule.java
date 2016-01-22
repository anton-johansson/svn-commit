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
package com.antonjohansson.svncommit2.core.ioc;

import com.antonjohansson.svncommit2.core.controller.Controller;
import com.antonjohansson.svncommit2.core.view.View;

import static com.google.inject.name.Names.named;

import java.io.IOException;
import java.net.URL;
import java.util.function.Supplier;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provider;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * Extension of {@link AbstractModule} that provides extra utility methods
 * for binding in the {@code svn-commit} application.
 *
 * @author Anton Johansson
 */
public abstract class AbstractApplicationModule extends AbstractModule
{
	/**
	 * @see Binder#install(Module)
	 */
	protected void install(Supplier<Module> moduleSupplier)
	{
		install(moduleSupplier.get());
	}

	/**
	 * Binds a controller instance.
	 *
	 * @param key The key of the controller.
	 * @param controllerClass The class of the controller.
	 */
	protected <C extends Controller> void controller(String key, Class<C> controllerClass)
	{
		bind(Controller.class).annotatedWith(named(key)).to(controllerClass);
	}

	/**
	 * Binds a view instance.
	 *
	 * @param viewClass The class of the view.
	 */
	protected <V extends View> void view(Class<V> viewClass)
	{
		bind(viewClass).toProvider(new ViewProvider<V>(viewClass));
	}

	/**
	 * Provides {@link View} implementations through FXML.
	 *
	 * @param <V> The view to provide.
	 * @author Anton Johansson
	 */
	private class ViewProvider<V extends View> implements Provider<V>
	{
		private final Class<V> viewClass;

		private ViewProvider(Class<V> viewClass)
		{
			this.viewClass = viewClass;
		}

		@Override
		public V get()
		{
			String name = viewClass.getSimpleName() + ".fxml";
			URL location = viewClass.getResource(name);
			try
			{
				FXMLLoader loader = new FXMLLoader(location);
				Parent parent = loader.load();
				V view = loader.getController();
				view.setParent(parent);
				return view;
			}
			catch (IOException e)
			{
				throw new RuntimeException(e);
			}
		}
	}
}
