package mylang.data;

import java.util.*;
import org.dom4j.*;

/*
 * Stat.java
 *
 * Created on 13 pa¼dziernik 2003, 20:21
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
 * Contains single stat related to some <CODE>Word</CODE> or
 * <CODE>Dictionary</CODE>.
 * @author herrmic
 */
public class Stat
{
	
	private Calendar m_date;
	private int m_score;
	private Integer m_mode;
	private Calendar m_duration;
	
	/**
	 * Creates a new <CODE>Stat</CODE> with the given score.
	 * @param score The score that the stat will contain.
	 */
	public Stat(int score)
	{
		this(score, null, null);
	}
	/**
	 * Creates new stat with given score, mode and duration.
	 * @param score Score of the stat.
	 * @param mode Mode of the stat, same as <CODE>Session</CODE> modes.
	 * @param duration Duration of the session that caused generation of the stat.
	 */	
	public Stat(int score, Integer mode, Calendar duration)
	{
		m_date = Calendar.getInstance();
		m_score = score;
		m_mode = mode;
		m_duration = duration;
	}
	/**
	 * Loads the stat from the XML data.
	 * @param stat XML <CODE>Element</CODE> containing the stat data.
	 */	
	public Stat(Element stat)
	{
		read(stat);
	}
	
	/**
	 * Retrieves the date of the stat creation.
	 * @return Date of the stat creation.
	 */	
	public Calendar getDate()
	{
		return m_date;
	}
	/**
	 * Score of the stat.
	 * @return Score of the stat.
	 */	
	public int getScore()
	{
		return m_score;
	}
	/**
	 * Gets mode of the session that created the stat.
	 * @return Mode of the session that created the stat.
	 */	
	public Integer getMode()
	{
		return m_mode;
	}
	/**
	 * Gets duration of the session that generated the stat.
	 * @return Duration of the session that generated the stat.
	 */	
	public Calendar getDuration()
	{
		return m_duration;
	}
	
	/**
	 * Adds stat's XML representation to the given parent XML element.
	 * @param doc The parent XML <CODE>Element</CODE> that contains stats.
	 */	
	public void write(Element doc)
	{
		Element s = doc.addElement("stat")
			.addAttribute("score", Integer.toString(m_score))
			.addAttribute("date",
				m_date.get(Calendar.YEAR) + "."
				+ (m_date.get(Calendar.MONTH) + 1) + "."
				+ m_date.get(Calendar.DATE));
		if(m_mode != null)
			s.addAttribute("mode", m_mode.toString());
		if(m_duration != null)
			s.addAttribute("duration",
				m_duration.get(Calendar.HOUR) + ":"
				+ m_duration.get(Calendar.MINUTE) + ":"
				+ m_duration.get(Calendar.SECOND));
	}
	
	private void read(Element stat) throws NumberFormatException
	{
		m_score = Integer.valueOf(stat.attributeValue("score")).intValue();
		
		String[] date = stat.attributeValue("date").split("\\.");
		m_date = Calendar.getInstance();
		m_date.set(
			Integer.parseInt(date[0]),
			Integer.parseInt(date[1]) - 1,
			Integer.parseInt(date[2]));
		
		String s;
		
		s = stat.attributeValue("mode");
		if(s != null)
			m_mode = Integer.valueOf(s);
		else
			m_mode = null;
		
		s = stat.attributeValue("duration");
		if(s != null)
		{
			String[] duration = s.split(":");
			m_duration = Calendar.getInstance();
			m_duration.set(Calendar.HOUR, Integer.parseInt(duration[0]));
			m_duration.set(Calendar.MINUTE, Integer.parseInt(duration[1]));
			m_duration.set(Calendar.SECOND, Integer.parseInt(duration[2]));
		}
		else
			m_duration = null;
	}
}
