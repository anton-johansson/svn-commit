package com.antonjohansson.svncommit;

import com.antonjohansson.svncommit.application.commit.CommitApplication;

import static javafx.application.Application.launch;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;
import static org.apache.commons.lang3.ArrayUtils.remove;

/**
 * Contains the applications main entry-point.
 *
 * @author Anton Johansson
 */
public class EntryPoint
{
	public static void main(String[] arguments)
	{
		if (isEmpty(arguments))
		{
			System.err.println("Missing first argument");
			System.exit(1);
			return;
		}

		String[] innerArguments = remove(arguments, 0);
		switch (arguments[0])
		{
			case "commit":	launch(CommitApplication.class, innerArguments);

			default:
				System.err.println("Invalid usage");
				System.exit(1);
		}
	}
}
