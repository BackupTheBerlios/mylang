/*
 * StatTest.java
 * JUnit based test
 *
 * Created on 19 styczeñ 2004, 23:56
 */

package test.mylang.data;

import junit.framework.*;
import mylang.data.Stat;
import mylang.data.Session;
import java.util.*;

/**
 *
 * @author herrmic
 */
public class StatTest extends TestCase
{
	
	public StatTest(java.lang.String testName)
	{
		super(testName);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(StatTest.class);
		return suite;
	}
	
	// TODO add test methods here, they have to start with 'test' name.
	// for example:
	// public void testHello() {}
	
	public void testStat_int()
	{
		Stat stat = new Stat(15);
		assertEquals(15, stat.getScore());
		assertNotNull(stat.getDate());
		assertNull(stat.getMode());
		assertNull(stat.getDuration());
	}
	
	public void testStat_int_Integer_Calendar()
	{
		Stat stat = new Stat(15, new Integer(Session.TEST_MODE), new GregorianCalendar(1982, Calendar.OCTOBER, 8));
		assertEquals(15, stat.getScore());
		assertNotNull(stat.getDate());
		assertEquals(new Integer(Session.TEST_MODE),  stat.getMode());
		assertEquals(new GregorianCalendar(1982, Calendar.OCTOBER, 8), stat.getDuration());
	}
	
}
