package mylang.data;

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
 * Stores information about a single word.
 * @author herrmic
 */
public class Word
{
	private String[] m_language;
	
	private Stat m_lastStat;
	
	private Dictionary m_dict;
	private boolean m_enabled;
	
	/**
	 * Creates an empty word.
	 * @param dict Parent dictionary of the new word.
	 */
	public Word(Dictionary dict)
	{
		m_language = new String[2];
		m_language[0] = "";
		m_language[1] = "";
		
		m_lastStat = null;
		
		m_dict = dict;
		m_enabled = true;
	}
	/**
	 * Restores the word from the XML representation.
	 * @param dict Parent dictionary of the word.
	 * @param w XML <CODE>Element</CODE> containing word's representation.
	 * @throws DocumentException Thrown in case of invalid XML data.
	 */	
	public Word(Dictionary dict, Element w) throws DocumentException
	{
		this(dict);
		read(w);
	}
	
	/**
	 * Gets the given content of the word.
	 * @param numLang Number of the language whose content will be returned.
	 * @return Content of word's translation to the specified language.
	 */	
	public String getLanguage(int numLang)
	{
		return m_language[numLang];
	}
	/**
	 * Sets the given content of the word.
	 * @param numLang Number of the language whose content will be affected.
	 * @param text A new content for the specified language.
	 */	
	public void setLanguage(int numLang, String text)
	{
		m_language[numLang]=text;
	}
	
	/**
	 * Gets the latest stat related to this word.
	 * @return The latest stat related to this word.
	 */	
	public Stat getLastStat() { return m_lastStat; }
	/**
	 * Sets the new stat related to this word. Currently word can have only one stat
	 * connected.
	 * @param s The stat that will be connected to the word.
	 */	
	public void setLastStat(Stat s) { m_lastStat = s; }
	/**
	 * Checks if the word is enabled. Only enabled words are taken into consideration
	 * when preforming a session.
	 * @return <CODE>true</CODE> if the word is enables, <CODE>false</CODE> otherwise.
	 */	
	public boolean getEnabled() { return m_enabled; }
	/**
	 * Sets the enabled/disabled state of the word. Only enabled words are taken into
	 * consideration when preforming a session.
	 * @param enabled <CODE>true</CODE> to enable the word, <CODE>false</CODE> to disable it.
	 */	
	public void setEnabled(boolean enabled) { m_enabled = enabled; }
	
	/**
	 * Writes word XML representation to the XML element.
	 * @param el Parent XML element; word's own element will be added to it.
	 */	
	public void write(Element el)
	{
		Element w = el.addElement("word").addAttribute("rev", "2");
		if(m_lastStat != null)
			m_lastStat.write(w.addElement("stats"));
		w.addElement("language0")
			.addText(m_language[0]);
		w.addElement("language1")
			.addText(m_language[1]);
	}
	
	private void read(Element w) throws DocumentException
	{
		int rev;
		rev = Integer.parseInt(w.attributeValue("rev", "1"));
		if(rev == 1)
		{
			m_language[0] = w.elementText(m_dict.getLanguageNames()[0]);
			m_language[1] = w.elementText(m_dict.getLanguageNames()[1]);
		}
		else
		{
			m_language[0] = w.elementText("language0");
			m_language[1] = w.elementText("language1");
			
			if(m_language[0] == null || m_language[1] == null)
				throw new DocumentException("No contents defined for word");
		}
		if(w.element("stats") != null)
			m_lastStat = new Stat((Element)w.element("stats").elements().get(0));
	}
	
	/**
	 * Flips the order of the languages of the word.
	 */	
	public void swapLanguages()
	{
		String buf;
		buf = m_language[0];
		m_language[0] = m_language[1];
		m_language[1] = buf;
	}
}
