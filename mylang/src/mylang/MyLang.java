package mylang;

import mylang.gui.*;
import com.jgoodies.plaf.plastic.*;
import javax.swing.*;
import java.util.prefs.*;

/*
 * MyLang.java
 *
 * Created on 10 pa¼dziernik 2003, 19:43
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
public class MyLang
{
	private static final String PROGRAMVERSION = "1.0RC1";
	private static final String PREF_REQUIREALLTRANSLATIONS = "Require All Translations";
	private static final String PREF_EMPHASIZEMISTAKES = "Emphasize Mistakes";
	private static final String PREF_DICTIONARIESPATH = "Dictionaries Path";
	private static final String PREF_LASTVERSIONRUNNING = "Last Version Running";
	
	private static Preferences m_prefs;
	
	public static boolean getPrefRequireAllTranslations()
	{
		return m_prefs.getBoolean(PREF_REQUIREALLTRANSLATIONS, true);
	}
	public static void setPrefRequireAllTranslations(boolean value)
	{
		m_prefs.putBoolean(PREF_REQUIREALLTRANSLATIONS, value);
	}
	
	public static boolean getPrefEmphasizeMistakes()
	{
		return m_prefs.getBoolean(PREF_EMPHASIZEMISTAKES, true);
	}
	public static void setPrefEmphasizeMistakes(boolean value)
	{
		m_prefs.putBoolean(PREF_EMPHASIZEMISTAKES, value);
	}
	
	public static String getPrefDictionariesPath()
	{
		return m_prefs.get(PREF_DICTIONARIESPATH, "");
	}
	public static void setPrefDictionariesPath(String value)
	{
		m_prefs.put(PREF_DICTIONARIESPATH, value);
	}

	public static String getPrefLastVersionRunning()
	{
		return m_prefs.get(PREF_LASTVERSIONRUNNING, "none");
	}
	public static void setPrefLastVersionRunning(String value)
	{
		m_prefs.put(PREF_LASTVERSIONRUNNING, value);
	}

	/** Creates a new instance of MyLang */
	//public MyLang()
	//{
	//}
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		try
		{
			UIManager.setLookAndFeel(new PlasticLookAndFeel());
		}
		catch(UnsupportedLookAndFeelException ex)
		{
		}
		
		m_prefs = Preferences.userRoot().node("MyLang");
		new FrameMain().show();
		if(!getPrefLastVersionRunning().equals(PROGRAMVERSION))
		{
			FrameAbout.showAboutFrame();
			setPrefLastVersionRunning(PROGRAMVERSION);
		}
	}
	
}
