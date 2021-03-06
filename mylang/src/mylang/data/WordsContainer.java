package mylang.data;

import java.util.*;
/*
 * WordsContainer.java
 *
 * Created on 11 październik 2003, 19:46
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
 * Interface to be implemented by all object that provide their word list.
 * @author herrmic
 */
public interface WordsContainer
{
	/**
	 * Gets the object's words list.
	 * @return List of words that the object contains.
	 */	
	public ArrayList getWordsList();
}
