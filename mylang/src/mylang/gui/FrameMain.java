package mylang.gui;

import mylang.MyLang;
import mylang.data.*;
import mylang.gui.models.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

/*
 * FrameMain.java
 *
 * Created on 10 październik 2003, 19:50
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
public class FrameMain extends javax.swing.JFrame
{
	
	private ArrayList m_editors;
	private mylang.data.DictionarySet m_dset;
	
	/** Creates new form FrameMain */
	public FrameMain()
	{
		initComponents();
		
		m_tableDictionaries.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_tableWords.getColumnModel().getColumn(m_tableWords.convertColumnIndexToView(0)).setHeaderValue("Language 1");
		m_tableWords.getColumnModel().getColumn(m_tableWords.convertColumnIndexToView(1)).setHeaderValue("Language 2");
		m_tableWords.getTableHeader().resizeAndRepaint();
		
		m_editors = new ArrayList();
		m_dset = new DictionarySet();
		((DictionarySetTableModel)m_tableDictionaries.getModel()).setDictionarySet(m_dset);
		updateSessionControls();
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	private void initComponents()//GEN-BEGIN:initComponents
	{
		java.awt.GridBagConstraints gridBagConstraints;
		
		m_filechooserDictionary = new javax.swing.JFileChooser();
		jPanel1 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		m_tableDictionaries = new javax.swing.JTable();
		m_buttonLoad = new javax.swing.JButton();
		m_buttonUnload = new javax.swing.JButton();
		m_buttonInfo = new javax.swing.JButton();
		jPanel3 = new javax.swing.JPanel();
		jPanel2 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		m_tableWords = new javax.swing.JTable();
		m_buttonEnable = new javax.swing.JButton();
		m_buttonDisable = new javax.swing.JButton();
		jPanel5 = new javax.swing.JPanel();
		m_checkboxLanguage1 = new javax.swing.JCheckBox();
		m_checkboxLanguage2 = new javax.swing.JCheckBox();
		jPanel4 = new javax.swing.JPanel();
		jSeparator2 = new javax.swing.JSeparator();
		m_panelSessionControls = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		m_comboMode = new javax.swing.JComboBox();
		m_comboDirection = new javax.swing.JComboBox();
		m_buttonStart = new javax.swing.JButton();
		jMenuBar1 = new javax.swing.JMenuBar();
		jMenu1 = new javax.swing.JMenu();
		m_menuFileNew = new javax.swing.JMenuItem();
		m_menuFileOpen = new javax.swing.JMenuItem();
		m_menuFileSave = new javax.swing.JMenuItem();
		m_menuFileSaveas = new javax.swing.JMenuItem();
		jSeparator1 = new javax.swing.JSeparator();
		m_menuFileQuit = new javax.swing.JMenuItem();
		jMenu2 = new javax.swing.JMenu();
		m_menuToolsNeweditor = new javax.swing.JMenuItem();
		jSeparator3 = new javax.swing.JSeparator();
		j_menuToolsPreferences = new javax.swing.JMenuItem();
		jMenu3 = new javax.swing.JMenu();
		m_menuHelpAbout = new javax.swing.JMenuItem();
		
		m_filechooserDictionary.setDialogTitle("Load dictionary");
		m_filechooserDictionary.setMultiSelectionEnabled(true);
		
		getContentPane().setLayout(new java.awt.GridBagLayout());
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setTitle("MyLang - Main Window");
		addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent evt)
			{
				exitForm(evt);
			}
		});
		
		jPanel1.setLayout(new java.awt.GridBagLayout());
		
		jPanel1.setBorder(new javax.swing.border.TitledBorder("Dictionaries"));
		jScrollPane1.setPreferredSize(new java.awt.Dimension(453, 150));
		m_tableDictionaries.setModel(new DictionarySetTableModel());
		m_tableDictionaries.setShowHorizontalLines(false);
		m_tableDictionaries.setShowVerticalLines(false);
		jScrollPane1.setViewportView(m_tableDictionaries);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridheight = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		jPanel1.add(jScrollPane1, gridBagConstraints);
		
		m_buttonLoad.setText("Load...");
		m_buttonLoad.setToolTipText("<HTML>Loads dictionary file(s).</HTML>");
		m_buttonLoad.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				m_buttonLoadActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		jPanel1.add(m_buttonLoad, gridBagConstraints);
		
		m_buttonUnload.setText("Unload");
		m_buttonUnload.setToolTipText("Unloads selected dictionaries.");
		m_buttonUnload.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				m_buttonUnloadActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		jPanel1.add(m_buttonUnload, gridBagConstraints);
		
		m_buttonInfo.setText("Info...");
		m_buttonInfo.setToolTipText("Displays additional information about selected dictionary.");
		m_buttonInfo.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				m_buttonInfoActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		jPanel1.add(m_buttonInfo, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
		gridBagConstraints.weighty = 1.0;
		jPanel1.add(jPanel3, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		getContentPane().add(jPanel1, gridBagConstraints);
		
		jPanel2.setLayout(new java.awt.GridBagLayout());
		
		jPanel2.setBorder(new javax.swing.border.TitledBorder("Words"));
		jScrollPane2.setPreferredSize(new java.awt.Dimension(453, 200));
		m_tableWords.setModel(new WordsListTableModel());
		m_tableWords.setShowHorizontalLines(false);
		m_tableWords.setShowVerticalLines(false);
		jScrollPane2.setViewportView(m_tableWords);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridheight = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		jPanel2.add(jScrollPane2, gridBagConstraints);
		
		m_buttonEnable.setText("Enable");
		m_buttonEnable.setToolTipText("Enables selected word(s).");
		m_buttonEnable.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				m_buttonEnableActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		jPanel2.add(m_buttonEnable, gridBagConstraints);
		
		m_buttonDisable.setText("Disable");
		m_buttonDisable.setToolTipText("Disables selected word(s).");
		m_buttonDisable.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				m_buttonDisableActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		jPanel2.add(m_buttonDisable, gridBagConstraints);
		
		jPanel5.setLayout(new java.awt.GridBagLayout());
		
		jPanel5.setBorder(new javax.swing.border.TitledBorder("Shadow"));
		m_checkboxLanguage1.setText("Language 1");
		m_checkboxLanguage1.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				m_checkboxLanguage1ActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		jPanel5.add(m_checkboxLanguage1, gridBagConstraints);
		
		m_checkboxLanguage2.setText("Language 2");
		m_checkboxLanguage2.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				m_checkboxLanguage2ActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		jPanel5.add(m_checkboxLanguage2, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		jPanel2.add(jPanel5, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 1.0;
		jPanel2.add(jPanel4, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		getContentPane().add(jPanel2, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(4, 2, 4, 2);
		getContentPane().add(jSeparator2, gridBagConstraints);
		
		m_panelSessionControls.setLayout(new java.awt.GridBagLayout());
		
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel1.setText("Start session:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		m_panelSessionControls.add(jLabel1, gridBagConstraints);
		
		m_comboMode.setModel(new javax.swing.DefaultComboBoxModel(new String[]
		{ "Teach", "Test" }));
		m_comboMode.setToolTipText("Switches session mode. See documentation for details.");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		m_panelSessionControls.add(m_comboMode, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		m_panelSessionControls.add(m_comboDirection, gridBagConstraints);
		
		m_buttonStart.setText("START");
		m_buttonStart.setToolTipText("Begins the session.");
		m_buttonStart.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				m_buttonStartActionPerformed(evt);
			}
		});
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		m_panelSessionControls.add(m_buttonStart, gridBagConstraints);
		
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
		getContentPane().add(m_panelSessionControls, gridBagConstraints);
		
		jMenu1.setText("File");
		m_menuFileNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
		m_menuFileNew.setText("New");
		jMenu1.add(m_menuFileNew);
		
		m_menuFileOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
		m_menuFileOpen.setText("Open...");
		jMenu1.add(m_menuFileOpen);
		
		m_menuFileSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
		m_menuFileSave.setText("Save");
		jMenu1.add(m_menuFileSave);
		
		m_menuFileSaveas.setText("Save as...");
		jMenu1.add(m_menuFileSaveas);
		
		jMenu1.add(jSeparator1);
		
		m_menuFileQuit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
		m_menuFileQuit.setText("Quit");
		m_menuFileQuit.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				m_menuFileQuitActionPerformed(evt);
			}
		});
		
		jMenu1.add(m_menuFileQuit);
		
		jMenuBar1.add(jMenu1);
		
		jMenu2.setText("Tools");
		m_menuToolsNeweditor.setText("Open dictionary editor...");
		m_menuToolsNeweditor.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				m_menuToolsNeweditorActionPerformed(evt);
			}
		});
		
		jMenu2.add(m_menuToolsNeweditor);
		
		jMenu2.add(jSeparator3);
		
		j_menuToolsPreferences.setText("Preferences...");
		j_menuToolsPreferences.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				j_menuToolsPreferencesActionPerformed(evt);
			}
		});
		
		jMenu2.add(j_menuToolsPreferences);
		
		jMenuBar1.add(jMenu2);
		
		jMenu3.setText("Help");
		m_menuHelpAbout.setText("About...");
		m_menuHelpAbout.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				m_menuHelpAboutActionPerformed(evt);
			}
		});
		
		jMenu3.add(m_menuHelpAbout);
		
		jMenuBar1.add(jMenu3);
		
		setJMenuBar(jMenuBar1);
		
		pack();
	}//GEN-END:initComponents

	private void m_checkboxLanguage2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_m_checkboxLanguage2ActionPerformed
	{//GEN-HEADEREND:event_m_checkboxLanguage2ActionPerformed
		((WordsListTableModel)m_tableWords.getModel()).shadowColumn(1,
		m_checkboxLanguage2.getSelectedObjects() != null);
	}//GEN-LAST:event_m_checkboxLanguage2ActionPerformed

	private void m_checkboxLanguage1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_m_checkboxLanguage1ActionPerformed
	{//GEN-HEADEREND:event_m_checkboxLanguage1ActionPerformed
		((WordsListTableModel)m_tableWords.getModel()).shadowColumn(0,
		m_checkboxLanguage1.getSelectedObjects() != null);
	}//GEN-LAST:event_m_checkboxLanguage1ActionPerformed

	private void m_buttonInfoActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_m_buttonInfoActionPerformed
	{//GEN-HEADEREND:event_m_buttonInfoActionPerformed
		if(m_tableDictionaries.getSelectedRow() == -1)
			return;
		FrameDictionaryInfo fdi = new FrameDictionaryInfo(
		(mylang.data.Dictionary)m_dset.getDictionaries().get(m_tableDictionaries.getSelectedRow())
		);
		fdi.show();
	}//GEN-LAST:event_m_buttonInfoActionPerformed

	private void m_menuFileQuitActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_m_menuFileQuitActionPerformed
	{//GEN-HEADEREND:event_m_menuFileQuitActionPerformed
		quit();
	}//GEN-LAST:event_m_menuFileQuitActionPerformed

	private void j_menuToolsPreferencesActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_j_menuToolsPreferencesActionPerformed
	{//GEN-HEADEREND:event_j_menuToolsPreferencesActionPerformed
		DialogPreferences dp = new DialogPreferences(this, true);
		dp.show();
	}//GEN-LAST:event_j_menuToolsPreferencesActionPerformed

	private void m_menuHelpAboutActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_m_menuHelpAboutActionPerformed
	{//GEN-HEADEREND:event_m_menuHelpAboutActionPerformed
		FrameAbout.showAboutFrame();
	}//GEN-LAST:event_m_menuHelpAboutActionPerformed

	private void m_buttonStartActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_m_buttonStartActionPerformed
	{//GEN-HEADEREND:event_m_buttonStartActionPerformed
		beginSession();
	}//GEN-LAST:event_m_buttonStartActionPerformed

	private void m_buttonDisableActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_m_buttonDisableActionPerformed
	{//GEN-HEADEREND:event_m_buttonDisableActionPerformed
		int[] rows = m_tableWords.getSelectedRows();
		for(int i = 0; i < rows.length; i++)
		{
			Word w = (Word)m_dset.getWordsList().get(rows[i]);
			w.setEnabled(false);
		}
		((WordsListTableModel)m_tableWords.getModel()).setWordsContainer(m_dset);
		for(int i = 0; i < rows.length; i++)
			m_tableWords.addRowSelectionInterval(rows[i], rows[i]);
	}//GEN-LAST:event_m_buttonDisableActionPerformed

	private void m_buttonEnableActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_m_buttonEnableActionPerformed
	{//GEN-HEADEREND:event_m_buttonEnableActionPerformed
		int[] rows = m_tableWords.getSelectedRows();
		for(int i = 0; i < rows.length; i++)
		{
			Word w = (Word)m_dset.getWordsList().get(rows[i]);
			w.setEnabled(true);
		}
		((WordsListTableModel)m_tableWords.getModel()).setWordsContainer(m_dset);
		for(int i = 0; i < rows.length; i++)
			m_tableWords.addRowSelectionInterval(rows[i], rows[i]);
	}//GEN-LAST:event_m_buttonEnableActionPerformed

	private void m_buttonUnloadActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_m_buttonUnloadActionPerformed
	{//GEN-HEADEREND:event_m_buttonUnloadActionPerformed
		int[] rows = m_tableDictionaries.getSelectedRows();
		mylang.data.Dictionary[] dicts = new mylang.data.Dictionary[rows.length];
		for(int i = 0; i < rows.length; i++)
			dicts[i] = (mylang.data.Dictionary)m_dset.getDictionaries().get(rows[i]);
		for(int i = 0; i < dicts.length; i++)
			m_dset.unloadDictionary(dicts[i]);
		((DictionarySetTableModel)m_tableDictionaries.getModel()).setDictionarySet(m_dset);
		((WordsListTableModel)m_tableWords.getModel()).setWordsContainer(m_dset);
		updateWordsTableHeaders();
		updateSessionControls();
	}//GEN-LAST:event_m_buttonUnloadActionPerformed

	private void m_buttonLoadActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_m_buttonLoadActionPerformed
	{//GEN-HEADEREND:event_m_buttonLoadActionPerformed
		m_filechooserDictionary.setCurrentDirectory(
		new File(MyLang.getPrefDictionariesPath()));
		if(m_filechooserDictionary.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			MyLang.setPrefDictionariesPath(m_filechooserDictionary
			.getSelectedFiles()[0].getAbsolutePath());
			loadDictionaries(m_filechooserDictionary.getSelectedFiles());
		}
	}//GEN-LAST:event_m_buttonLoadActionPerformed

	private void m_menuToolsNeweditorActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_m_menuToolsNeweditorActionPerformed
	{//GEN-HEADEREND:event_m_menuToolsNeweditorActionPerformed
		openNewEditor();
	}//GEN-LAST:event_m_menuToolsNeweditorActionPerformed
	
	/** Exit the Application */
	private void exitForm(java.awt.event.WindowEvent evt)//GEN-FIRST:event_exitForm
	{
		quit();
	}//GEN-LAST:event_exitForm
		
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JLabel jLabel1;
	private javax.swing.JMenu jMenu1;
	private javax.swing.JMenu jMenu2;
	private javax.swing.JMenu jMenu3;
	private javax.swing.JMenuBar jMenuBar1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JSeparator jSeparator1;
	private javax.swing.JSeparator jSeparator2;
	private javax.swing.JSeparator jSeparator3;
	private javax.swing.JMenuItem j_menuToolsPreferences;
	private javax.swing.JButton m_buttonDisable;
	private javax.swing.JButton m_buttonEnable;
	private javax.swing.JButton m_buttonInfo;
	private javax.swing.JButton m_buttonLoad;
	private javax.swing.JButton m_buttonStart;
	private javax.swing.JButton m_buttonUnload;
	private javax.swing.JCheckBox m_checkboxLanguage1;
	private javax.swing.JCheckBox m_checkboxLanguage2;
	private javax.swing.JComboBox m_comboDirection;
	private javax.swing.JComboBox m_comboMode;
	private javax.swing.JFileChooser m_filechooserDictionary;
	private javax.swing.JMenuItem m_menuFileNew;
	private javax.swing.JMenuItem m_menuFileOpen;
	private javax.swing.JMenuItem m_menuFileQuit;
	private javax.swing.JMenuItem m_menuFileSave;
	private javax.swing.JMenuItem m_menuFileSaveas;
	private javax.swing.JMenuItem m_menuHelpAbout;
	private javax.swing.JMenuItem m_menuToolsNeweditor;
	private javax.swing.JPanel m_panelSessionControls;
	private javax.swing.JTable m_tableDictionaries;
	private javax.swing.JTable m_tableWords;
	// End of variables declaration//GEN-END:variables
	
	private void openNewEditor()
	{
		FrameEditor fe = new FrameEditor();
		fe.addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e)
			{
				m_editors.remove(e.getSource());
			}
		});
		m_editors.add(fe);
		fe.show();
	}
	
	private void loadDictionaries(File[] files)
	{
		for(int i = 0; i < files.length; i++)
		{
			try
			{
			((DictionarySetTableModel)m_tableDictionaries.getModel())
				.addDictionary(files[i]);
			}
			catch(IOException ex)
			{
				JOptionPane.showMessageDialog(this, 
					"Failed to load file \"" + files[i].getName() + "\"\n\n"
					+ ex.getMessage(), 
					"Error", 
					JOptionPane.ERROR_MESSAGE);
			}
		}
		((WordsListTableModel)m_tableWords.getModel())
			.setWordsContainer(m_dset);
		updateWordsTableHeaders();
		updateSessionControls();
	}
	
	private void beginSession()
	{
		if(m_editors.size() > 0)
		{
			JOptionPane.showMessageDialog(this, "Please close all editors first.",
			"Message", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		boolean nothingIsEnabled = true;
		for(Iterator i = m_dset.getWordsList().iterator(); i.hasNext();)
		{
			Word w = (Word)i.next();
			if(w.getEnabled())
			{
				nothingIsEnabled = false;
				break;
			}
		}
		if(nothingIsEnabled)
		{
			JOptionPane.showMessageDialog(this, "Please enable some words first.",
			"Message", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		
		DialogSession ds = new DialogSession(this, true);
		int mode;
		switch(m_comboMode.getSelectedIndex())
		{
			case 0: mode = DialogSession.TEACH_MODE; break;
			case 1: mode = DialogSession.TEST_MODE; break;
			default: return;
		}
		ds.prepareSession(mode, m_comboDirection.getSelectedIndex(), m_dset);
		ds.show();
	}
	
	private void updateSessionControls()
	{
		m_comboDirection.removeAllItems();
		if(m_dset.getWordsList().size() > 0)
		{
			String[] ln = ((mylang.data.Dictionary)m_dset.getDictionaries().get(0))
			.getLanguageNames();
			
			m_comboDirection.addItem(ln[0] + " -> " + ln[1]);
			m_comboDirection.addItem(ln[1] + " -> " + ln[0]);
			m_comboMode.setEnabled(true);
			m_comboDirection.setEnabled(true);
			m_buttonStart.setEnabled(true);
		}
		else
		{
			m_comboMode.setEnabled(false);
			m_comboDirection.setEnabled(false);
			m_buttonStart.setEnabled(false);
		}
	}
	
	private void quit()
	{
		if(m_editors.size() > 0)
		{
			if(JOptionPane.showConfirmDialog(this,
			"Some editors are still open. Do you want to quit anyway?",
			"Question", JOptionPane.YES_NO_OPTION, 
			JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
				return;
		}
		System.exit(0);
	}
	
	private void updateWordsTableHeaders()
	{
		if(m_dset.getDictionaries().size() == 0)
		{
			m_tableWords.getColumnModel().getColumn(m_tableWords
			.convertColumnIndexToView(0)).setHeaderValue("Language 1");
			m_tableWords.getColumnModel().getColumn(m_tableWords
			.convertColumnIndexToView(1)).setHeaderValue("Language 2");
			m_checkboxLanguage1.setText("Language 1");
			m_checkboxLanguage2.setText("Language 2");
		}
		else
		{
			m_tableWords.getColumnModel().getColumn(m_tableWords
			.convertColumnIndexToView(0)).setHeaderValue(((mylang.data.Dictionary)m_dset
			.getDictionaries().get(0)).getLanguageNames()[0]);
			m_tableWords.getColumnModel().getColumn(m_tableWords
			.convertColumnIndexToView(1)).setHeaderValue(((mylang.data.Dictionary)m_dset
			.getDictionaries().get(0)).getLanguageNames()[1]);
			m_checkboxLanguage1.setText(((mylang.data.Dictionary)m_dset
			.getDictionaries().get(0)).getLanguageNames()[0]);
			m_checkboxLanguage2.setText(((mylang.data.Dictionary)m_dset
			.getDictionaries().get(0)).getLanguageNames()[1]);
		}
		m_tableWords.getTableHeader().resizeAndRepaint();
	}
}
