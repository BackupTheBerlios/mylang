/*
 * StatTest.java
 * JUnit based test
 *
 * Created on 19 styczeñ 2004, 23:56
 */

package mylang.data;

import junit.framework.*;
import mylang.data.Stat;
import mylang.data.Session;
import java.util.*;
import org.dom4j.*;

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

	public void testStat_Element()
	{
		{
			Element el = DocumentHelper.createElement("stat");
			el.addAttribute("score", "21");
			el.addAttribute("date", "17.11.1983");
			
			Stat stat = new Stat(el);
			assertEquals(21, stat.getScore());
			assertNotNull(stat.getDate());
			assertNull(stat.getMode());
			assertNull(stat.getDuration());
		}
		{
			Element el = DocumentHelper.createElement("stat");
			el.addAttribute("score", "21");
			el.addAttribute("date", "17.11.1983");
			el.addAttribute("mode", "1");
			el.addAttribute("duration", "2:45:27");
			
			Stat stat = new Stat(el);
			assertEquals(21, stat.getScore());
			assertNotNull(stat.getDate());
			assertEquals(new Integer(1), stat.getMode());
			assertNotNull(stat.getDuration());
		}
	}
	
	public void testWrite()
	{
		{
			Element parent = DocumentHelper.createElement("stats");
			Stat stat = new Stat(17);
			stat.write(parent);
			
			Element el = parent.element("stat");
			assertEquals("17", el.attributeValue("score"));
			assertNotNull(el.attributeValue("date")); // TODO: make a real comparision of the calendars
			assertNull(el.attributeValue("mode"));
			assertNull(el.attributeValue("duration"));
		}
		{
			Element parent = DocumentHelper.createElement("stats");
			Stat stat = new Stat(18, new Integer(Session.TEST_MODE), new GregorianCalendar());
			stat.write(parent);
			
			Element el = parent.element("stat");
			assertEquals("18", el.attributeValue("score"));
			assertNotNull(el.attributeValue("date")); // TODO: make a real comparision of the calendars
			assertEquals(new Integer(Session.TEST_MODE), Integer.valueOf(el.attributeValue("mode")));
			assertNotNull(el.attributeValue("duration")); // TODO: make a real comparision of the calendars
		}
	}
}
