package com.github.avarabyeu.jashing.utils;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for {@link com.github.avarabyeu.jashing.utils.StringUtils}
 * @author Andrei Varabyeu
 */
public class StringUtilsTest {

    @Test
    public void testSubstringBefore(){
        String before = StringUtils.substringBefore("test string", "st");
        Assert.assertThat("Incorrect 'substringBefore' result", before, CoreMatchers.is("te"));
    }
}
