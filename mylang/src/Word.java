import org.dom4j.*;

/*
 * Word.java
 *
 * Created on 11 pa¼dziernik 2003, 19:35
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
public class Word
{
	private String[] m_language;
	
	private Stat m_lastStat;
	
	private Dictionary m_dict;
	private boolean m_enabled;
	
	/** Creates a new instance of Word */
	public Word(Dictionary dict)
	{
		m_language = new String[2];
		m_language[0] = "";
		m_language[1] = "";
		
		m_lastStat = null;
		
		m_dict = dict;
		m_enabled = true;
	}
	public Word(Dictionary dict, Element w)
	{
		this(dict);
		read(w);
	}
	
	public String getLanguage(int numLang)
	{
		return m_language[numLang];
	}
	public void setLanguage(int numLang, String text)
	{
		m_language[numLang]=text;
	}
	
	public Stat getLastStat() { return m_lastStat; }
	public void setLastStat(Stat s) { m_lastStat = s; }
	public boolean getEnabled() { return m_enabled; }
	public void setEnabled(boolean enabled) { m_enabled = enabled; }
	
	public void write(Element el)
	{
		Element w = el.addElement("word");
		if(m_lastStat != null)
			m_lastStat.write(w.addElement("stats"));
		w.addElement(m_dict.getLanguageNames()[0])
			.addText(m_language[0]);
		w.addElement(m_dict.getLanguageNames()[1])
			.addText(m_language[1]);
	}
	
	private void read(Element w)
	{
		m_language[0] = w.elementText(m_dict.getLanguageNames()[0]);
		m_language[1] = w.elementText(m_dict.getLanguageNames()[1]);
		if(w.element("stats") != null)
			m_lastStat = new Stat((Element)w.element("stats").elements().get(0));
	}
	
	public void swapLanguages()
	{
		String buf;
		buf = m_language[0];
		m_language[0] = m_language[1];
		m_language[1] = buf;
	}
}
