package mylang.data;

import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;

/*
 * Dictionary.java
 *
 * Created on 11 pa¼dziernik 2003, 17:39
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

public class Dictionary implements WordsContainer
{
	private ArrayList m_words;
	private String[] m_languageNames;
	private String m_description;
	private ArrayList m_stats;
	
	private File m_file;
	
	/** Creates a new instance of Dictionary */
	public Dictionary()
	{
		m_words = new ArrayList();
		m_languageNames = new String[2];
		m_languageNames[0] = "";
		m_languageNames[1] = "";
		m_description = "";
		m_stats = new ArrayList();
		
		m_file = null;
	}
	
	public Dictionary(File file) throws IOException
	{
		this();
		read(file);
	}
	
	public ArrayList getWordsList()
	{
		return m_words;
	}
	
	public String[] getLanguageNames()
	{ 
		return m_languageNames; 
	}
	
	public String getDescription()
	{ 
		return m_description; 
	}
	
	public void  setDescription(String desc)
	{ 
		m_description = desc; 
	}
	
	public ArrayList getStats()
	{
		return m_stats; 
	}
	
	public File getFile()
	{
		return m_file; 
	}
	
	public void write(File file) throws IOException
	{
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("dictionary");
		
		root.addAttribute("languageName0", m_languageNames[0])
			.addAttribute("languageName1", m_languageNames[1])
			.addAttribute("description", m_description);
		
		Element words = root.addElement("words");
		for(int i=0; i<m_words.size(); i++)
			((Word)m_words.get(i)).write(words);
		
		Element stats = root.addElement("stats");
		for(int i=0; i<m_stats.size(); i++)
			((Stat)m_stats.get(i)).write(stats);

		XMLWriter xw = new XMLWriter(
			new OutputStreamWriter(new FileOutputStream(file), "UTF-8"),
			OutputFormat.createPrettyPrint()
			);
		xw.write(doc);
		xw.close();
		
		m_file = file;
	}
	
	private void read(File file) throws IOException
	{
		try
		{
			SAXReader sr = new SAXReader();
			Document doc = sr.read(
				new InputStreamReader(new FileInputStream(file), "UTF-8")
				);
			
			Element root = doc.getRootElement();
			m_languageNames[0] = root.attributeValue("languageName0");
			m_languageNames[1] = root.attributeValue("languageName1");
			m_description = root.attributeValue("description");
			for(Iterator iRoot = root.elementIterator(); iRoot.hasNext(); )
			{
				Element el = (Element)iRoot.next();
				if(el.getName()=="words")
				{
					for(Iterator iEl = el.elementIterator(); iEl.hasNext(); )
						m_words.add(new Word(this, (Element)iEl.next()));
				}
				else if(el.getName()=="stats")
				{
					for(Iterator iEl = el.elementIterator(); iEl.hasNext(); )
						m_stats.add(new Stat((Element)iEl.next()));
				}
			}
			
			m_file = file;
		}
		catch(DocumentException ex)
		{
			throw new IOException(ex.getMessage());
		}
	}
	
	public void swapLanguages()
	{
		String buf;
		buf = m_languageNames[0];
		m_languageNames[0] = m_languageNames[1];
		m_languageNames[1] = buf;
		
		for(Iterator i = m_words.iterator(); i.hasNext();)
		{
			Word w = (Word)i.next();
			w.swapLanguages();
		}
	}
}
