/*
 * Created on 2004-06-19
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mylang.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mylang.data.WordsContainer;

class WordsListSelection implements WordsContainer, Transferable, Serializable {

	public static final DataFlavor g_wordsListFlavor = new DataFlavor(WordsListSelection.class, null);

	public ArrayList m_wordsList;
	
	public WordsListSelection(List wordsList) {
		m_wordsList = new ArrayList(wordsList);
	}

	public ArrayList getWordsList() {
		return this.m_wordsList;
	}

	DataFlavor[] m_supportedFlavors = new DataFlavor[] { g_wordsListFlavor };
	
	public DataFlavor[] getTransferDataFlavors() {
		return m_supportedFlavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if (flavor.equals(g_wordsListFlavor))
			return true;
		return false;
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
		if (flavor.equals(g_wordsListFlavor)) {
			return this;
		} else
			throw new UnsupportedFlavorException(flavor);
	}	
}
