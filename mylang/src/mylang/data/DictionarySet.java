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
 * Used to group several dictionaries. All dictionaries must contain the same
 * languages.
 * @author herrmic
 */
public class DictionarySet implements WordsContainer
{
	private ArrayList m_dictionaries;
	private ArrayList m_words;
	
	/**
	 * Creates empty dictionary set.
	 */
	public DictionarySet()
	{
		m_dictionaries = new ArrayList();
		m_words = new ArrayList();
	}
	
	/**
	 * Gets list of all words contained within the set.
	 * @return <CODE>ArrayList</CODE> of all <CODE>Word</CODE>s that are contained in the set's
	 * dictionaries.
	 */	
	public java.util.ArrayList getWordsList()
	{
		return m_words;
	}
	
	/**
	 * Gets list of dictionaries that this set contains.
	 * @return <CODE>ArrayList</CODE> of all dictionaries contained within this set.
	 * <B>NOTE</B>: Do NOT add/remove dictionaries manually, use the
	 * [<CODE>un</CODE>]<CODE>loadDictionary()</CODE> methods.
	 */	
	public ArrayList getDictionaries()
	{
		return m_dictionaries;
	}
	
	/**
	 * Loads dictionary from the given file then adds it to the set. If the operation
	 * succeeds all new <CODE>Word</CODE>s are added to the set's words list.
	 * @param file File that contain the dictionary.
	 * @throws IOException thrown if the given dictionary can't be loaded.
	 */	
	public void loadDictionary(File file) throws IOException
	{
		for(Iterator i = m_dictionaries.iterator(); i.hasNext();)
		{
			Dictionary dict = (Dictionary)i.next();
			if(dict.getFile().getAbsoluteFile().compareTo(file.getAbsoluteFile()) == 0)
				throw new IOException("File is already loaded");
		}
		addDictionary(new Dictionary(file));
	}
	
	/**
	 * Adds the given dictionary to the set. Flips the language order if necessary.
	 * @param dict Dictionary to be added.
	 * @throws IOException Thrown if dictionary have not matching languages.
	 */	
	public void addDictionary(Dictionary dict) throws IOException
	{
		// If there are any languages in the set, the new one must contain
		// the same languages
		if(m_dictionaries.size() > 0)
		{
			// Compare the language names and see if they are: exactly the same,
			// same but reversed ordering, different
			String[] so = ((Dictionary)m_dictionaries.get(0)).getLanguageNames();
			String[] sn = dict.getLanguageNames();
			if( (so[0].trim().compareToIgnoreCase(sn[0].trim()) == 0) &&
			(so[1].trim().compareToIgnoreCase(sn[1].trim()) == 0) )
			{
				// Languages are exactly the same
			}
			else if( (so[0].trim().compareToIgnoreCase(sn[1].trim()) == 0) &&
			(so[1].trim().compareToIgnoreCase(sn[0].trim()) == 0) )
			{
				// Languages are the same but have different order: flip them
				dict.swapLanguages();
			}
			else
				throw new IOException("File contains other languages than files already loaded");
		}
		m_dictionaries.add(dict);
		m_words.addAll(dict.getWordsList());
	}
	
	/**
	 * Removes given dictionary from the set. All the dictionary's <CODE>Word</CODE>s
	 * are removed from the set's words list as well.
	 * @param dict dictionary to be removed from the set.
	 */	
	public void unloadDictionary(Dictionary dict)
	{
		m_words.removeAll(dict.getWordsList());
		m_dictionaries.remove(dict);
	}
}
