package mylang.gui.models;

import mylang.data.*;
import javax.swing.table.*;

/*
 * DictionaryTableModel.java
 *
 * Created on 11 pa¼dziernik 2003, 18:56
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
public class WordsEditorTableModel extends AbstractTableModel
{
	private Dictionary m_dict;
	
	/** Creates a new instance of DictionaryTableModel */
	public WordsEditorTableModel()
	{
		m_dict = new Dictionary();
	}
		
	public void addWord()
	{
		Word w = new Word(m_dict);
		m_dict.getWordsList().add(w);
		fireTableRowsInserted(m_dict.getWordsList().size(), m_dict.getWordsList().size());
	}
	
	public void removeWords(int[] indices)
	{
		for(int i=0; i<indices.length; i++)
		{
			m_dict.getWordsList().remove(indices[i] - i);
			fireTableRowsDeleted(indices[i] - i, indices[i] - i);
		}
	}
	
	public void setDictionary(Dictionary dict)
	{
		m_dict = dict;
		fireTableDataChanged();
	}
	
	// AbstractTableModel
	
	public Class getColumnClass(int columnIndex)
	{
		return String.class;
	}
	
	public int getColumnCount()
	{
		return 2;
	}
	
	public int getRowCount()
	{
		return m_dict.getWordsList().size();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		return ((Word)m_dict.getWordsList().get(rowIndex)).getLanguage(columnIndex);
	}
	
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		Word w = (Word)m_dict.getWordsList().get(rowIndex);
		w.setLanguage(columnIndex, aValue.toString());
		w.setLastStat(null);
		fireTableCellUpdated(rowIndex, columnIndex);
	}
	
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return true;
	}
}
