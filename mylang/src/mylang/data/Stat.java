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
 *
 * @author  herrmic
 */
public class Stat
{
	
	private Calendar m_date;
	private int m_score;
	private Integer m_mode;
	private Calendar m_duration;
	
	/** Creates a new instance of Stat */
	public Stat(int score)
	{
		this(score, null, null);
	}
	public Stat(int score, Integer mode, Calendar duration)
	{
		m_date = Calendar.getInstance();
		m_score = score;
		m_mode = mode;
		m_duration = duration;
	}
	public Stat(Element stat)
	{
		read(stat);
	}
	
	public Calendar getDate()
	{
		return m_date;
	}
	public int getScore()
	{
		return m_score;
	}
	public Integer getMode()
	{
		return m_mode;
	}
	public Calendar getDuration()
	{
		return m_duration;
	}
	
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
