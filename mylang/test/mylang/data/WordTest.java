/*
 * Word.java
 * JUnit based test
 *
 * Created on 20 grudzieñ 2003, 17:12
 */

package test.mylang.data;

import junit.framework.*;
import mylang.data.Word;
import mylang.data.Stat;
import org.dom4j.*;

/**
 *
 * @author herrmic
 */
public class WordTest extends TestCase
{
	
	public WordTest(java.lang.String testName)
	{
		super(testName);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(WordTest.class);
		return suite;
	}
	
	// Add test methods here, they have to start with 'test' name.
	// for example:
	// public void testHello() {}
	
	public void testWord_Dictionary()
	{
		Word w = new Word(null);
		assertEquals("", w.getLanguage(0));
		assertEquals("", w.getLanguage(1));
		assertNull(w.getLastStat());
		assertEquals(true, w.getEnabled());
	}
	
	public void testWord_Dictionary_Element()
	{
		// Test the constructor Word(Dictionary, Element);
		// Case:
		//	both languages are defined
		//	lastStat is not defined
		{
			Element el = DocumentHelper.createElement("word");
			el.addAttribute("rev", "2");
			el.addElement("language0").addText("zero");
			el.addElement("language1").addText("one");
			
			Word w = null;
			try
			{
				w = new Word(null, el);
			}
			catch(DocumentException ex)
			{
				fail("Word was expected to load.");
			}
			assertEquals("zero", w.getLanguage(0));
			assertEquals("one", w.getLanguage(1));
			assertNull(w.getLastStat());
		}
		
		// Test the constructor Word(Dictionary, Element);
		// Case:
		//	both languages are defined
		//	lastStat is defined
		{
			Element el = DocumentHelper.createElement("word");
			el.addAttribute("rev", "2");
			el.addElement("language0").addText("zero");
			el.addElement("language1").addText("one");
			Stat stat = new Stat(123);
			stat.write(el.addElement("stats"));
			
			Word w = null;
			try
			{
				w = new Word(null, el);
			}
			catch(DocumentException ex)
			{
				fail("Word was expected to load.");
			}
			assertEquals("zero", w.getLanguage(0));
			assertEquals("one", w.getLanguage(1));
			assertEquals(123, w.getLastStat().getScore());
		}
		
		// Test the constructor Word(Dictionary, Element);
		// Case:
		//	no languages are defined
		//	lastStat is not defined
		{
			Element el = DocumentHelper.createElement("word");
			el.addAttribute("rev", "2");

			try
			{
				new Word(null, el);
				fail("Exception expected");
			}
			catch(Exception ex)
			{
			}
		}
	}
	
	public void testLanguage()
	{
		Word w = new Word(null);
		w.setLanguage(0, "zero");
		w.setLanguage(1, "one");
		assertEquals("zero", w.getLanguage(0));
		assertEquals("one", w.getLanguage(1));
	}
	
	public void testLastStat()
	{
		Word w = new Word(null);
		Stat stat = new Stat(0);
		w.setLastStat(stat);
		assertSame(w.getLastStat(), stat);
	}
	
	public void testEnabled()
	{
		Word w = new Word(null);
		w.setEnabled(true);
		assertEquals(true, w.getEnabled());
		w.setEnabled(false);
		assertEquals(false, w.getEnabled());
	}
	
	public void testWrite()
	{
		// Test the method write(Element)
		// Case:
		//	both languages are defined
		//	lastStat is not defined
		{
			Element parent = DocumentHelper.createElement("words");
			Word w = new Word(null);
			w.setLanguage(0, "zero");
			w.setLanguage(1, "one");
			w.write(parent);
			
			Element el = parent.element("word");
			assertEquals("zero", el.elementText("language0"));
			assertEquals("one", el.elementText("language1"));
			assertNull(el.element("stats"));
		}
		
		// Test the method write(Element)
		// Case:
		//	both languages are defined
		//	lastStat is defined
		{
			Element parent = DocumentHelper.createElement("words");
			Word w = new Word(null);
			w.setLanguage(0, "zero");
			w.setLanguage(1, "one");
			w.setLastStat(new Stat(123));
			w.write(parent);
			
			Element el = parent.element("word");
			assertEquals("zero", el.elementText("language0"));
			assertEquals("one", el.elementText("language1"));
			assertNotNull(el.element("stats"));
			assertNotNull(el.element("stats").element("stat"));
			Stat stat = new Stat(el.element("stats").element("stat"));
			assertEquals(123, stat.getScore());
		}		
	}
	
	public void testSwapLanguages()
	{
		Word w = new Word(null);
		w.setLanguage(0, "Zero");
		w.setLanguage(1, "One");
		w.swapLanguages();
		assertEquals("One", w.getLanguage(0));
		assertEquals("Zero", w.getLanguage(1));
	}
}
