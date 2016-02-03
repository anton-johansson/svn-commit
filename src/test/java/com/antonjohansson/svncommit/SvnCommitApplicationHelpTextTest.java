package com.antonjohansson.svncommit;

import static java.lang.System.lineSeparator;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Integration test of {@link SvnCommitApplication} that tests running the
 * application with no arguments, which results in help text being printed.
 *
 * @author Anton Johansson
 */
public class SvnCommitApplicationHelpTextTest extends Assert
{
	private PrintStream oldOutput;
	private ByteArrayOutputStream output;

	@Before
	public void setUp()
	{
		output = new ByteArrayOutputStream();
		PrintStream stream = new PrintStream(output);

		oldOutput = System.out;
		System.setOut(stream);
	}

	@After
	public void tearDown()
	{
		System.setOut(oldOutput);
	}

	@Test
	public void test_print_help_text()
	{
		EntryPoint.main(new String[] {});

		String actual = output.toString();
		String expected = new StringBuilder()
				.append("usage: svn-commit").append(lineSeparator())
				.append(" -a,--application <APP>      the application to run, e. g. 'commit'").append(lineSeparator())
				.append(" -c,--configuration <PATH>   the location of the configuration file").append(lineSeparator())
				.append(" -p,--path <PATH>            the path to execute SVN commands in").append(lineSeparator())
				.append(" -v,--version                prints the version of the application").append(lineSeparator())
				.toString();

		assertEquals(expected, actual);
	}
}
