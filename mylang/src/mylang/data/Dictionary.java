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
 * Object of this class holds information about single dictionary. It manages list
 * of words, stats and other data.
 * @author herrmic
 */

public class Dictionary implements WordsContainer
{
	private ArrayList m_words;
	private String[] m_languageNames;
	private String m_description;
	private ArrayList m_stats;
	
	private File m_file;
	
	/**
	 * Creates an empty dictionary.
	 */
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
	
	/**
	 * Reads the dictionary from the given file. The <code>file</code> parameter must
	 * point valid XML dictionary file.
	 * @param file valid XML file created by <CODE>write()</CODE> method.
	 * @throws IOException if any error occurs during loading.
	 */	
	public Dictionary(File file) throws IOException
	{
		this();
		read(file);
	}
	
	/**
	 * Retrieves list of all words that this dictionary contains.
	 * @return <CODE>ArrayList</CODE> containing dictionary's words.
	 */	
	public ArrayList getWordsList()
	{
		return m_words;
	}
	
	/**
	 * Retrieves names of the languages this dictionary contains.
	 * @return <CODE>Array</CODE> of two <CODE>String</CODE>s with names of the languages.
	 */	
	public String[] getLanguageNames()
	{ 
		return m_languageNames; 
	}
	
	/**
	 * Retrieves description of the dictionary.
	 * @return <CODE>String</CODE> containing dictionary description.
	 */	
	public String getDescription()
	{ 
		return m_description; 
	}
	
	/**
	 * Sets new description of the dictionary.
	 * @param desc New description.
	 * @throws NullPointerException is thrown when null is passed as parameter.
	 */	
	public void  setDescription(String desc) throws NullPointerException
	{ 
		if(desc == null)
			throw new NullPointerException();
		
		m_description = desc; 
	}
	
	/**
	 * Gets list of all stats for this dictionary.
	 * @return <CODE>ArrayList</CODE> containing all <CODE>Stat</CODE>s.
	 */	
	public ArrayList getStats()
	{
		return m_stats; 
	}
	
	/**
	 * Gets the file associated with the dictionary.
	 * @return <CODE>File</CODE> object associated with this dictionary. For newly created
	 * dictionaries <CODE>null</CODE> is returned until successful call to the
	 * <CODE>write()</CODE> method.
	 */	
	public File getFile()
	{
		return m_file; 
	}
	
	/**
	 * Writes contents of the dictionary to the file. If the method succeeds, object's
	 * <CODE>File</CODE> property will be set to the value of the parameter.
	 * @param file <CODE>File</CODE> object that dictionary contents will be written to.
	 * @throws IOException is thrown when any error occurs during the process.
	 */	
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
	
	/**
	 * Flips the languages order in the dictionary (1&lt;-&gt;0).
	 * This method is needed when merging two dictionaries that have the same
	 * languages, but ordered differently.
	 */	
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
