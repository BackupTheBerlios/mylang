import java.util.*;
import javax.swing.table.*;

/*
 * WordsListTableModel.java
 *
 * Created on 25 pa¼dziernik 2003, 19:01
 *
 * Copyright 2003 Michal Dabrowski
 *
 * This file is part of MyLang.
 *
 * MyLang is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * MyLang is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyLang; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/**
 *
 * @author  herrmic
 */
public class WordsListTableModel extends AbstractTableModel
{
	private static ArrayList m_emptyList = new ArrayList();
	
	public WordsContainer m_wc;
	
	private boolean[] m_shadowColumn;
	
	/** Creates a new instance of WordsListTableModel */
	public WordsListTableModel()
	{
		m_wc = new WordsContainer(){
			public ArrayList getWordsList()
			{
				return m_emptyList;
			}
		};
		
		m_shadowColumn = new boolean[2];
		m_shadowColumn[0] = false;
		m_shadowColumn[1] = false;
	}
	
	public void shadowColumn(int language, boolean shadow)
	{
		m_shadowColumn[language] = shadow;
		fireTableDataChanged();
	}
	
	public Class getColumnClass(int columnIndex)
	{
		switch(columnIndex)
		{
			case 0: return String.class;
			case 1: return String.class;
			case 2: return String.class;
			case 3: return String.class;
			case 4: return Boolean.class;
		}
		return null;
	}
	
	public int getColumnCount()
	{
		return 5;
	}
	
	public String getColumnName(int columnIndex)
	{
		switch(columnIndex)
		{
			case 0: return "Language 1";
			case 1: return "Language 2";
			case 2: return "Last (date)";
			case 3: return "Last (score)";
			case 4: return "Enabled";
		}
		return "";
	}
	
	public int getRowCount()
	{
		return m_wc.getWordsList().size();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Word w = (Word)m_wc.getWordsList().get(rowIndex);
		Stat stat = w.getLastStat();
		
		switch(columnIndex)
		{
			case 0: 
				if(m_shadowColumn[0])
					return "<HTML><I>shadow</I></HTML>";
				else
					return w.getLanguage(0);
			case 1: 
				if(m_shadowColumn[1])
					return "<HTML><I>shadow</I></HTML>";
				else
					return w.getLanguage(1);
			case 2: 
			{
				if(stat == null)
					return "-";
				else
				{
					Calendar cal = w.getLastStat().getDate();
					return cal.get(Calendar.YEAR) + "." +
					(cal.get(Calendar.MONTH)+1) + "." + 
					cal.get(Calendar.DATE);
				}
			}
			case 3: 
			{
				if(stat == null)
					return "-";
				else
					return Integer.toString(w.getLastStat().getScore());
			}
			case 4: 
				return new Boolean(w.getEnabled());
		}
		return "";
	}
	
	public void setWordsContainer(WordsContainer wc)
	{
		m_wc = wc;
		fireTableDataChanged();
	}
}
