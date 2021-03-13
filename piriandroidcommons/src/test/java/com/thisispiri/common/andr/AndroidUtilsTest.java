package com.thisispiri.common.andr;

import android.os.Bundle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

import static com.thisispiri.common.andr.AndrUtil.bundleWith;

@RunWith(RobolectricTestRunner.class) public class AndroidUtilsTest {
	@Test public void bundleWithTest() throws Exception {
		Bundle res = bundleWith("a", "b", "c", "d");
		//Test if it does its job
		assertEquals("b", res.getString("a"));
		assertEquals("d", res.getString("c"));
		//Test if it inserts any unexpected values
		assertEquals(2, res.keySet().size());

		res = bundleWith("qo#04", "paie", "c");
		//Test if it ignores the last value when fed an odd number of arguments
		assertNull(res.getString("c"));
		//Test if it puts the other arguments correctly
		assertEquals("paie", res.getString("qo#04"));

		//Test if it returns an empty Bundle when fed nothing or only 1 argument
		res = bundleWith();
		assertTrue(res.isEmpty());
		res = bundleWith("flaskjf;lsadkj");
		assertTrue(res.isEmpty());

		//Test if it handles unicode Strings correctly
		res = bundleWith("반갑", "습니다", "유니", "코드");
		assertEquals("습니다", res.getString("반갑"));
		assertEquals("코드", res.getString("유니"));
	}
}
