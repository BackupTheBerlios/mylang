package mylang.data;

import java.io.*;
import java.util.*;

/*
 * DictionarySet.java
 *
 * Created on 25 pa¼dziernik 2003, 16:43
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
public class DictionarySet implements WordsContainer
{
	private ArrayList m_dictionaries;
	private ArrayList m_words;
	
	/** Creates a new instance of DictionarySet */
	public DictionarySet()
	{
		m_dictionaries = new ArrayList();
		m_words = new ArrayList();
	}
	
	public java.util.ArrayList getWordsList()
	{
		return m_words;
	}
	
	public ArrayList getDictionaries()
	{
		return m_dictionaries;
	}
	
	public void loadDictionary(File file) throws IOException
	{
		for(Iterator i = m_dictionaries.iterator(); i.hasNext();)
		{
			Dictionary dict = (Dictionary)i.next();
			if(dict.getFile().getAbsoluteFile().compareTo(file.getAbsoluteFile()) == 0)
				throw new IOException("File is already loaded");
		}
		
		Dictionary dict = new Dictionary(file);
		if(m_dictionaries.size() > 0)
		{
			String[] so = ((Dictionary)m_dictionaries.get(0)).getLanguageNames();
			String[] sn = dict.getLanguageNames();
			if( (so[0].trim().compareToIgnoreCase(sn[0].trim()) == 0) &&
			(so[1].trim().compareToIgnoreCase(sn[1].trim()) == 0) )
			{
			}
			else if( (so[0].trim().compareToIgnoreCase(sn[1].trim()) == 0) &&
			(so[1].trim().compareToIgnoreCase(sn[0].trim()) == 0) )
			{
				dict.swapLanguages();
			}
			else
				throw new IOException("File contains other languages than files already loaded");
		}
		m_dictionaries.add(dict);
		m_words.addAll(dict.getWordsList());
	}
	
	public void unloadDictionary(Dictionary dict)
	{
		m_words.removeAll(dict.getWordsList());
		m_dictionaries.remove(dict);
	}
}
