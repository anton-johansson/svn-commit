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
package com.antonjohansson.svncommit.core.view.commit;

import com.antonjohansson.svncommit.core.domain.SvnItem;
import com.antonjohansson.svncommit.core.svn.SVN;
import com.antonjohansson.svncommit.core.view.utils.Alerter;

import java.io.File;
import java.util.Collection;
import java.util.function.Consumer;

import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;

/**
 * View that shows commitable items in a folder.
 *
 * @author Anton Johansson
 */
class CommitView extends Pane
{
	CommitView(File directory, ObservableList<SvnItem> items)
	{
		SvnItemTable table = new SvnItemTable();
		table.setItems(items);
		table.setEnterHandler(new CompareEnterHandler(directory));
		table.setSpaceHandler(new SwitchDoCommitSpaceHandler());
		table.prefWidthProperty().bind(widthProperty());
		table.prefHeightProperty().bind(heightProperty());

		getChildren().add(table);
	}

	/**
	 * Handles Enter clicks by bringing up compare windows for each item.
	 *
	 * @author Anton Johansson
	 */
	private static class CompareEnterHandler implements Consumer<Collection<SvnItem>>
	{
		private final File directory;

		CompareEnterHandler(File directory)
		{
			this.directory = directory;
		}

		@Override
		public void accept(Collection<SvnItem> items)
		{
			int size = items.size();
			if (size > 5)
			{
				Alerter.error("I won't open up that many compare windows!");
				return;
			}
			if (size > 1 && !Alerter.confirm("Do you want to open up %d compare windows?", size))
			{
				return;
			}

			items.forEach(i -> SVN.compare(directory, i.getFileName()));
		}
	}

	/**
	 * Handles Space clicks by switching the 'doCommit' property.
	 *
	 * @author Anton Johansson
	 */
	private static class SwitchDoCommitSpaceHandler implements Consumer<Collection<SvnItem>>
	{
		@Override
		public void accept(Collection<SvnItem> items)
		{
			boolean allMarked = items.stream().allMatch(i -> i.isDoCommit());
			boolean doCommit = !allMarked;
			items.forEach(i -> i.setDoCommit(doCommit));
		}
	}
}
