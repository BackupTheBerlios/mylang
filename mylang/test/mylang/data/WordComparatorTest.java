/*
 * WordComparatorTest.java
 * JUnit based test
 *
 * Created on 30 styczeñ 2004, 20:54
 */

package mylang.data;

import junit.framework.*;
import mylang.data.Stat;
import mylang.data.Word;
import mylang.data.WordComparator;
import org.dom4j.*;

/**
 *
 * @author herrmic
 */
public class WordComparatorTest extends TestCase
{
	
	public WordComparatorTest(java.lang.String testName)
	{
		super(testName);
	}
	
	public static Test suite()
	{
		TestSuite suite = new TestSuite(WordComparatorTest.class);
		return suite;
	}
	
	public void testCompare()
	{
		// The words w1 and w11 are equal
		Word w1 = new Word(null);
		w1.setLanguage(0, "alfa");
		w1.setLanguage(1, "omega");
		w1.setLastStat(new Stat(13));
		
		Word w11 = new Word(null);
		w11.setLanguage(0, "alfa");
		w11.setLanguage(1, "omega");
		w11.setLastStat(new Stat(13));
				
		// The w2 has to be different to w1
		Word w2 = new Word(null);
		w2.setLanguage(0, "beta");
		w2.setLanguage(1, "delta");
		w2.setLastStat(new Stat(7));
		w2.setEnabled(false);
		
		// Test comparision: language 0 (alfa / beta)
		assertTrue(new WordComparator(0, true).compare(w1, w2) < 0);
		assertTrue(new WordComparator(0, true).compare(w1, w11) == 0);
		assertTrue(new WordComparator(0, true).compare(w2, w1) > 0);		
		assertTrue(new WordComparator(0, false).compare(w1, w2) > 0);
		assertTrue(new WordComparator(0, false).compare(w1, w11) == 0);
		assertTrue(new WordComparator(0, false).compare(w2, w1) < 0);
		
		// Test comparision: language 1 (omega / delta)
		assertTrue(new WordComparator(1, true).compare(w1, w2) > 0);
		assertTrue(new WordComparator(1, true).compare(w1, w11) == 0);
		assertTrue(new WordComparator(1, true).compare(w2, w1) < 0);		
		assertTrue(new WordComparator(1, false).compare(w1, w2) < 0);
		assertTrue(new WordComparator(1, false).compare(w1, w11) == 0);
		assertTrue(new WordComparator(1, false).compare(w2, w1) > 0);
		
		// Test comparision: score (13 / 7)
		assertTrue(new WordComparator(3, true).compare(w1, w2) > 0);
		assertTrue(new WordComparator(3, true).compare(w1, w11) == 0);
		assertTrue(new WordComparator(3, true).compare(w2, w1) < 0);		
		assertTrue(new WordComparator(3, false).compare(w1, w2) < 0);
		assertTrue(new WordComparator(3, false).compare(w1, w11) == 0);
		assertTrue(new WordComparator(3, false).compare(w2, w1) > 0);
		
		// Test comparision: enabled (true / false)
		assertTrue(new WordComparator(3, true).compare(w1, w2) > 0);
		assertTrue(new WordComparator(3, true).compare(w1, w11) == 0);
		assertTrue(new WordComparator(3, true).compare(w2, w1) < 0);		
		assertTrue(new WordComparator(3, false).compare(w1, w2) < 0);
		assertTrue(new WordComparator(3, false).compare(w1, w11) == 0);
		assertTrue(new WordComparator(3, false).compare(w2, w1) > 0);
		
		// Miscellaneous comparisions
		assertTrue(new WordComparator(0, false).compare(w1, null) != 0);
		assertTrue(new WordComparator(0, false).compare(w1, "string?") != 0);
		assertTrue(new WordComparator(0, false).compare(null, null) != 0);
	}
	
}
