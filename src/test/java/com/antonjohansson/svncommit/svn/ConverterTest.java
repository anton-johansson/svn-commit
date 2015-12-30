package com.antonjohansson.svncommit.svn;

import org.junit.Assert;
import org.junit.Test;

import com.antonjohansson.svncommit.domain.SvnItem;

/**
 * Unit tests of {@link Converter}.
 *
 * @author Anton Johansson
 */
public class ConverterTest extends Assert
{
	@Test
	public void test_that_modified_file_is_converted()
	{
		SvnItem item = Converter.convertFile("M      some/file/path.txt");

		assertEquals("some/file/path.txt", item.fileNameProperty().getValue());
	}

	@Test
	public void test_that_added_file_is_converted()
	{
		SvnItem item = Converter.convertFile("A      some/file/path.txt");

		assertEquals("some/file/path.txt", item.fileNameProperty().getValue());
	}
}
