package mylang.gui.models;

import mylang.data.*;
import mylang.gui.DialogSession;
import java.text.*;
import java.util.*;
import javax.swing.table.*;

/*
 * StatsListTableModel.java
 *
 * Created on 22 listopad 2003, 16:12
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
public class StatsListTableModel extends AbstractTableModel
{
	ArrayList m_stats;
	
	/** Creates a new instance of StatsListTableModel */
	public StatsListTableModel(ArrayList stats)
	{
		setStatsList(stats);
	}
	
	public int getColumnCount()
	{
		return 4;
	}
	
	public String getColumnName(int columnIndex)
	{
		switch(columnIndex)
		{
			case 0: return "Date";
			case 1: return "Mode";
			case 2: return "Score";
			case 3: return "Duration";
		}
		return "";
	}
	
	public int getRowCount()
	{
		return m_stats.size();
	}
	
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		Stat stat = (Stat)m_stats.get(rowIndex);
		switch(columnIndex)
		{
			case 0:
			{
				Calendar cal = stat.getDate();
				return cal.get(Calendar.YEAR) + "." +
				(cal.get(Calendar.MONTH)+1) + "." +
				cal.get(Calendar.DATE);
			}
			case 1:
			{
				Integer mode = stat.getMode();
				if(mode != null)
				{
					switch(mode.intValue())
					{
						case DialogSession.TEACH_MODE:
							return "Teach";
						case DialogSession.TEST_MODE:
							return "Test";
						default:
							return "Invalid";
					}
				}
				else
					return "";
			}
			case 2: return String.valueOf(stat.getScore());
			case 3:
			{
				Calendar duration = stat.getDuration();
				if(duration!=null)
				{
					DecimalFormat df = new DecimalFormat("00");
					return duration.get(Calendar.HOUR) + ":"
					+ df.format(duration.get(Calendar.MINUTE)) + ":"
					+ df.format(duration.get(Calendar.SECOND));
				}
				else
					return "";
			}
		}
		return "";
	}
	
	public void setStatsList(ArrayList stats)
	{
		m_stats = stats;
		fireTableDataChanged();
	}
}
