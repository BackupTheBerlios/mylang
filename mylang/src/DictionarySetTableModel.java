import java.io.*;
import java.util.*;
import javax.swing.table.*;

/*
 * DictionariesTableModel.java
 *
 * Created on 25 pa¼dziernik 2003, 16:59
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
public class DictionarySetTableModel extends AbstractTableModel
{
	DictionarySet m_dset;
	
	/** Creates a new instance of DictionariesTableModel */
	public DictionarySetTableModel()
	{
		m_dset = new DictionarySet();
	}
	
	public void addDictionary(File file) throws IOException
	{
		m_dset.loadDictionary(file);
		fireTableRowsInserted(m_dset.getDictionaries().size(), m_dset.getDictionaries().size());
	}
	
	public Class getColumnClass(int columnIndex)
	{
		switch(columnIndex)
		{
			case 0: return String.class;
			case 1: return String.class;
			case 2: return String.class;
			case 3: return String.class;
			case 4: return String.class;
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
			case 0: return "Name";
			case 1: return "Last (date)";
			case 2: return "Last (score)";
			case 3: return "Size";
			case 4: return "Description";
		}
		return "";
	}
	
	public int getRowCount()
	{
		return m_dset.getDictionaries().size();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Dictionary d = (Dictionary)m_dset.getDictionaries().get(rowIndex);
		Stat stat = null;
		if(!d.getStats().isEmpty())
			stat = (Stat)d.getStats().get(d.getStats().size() - 1);
		
		switch(columnIndex)
		{
			case 0: return d.getFile().getName();
			case 1:
			{
				if(stat == null)
					return "-";
				else
				{
					Calendar cal = stat.getDate();
					return cal.get(Calendar.YEAR) + "." +
					(cal.get(Calendar.MONTH)+1) + "." + 
					cal.get(Calendar.DATE);
				}
			}
			case 2:
			{
				if(stat == null)
					return "-";
				else
					return Integer.toString(stat.getScore());

			}
			case 3: return String.valueOf(d.getWordsList().size());
			case 4: return d.getDescription();
		}
		return "";
	}
	
	public void setDictionarySet(DictionarySet dset)
	{
		m_dset = dset;
		fireTableDataChanged();
	}
}
