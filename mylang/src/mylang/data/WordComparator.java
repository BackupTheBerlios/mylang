/*
 * WordComparator.java
 *
 * Created on 4 styczeñ 2004, 22:28
 */

package mylang.data;

import java.util.*;

/**
 *
 * @author  herrmic
 */
public class WordComparator implements java.util.Comparator
{
	private int m_col;
	private boolean m_ascending;
	
	/** Creates a new instance of WordComparator */
	public WordComparator(int col, boolean ascending)
	{
		m_col = col;
		m_ascending = ascending;
	}
	
	public int compare(Object o1, Object o2)
	{
		if((o1 instanceof Word) && (o2 instanceof Word))
		{
			Word w1;
			Word w2;
			if(m_ascending)
			{
				w1 = (Word)o1;
				w2 = (Word)o2;
			}
			else
			{
				w2 = (Word)o1;
				w1 = (Word)o2;
			}
			switch(m_col)
			{
				case 0:
					return w1.getLanguage(0).compareToIgnoreCase(w2.getLanguage(0));
				case 1:
					return w1.getLanguage(1).compareToIgnoreCase(w2.getLanguage(1));
				case 2:
				{
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					if(w1.getLastStat() != null)
						cal1 = w1.getLastStat().getDate();
					else
						cal1.set(cal1.getActualMinimum(Calendar.YEAR), cal1.getActualMinimum(Calendar.MONTH), cal1.getActualMinimum(Calendar.DATE));
					if(w2.getLastStat() != null)
						cal2 = w2.getLastStat().getDate();
					else
						cal2.set(cal2.getActualMinimum(Calendar.YEAR), cal2.getActualMinimum(Calendar.MONTH), cal2.getActualMinimum(Calendar.DATE));
					return (int)(cal1.getTimeInMillis() - cal2.getTimeInMillis());
				}
				case 3:
				{
					int score1 = -1;
					int score2 = -1;
					if(w1.getLastStat() != null)
						score1 = w1.getLastStat().getScore();
					if(w2.getLastStat() != null)
						score2 = w2.getLastStat().getScore();
					return score1 - score2;
				}
				case 4:
					return (w1.getEnabled() ? 1 : 0) - (w2.getEnabled() ? 1 : 0);
			}
		}
		return 1;
	}
}
