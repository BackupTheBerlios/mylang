/*
 * DictionaryComparator.java
 *
 * Created on 4 styczeñ 2004, 21:33
 */

package mylang.data;

import java.util.*;

/**
 *
 * @author  herrmic
 */
public class DictionaryComparator implements java.util.Comparator
{
	private int m_col;
	private boolean m_ascending;
	
	/** Creates a new instance of DictionaryComparator */
	public DictionaryComparator(int col, boolean ascending)
	{
		m_col = col;
		m_ascending = ascending;
	}
	
	public int compare(Object o1, Object o2)
	{
		if((o1 instanceof Dictionary) && (o2 instanceof Dictionary))
		{
			Dictionary d1;
			Dictionary d2;
			if(m_ascending)
			{
				d1 = (Dictionary)o1;
				d2 = (Dictionary)o2;
			}
			else
			{
				d2 = (Dictionary)o1;
				d1 = (Dictionary)o2;
			}
			switch(m_col)
			{
				case 0:
					return d1.getFile().getName().compareToIgnoreCase(d2.getFile().getName());
				case 1:
				{
					Calendar cal1 = Calendar.getInstance();
					Calendar cal2 = Calendar.getInstance();
					if(d1.getStats().size() > 0)
						cal1 = ((Stat)d1.getStats().get(d1.getStats().size() - 1)).getDate();
					else
						cal1.set(cal1.getActualMinimum(Calendar.YEAR), cal1.getActualMinimum(Calendar.MONTH), cal1.getActualMinimum(Calendar.DATE));
					if(d2.getStats().size() > 0)
						cal2 = ((Stat)d2.getStats().get(d2.getStats().size() - 1)).getDate();
					else
						cal2.set(cal2.getActualMinimum(Calendar.YEAR), cal2.getActualMinimum(Calendar.MONTH), cal2.getActualMinimum(Calendar.DATE));
					return (int)(cal1.getTimeInMillis() - cal2.getTimeInMillis());
				}
				case 2:
				{
					int score1 = -1;
					int score2 = -1;
					if(d1.getStats().size() > 0)
						score1 = ((Stat)d1.getStats().get(d1.getStats().size() - 1)).getScore();
					if(d2.getStats().size() > 0)
						score2 = ((Stat)d2.getStats().get(d2.getStats().size() - 1)).getScore();
					return score1 - score2;
				}
				case 3:
					return d1.getWordsList().size() - d2.getWordsList().size();
				case 4:
					return d1.getDescription().compareToIgnoreCase(d2.getDescription());
			}
		}
		return 1;
	}
}
