/*
 * Created on 2004-06-12
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mylang.gui;

import java.awt.AWTEvent;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.RepaintManager;
import javax.swing.Scrollable;
import javax.swing.UIManager;

import mylang.data.Word;
import mylang.data.WordsContainer;

/**
 * @author herrmic
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DictionaryEditor extends JComponent implements WordsContainer,
		Scrollable {

	/***************************************************************************
	 * Data
	 */

	private ArrayList m_wordsList = new ArrayList();

	public ArrayList getWordsList() {
		return m_wordsList;
	}

	private String getTranslation(int column, int row) {
		return ((Word) m_wordsList.get(row)).getLanguage(column);
	}

	/***************************************************************************
	 * Appearance and layout
	 */

	private Dimension m_preferredSize = new Dimension(600, 300);

	private Color m_selectionBackground = new Color(10, 36, 106);

	private Stroke m_defaultStroke = new BasicStroke(1);

	private Stroke m_gridStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER, 10, new float[] { 1, 2 }, 0);

	public Dimension getPreferredSize() {
		return new Dimension(this.getWidth(), m_wordsList.size()
				* (this.getRowHeight(0) + 1));
	}

	private void recalcSize() {
		this.setSize(this.getPreferredSize());
	}

	/***************************************************************************
	 * Constructor
	 */

	public DictionaryEditor() {
		Word w = new Word(null);
		w.setLanguage(0, "alfa");
		w.setLanguage(1, "beta");
		m_wordsList.add(w);

		w = new Word(null);
		w.setLanguage(0, "gamma");
		w.setLanguage(1, "delta");
		m_wordsList.add(w);

		w = new Word(null);
		w.setLanguage(0, "eta");
		w.setLanguage(1, "teta");
		m_wordsList.add(w);

		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK
				| AWTEvent.MOUSE_MOTION_EVENT_MASK);

		//this.setFont(new Font("Monospaced", Font.PLAIN, 13));
		//this.setFont(UIManager.getFont("TableHeader.font"));

		this.setFocusTraversalKeysEnabled(false);
		this.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

		this.recalcSize();
	}

	/***************************************************************************
	 * Caret location and selection range
	 */

	private class Position {

		public static final int NO_CHARACTER = -2;

		public int column;

		public int row;

		public int character;

		public Position(int col, int r, int ch) {
			this.column = col;
			this.row = r;
			this.character = ch;
		}

		public Position(Position pos) {
			this(pos.column, pos.row, pos.character);
		}

		public boolean equals(Object obj) {
			if (obj instanceof Position) {
				Position pos = (Position) obj;
				if (pos.column == this.column && pos.row == this.row
						&& pos.character == this.character) return true;
			}
			return false;
		}

		public boolean sameCell(Position pos) {
			return column == pos.column && row == pos.row;
		}

		public Position previousCharacterInTable() {
			if (character > 0) {
				return new Position(column, row, character - 1);
			} else {
				if (column == 1) {
					return new Position(0, row, getTranslation(0, row).length());
				} else {
					if (row > 0) {
						return new Position(1, row - 1, getTranslation(1,
								row - 1).length());
					} else {
						return new Position(this);
					}
				}
			}
		}

		public Position nextCharacterInTable() {
			if (character < getTranslation(column, row).length()) {
				return new Position(column, row, character + 1);
			} else {
				if (column == 0) {
					return new Position(1, row, 0);
				} else {
					if (row < m_wordsList.size() - 1) {
						return new Position(0, row + 1, 0);
					} else {
						return new Position(this);
					}
				}
			}
		}

		public Position previousWordInCell() {
			if (character > 0) {
				String currentTranslation = getTranslation(column, row);
				int ch;
				for (ch = character - 1; ch > 0; --ch) {
					if (isWordSeperator(currentTranslation.charAt(ch - 1))
							&& !isWordSeperator(currentTranslation.charAt(ch)))
							break;
				}
				return new Position(column, row, ch);
			} else {
				return new Position(this);
			}
		}

		public Position previousWordInTable() {
			if (character > 0) {
				return this.previousWordInCell();
			} else {
				return this.previousCharacterInTable();
			}
		}

		public Position nextWordInCell() {
			if (character < getTranslation(column, row).length()) {
				String currentTranslation = getTranslation(column, row);
				int ch;
				for (ch = character + 1; ch < currentTranslation
						.length(); ++ch) {
					if (isWordSeperator(currentTranslation.charAt(ch - 1))
							&& !isWordSeperator(currentTranslation.charAt(ch)))
							break;
				}
				return new Position(column, row, ch);
			} else {
				return new Position(this);
			}
		}

		public Position nextWordInTable() {
			if (character < getTranslation(column, row).length()) {
				return this.nextWordInCell();
			} else
				return this.nextCharacterInTable();
		}
	}

	private Position m_caretPosition = new Position(0, 0, 0);

	private Position m_selectionOrigin = null;

	private int m_desiredCaretCharacter = 0;

	private boolean m_multiSelectionActive = false;

	private boolean isWholeCellSelected(int column, int row) {
		if (!m_multiSelectionActive) return false;
		if (Math.min(m_caretPosition.row, m_selectionOrigin.row) <= row
				&& row <= Math.max(m_caretPosition.row, m_selectionOrigin.row))
				return true;
		return false;
	}

	/***************************************************************************
	 * Drawing
	 */

	FontRenderContext m_fontRenderContext = null;

	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		if (m_fontHeight == -1) {
			m_fontHeight = g2.getFontMetrics().getHeight();
			m_fontRenderContext = g2.getFontRenderContext();
			recalcSize();
		}

		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());

		Rectangle clip = g2.getClipBounds();
		if (clip == null)
				clip = new Rectangle(0, 0, this.getWidth(), this.getHeight());

		Position posMin = this.findCellAt(clip.x, clip.y);
		Position posMax = this.findCellAt(clip.x + clip.width, clip.y
				+ clip.height);
		for (int row = posMin.row; row <= posMax.row; ++row) {
			for (int col = posMin.column; col <= posMax.column; ++col) {
				paintCell(g2, col, row);
			}
		}
	}

	private void paintCell(Graphics2D g2, int column, int row) {
		//System.out.println("" + column + ", " + row);
		Rectangle rect = this.getCellRectangle(column, row);
		String content = ((Word) m_wordsList.get(row)).getLanguage(column);
		if (isWholeCellSelected(column, row)) {
			// Draw whole cell as selected
			g2.setColor(m_selectionBackground);
			g2.fillRect(rect.x, rect.y, rect.width, rect.height);

			g2.setColor(Color.WHITE);
			g2.drawString(content, rect.x + m_cellLeftMargin, rect.y
					+ rect.height - g2.getFontMetrics().getDescent()
					- m_cellBottomMargin - 1);
		} else {
			if (m_selectionOrigin != null && m_selectionOrigin.column == column
					&& m_selectionOrigin.row == row
					&& m_caretPosition.column == column
					&& m_caretPosition.row == row
					&& !m_selectionOrigin.equals(m_caretPosition)) {
				// Draw text as selected

				int leftSel = Math.min(m_selectionOrigin.character,
						m_caretPosition.character);
				int lengthSel = Math.max(m_selectionOrigin.character,
						m_caretPosition.character)
						- leftSel;

				String preContent = content.substring(0, leftSel);
				g2.setColor(Color.BLACK);
				g2.drawString(preContent, rect.x + m_cellLeftMargin, rect.y
						+ rect.height - g2.getFontMetrics().getDescent()
						- m_cellBottomMargin - 1);
				Rectangle2D rectPreContent = g2.getFont().getStringBounds(
						preContent, g2.getFontRenderContext());

				String selContent = content.substring(leftSel, leftSel
						+ lengthSel);
				Rectangle2D rectSelContent = g2.getFont().getStringBounds(
						selContent, g2.getFontRenderContext());
				g2.setColor(m_selectionBackground);
				g2.fillRect(rect.x + m_cellLeftMargin
						+ (int) rectPreContent.getWidth(), rect.y + rect.height
						- g2.getFontMetrics().getHeight() - m_cellBottomMargin
						- 1, (int) rectSelContent.getWidth(), g2
						.getFontMetrics().getHeight());
				g2.setColor(Color.WHITE);
				g2.drawString(selContent, rect.x + m_cellLeftMargin
						+ (int) rectPreContent.getWidth(), rect.y + rect.height
						- g2.getFontMetrics().getDescent() - m_cellBottomMargin
						- 1);

				String postContent = content.substring(leftSel + lengthSel);
				g2.setColor(Color.BLACK);
				g2.drawString(postContent, rect.x + m_cellLeftMargin
						+ (int) rectPreContent.getWidth()
						+ (int) rectSelContent.getWidth(), rect.y + rect.height
						- g2.getFontMetrics().getDescent() - m_cellBottomMargin
						- 1);
			} else {
				// Draw text as plain
				g2.setColor(Color.WHITE);
				g2.fillRect(rect.x, rect.y, rect.width, rect.height);

				g2.setColor(Color.BLACK);
				g2.drawString(content, rect.x + m_cellLeftMargin, rect.y
						+ rect.height - g2.getFontMetrics().getDescent()
						- m_cellBottomMargin - 1);
			}
			if (row == m_caretPosition.row && column == m_caretPosition.column) {
				g2.setColor(Color.BLACK);
				Rectangle2D r2 = g2.getFont().getStringBounds(content, 0,
						m_caretPosition.character, g2.getFontRenderContext());
				g2.fillRect(rect.x + (int) r2.getWidth() + 1, rect.y
						+ rect.height - g2.getFontMetrics().getHeight()
						- m_cellBottomMargin - 1, 2, g2.getFontMetrics()
						.getHeight());
			}
		}
		g2.setStroke(m_gridStroke);
		g2.setColor(Color.LIGHT_GRAY);
		g2.drawLine(rect.x, rect.y + rect.height, rect.x + rect.width, rect.y
				+ rect.height);
		if (column == 0) {
			g2.drawLine(rect.x + rect.width, rect.y, rect.x + rect.width,
					rect.y + rect.height);
		}
		g2.setStroke(m_defaultStroke);
	}

	/***************************************************************************
	 * Event handling
	 */

	protected void processKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_TYPED) {
			if (!Character.isISOControl(e.getKeyChar())) {
				putText(String.valueOf(e.getKeyChar()));
			}
		} else if (e.getID() == KeyEvent.KEY_PRESSED) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_UP:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_HOME:
			case KeyEvent.VK_END:
			case KeyEvent.VK_PAGE_UP:
			case KeyEvent.VK_PAGE_DOWN:
				moveCaretByKey(e);
				break;
			case KeyEvent.VK_TAB:
				if (m_multiSelectionActive) break;
				if (m_caretPosition.column == 0)
					moveCaretTo(new Position(1, m_caretPosition.row, 0), false,
							false);
				else
					moveCaretTo(new Position(0, m_caretPosition.row, 0), false,
							false);
				break;
			case KeyEvent.VK_BACK_SPACE: {
				if (m_multiSelectionActive) break;

				if (e.isControlDown()) {
					if (m_selectionOrigin == null) {
						moveCaretTo(m_caretPosition.previousWordInCell(), true,
								false);
						removeSelectedText();
					}
				} else {
					if (m_selectionOrigin == null) {
						if (m_caretPosition.character > 0) {
							Word w = (Word) m_wordsList
									.get(m_caretPosition.row);

							String oldTranslation = w
									.getLanguage(m_caretPosition.column);
							w
									.setLanguage(
											m_caretPosition.column,
											oldTranslation
													.substring(
															0,
															m_caretPosition.character - 1)
													+ oldTranslation
															.substring(m_caretPosition.character));
							moveCaretTo(new Position(m_caretPosition.column,
									m_caretPosition.row,
									m_caretPosition.character - 1), false,
									false);
						}
					} else {
						removeSelectedText();
					}
				}
				break;
			}
			case KeyEvent.VK_DELETE: {
				if (e.isControlDown()) {
					if (m_selectionOrigin == null) {
						moveCaretTo(new Position(m_caretPosition
								.nextWordInCell()), true, false);
						removeSelectedText();
					}
				} else {
					if (m_multiSelectionActive) {
						removeSelectedRows();
					} else {
						Word w = (Word) m_wordsList.get(m_caretPosition.row);
						String oldTranslation = w
								.getLanguage(m_caretPosition.column);
						if (m_selectionOrigin == null) {
							if (m_caretPosition.character < oldTranslation
									.length()) {
								w
										.setLanguage(
												m_caretPosition.column,
												oldTranslation
														.substring(
																0,
																m_caretPosition.character)
														+ oldTranslation
																.substring(m_caretPosition.character + 1));
								this.repaint(this
										.getCellRectangle(m_caretPosition));
							}
						} else {
							removeSelectedText();
						}
					}
				}
				break;
			}
			case KeyEvent.VK_ENTER: {
				if (m_selectionOrigin != null || e.isShiftDown()) break;

				Position oldPos = new Position(m_caretPosition);

				Word w = new Word(null);
				if (m_caretPosition.column == 0
						&& m_caretPosition.character == 0) {
					m_wordsList.add(m_caretPosition.row, w);
				} else {
					m_wordsList.add(m_caretPosition.row + 1, w);
				}
				this.recalcSize();
				moveCaretTo(new Position(0, m_caretPosition.row + 1, 0), false,
						false);

				Rectangle rect = this.getCellRectangle(oldPos);
				this.repaint(0, rect.y, this.getWidth(), this.getHeight()
						- rect.y);
				break;
			}
			case KeyEvent.VK_V:
				if (e.isControlDown() && !e.isAltDown()) this.paste();
				break;
			case KeyEvent.VK_C:
				if (e.isControlDown() && !e.isAltDown()) this.copy();
				break;
			case KeyEvent.VK_X:
				if (e.isControlDown() && !e.isAltDown()) this.cut();
				break;
			}
		}
	}

	private boolean m_mouseSelectsWords = false;
	
	private Position m_mouseSelectionOrigin = null;
	
	protected void processMouseEvent(MouseEvent e) {
		if (e.getID() == MouseEvent.MOUSE_PRESSED) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				if (e.getClickCount() == 1) {
					this.moveCaretTo(this.findCellAt(e.getPoint()),
							e.isShiftDown(), false);
					m_mouseSelectsWords = false;
				} else if (e.getClickCount() == 2) {
					Position clickPos = this.findCellAt(e.getPoint());
					m_mouseSelectionOrigin = clickPos;
					this.moveCaretTo(clickPos.previousWordInCell(),
							false, false);
					this.moveCaretTo(clickPos.nextWordInCell(), true, false);
					m_mouseSelectsWords = true;
				}
			}
		}
		this.grabFocus();
	}

	protected void processMouseMotionEvent(MouseEvent e) {
		if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
			if ((e.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) != 0) {
				if (m_mouseSelectsWords) {
					Position mousePos = this.findCellAt(e.getPoint());
					if (mousePos.character < m_selectionOrigin.character) {
						if (!m_multiSelectionActive)
							this.moveCaretTo(m_mouseSelectionOrigin.nextWordInCell(), false, false);
						this.moveCaretTo(mousePos.previousWordInCell(), true, false);
					} else {
						if (!m_multiSelectionActive)
							this.moveCaretTo(m_mouseSelectionOrigin.previousWordInCell(), false, false);
						this.moveCaretTo(mousePos.nextWordInCell(), true, false);
					}
				} else {
					this.moveCaretTo(this.findCellAt(e.getPoint()), true, false);
				}
			}
		}
	}

	/***************************************************************************
	 * Helper methods
	 */

	private static boolean isWordSeperator(char c) {
		if (Character.isWhitespace(c) || "/\"'()[],.:;|+{}\\".indexOf(c) != -1)
				return true;
		return false;
	}

	private void moveCaretTo(Position destination, boolean enableSelection,
			boolean delayMultiSelection) throws IndexOutOfBoundsException {
		if (enableSelection) {
			if (m_selectionOrigin == null) m_selectionOrigin = m_caretPosition;
			if (!m_caretPosition.sameCell(destination)) {
				if (!m_multiSelectionActive && delayMultiSelection) {
					destination = m_caretPosition;
				}
				m_multiSelectionActive = true;
			}
		} else {
			if (m_multiSelectionActive) {
				int minRow = Math.min(m_selectionOrigin.row,
						m_caretPosition.row);
				int maxRow = Math.max(m_selectionOrigin.row,
						m_caretPosition.row);
				int minY = this.getCellRectangle(0, minRow).y;
				Rectangle maxRect = this.getCellRectangle(0, maxRow);
				int maxY = maxRect.y + maxRect.height;
				this.repaint(0, minY, this.getWidth(), maxY - minY);
			}
			m_selectionOrigin = null;
			m_multiSelectionActive = false;
		}

		if (m_multiSelectionActive) {
			int minRow = Math.min(destination.row, m_caretPosition.row);
			int maxRow = Math.max(destination.row, m_caretPosition.row);
			int minY = this.getCellRectangle(0, minRow).y;
			Rectangle maxRect = this.getCellRectangle(0, maxRow);
			int maxY = maxRect.y + maxRect.height;
			this.repaint(0, minY, this.getWidth(), maxY - minY);
			m_caretPosition = destination;
			m_desiredCaretCharacter = m_caretPosition.character;
		} else {
			this.repaint(this.getCellRectangle(m_caretPosition));
			this.repaint(this.getCellRectangle(destination));
			m_caretPosition = destination;
			m_desiredCaretCharacter = m_caretPosition.character;
		}

		this.scrollRectToVisible(getCellRectangle(m_caretPosition));
	}

	private void moveCaretByKey(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if (m_multiSelectionActive && e.isShiftDown()) break;
			if (e.isControlDown())
				moveCaretTo(m_caretPosition.previousWordInTable(), e
						.isShiftDown(), true);
			else
				moveCaretTo(m_caretPosition.previousCharacterInTable(), e
						.isShiftDown(), true);
			break;
		case KeyEvent.VK_RIGHT:
			if (m_multiSelectionActive && e.isShiftDown()) break;
			if (e.isControlDown())
				moveCaretTo(m_caretPosition.nextWordInTable(), e.isShiftDown(),
						true);
			else
				moveCaretTo(m_caretPosition.nextCharacterInTable(), e
						.isShiftDown(), true);
			break;
		case KeyEvent.VK_UP:
			if (m_caretPosition.row > 0) {
				int desired = m_desiredCaretCharacter;
				moveCaretTo(new Position(m_caretPosition.column,
						m_caretPosition.row - 1, Math.min(
								m_desiredCaretCharacter, getTranslation(
										m_caretPosition.column,
										m_caretPosition.row - 1).length())), e
						.isShiftDown(), true);
				m_desiredCaretCharacter = desired;
			}
			break;
		case KeyEvent.VK_DOWN:
			if (m_caretPosition.row < m_wordsList.size() - 1) {
				int desired = m_desiredCaretCharacter;
				moveCaretTo(new Position(m_caretPosition.column,
						m_caretPosition.row + 1, Math.min(
								m_desiredCaretCharacter, getTranslation(
										m_caretPosition.column,
										m_caretPosition.row + 1).length())), e
						.isShiftDown(), true);
				m_desiredCaretCharacter = desired;
			}
			break;
		case KeyEvent.VK_HOME:
			if (e.isControlDown()) {
				moveCaretTo(new Position(0, 0, 0), e.isShiftDown(), false);
			} else {
				if (m_caretPosition.character == 0 && m_caretPosition.column == 1) {
					moveCaretTo(new Position(0, m_caretPosition.row,
							getTranslation(0, m_caretPosition.row).length()), e
							.isShiftDown(), true);
				} else
					moveCaretTo(new Position(m_caretPosition.column,
							m_caretPosition.row, 0), e.isShiftDown(), true);
			}
			break;
		case KeyEvent.VK_END:
			if (e.isControlDown()) {
				moveCaretTo(new Position(1, m_wordsList.size() - 1, getTranslation(1, m_wordsList.size() - 1).length()), e.isShiftDown(), false);
			} else {
				if (m_caretPosition.character == getTranslation(
						m_caretPosition.column, m_caretPosition.row).length()
						&& m_caretPosition.column == 0) {
					moveCaretTo(new Position(1, m_caretPosition.row, 0), e
							.isShiftDown(), true);
				} else
					moveCaretTo(new Position(m_caretPosition.column,
							m_caretPosition.row, getTranslation(
									m_caretPosition.column, m_caretPosition.row)
									.length()), e.isShiftDown(), true);
			}
			break;
		case KeyEvent.VK_PAGE_UP: {
			int desired = m_desiredCaretCharacter;
			int newRow = Math.max(0, m_caretPosition.row - this.getVisibleRect().height / (this.getRowHeight(0) + 1) + 1);
			this.moveCaretTo(new Position(
					m_caretPosition.column, 
					newRow, 
					Math.min(
							m_desiredCaretCharacter, getTranslation(
									m_caretPosition.column,
									newRow).length())),
					e.isShiftDown(), false);
			m_desiredCaretCharacter = desired;
			break;
		}
		case KeyEvent.VK_PAGE_DOWN: {
			int desired = m_desiredCaretCharacter;
			int newRow = Math.min(m_wordsList.size() - 1,
					m_caretPosition.row + this.getVisibleRect().height / (this.getRowHeight(0) + 1) - 1);
			this.moveCaretTo(new Position(
				m_caretPosition.column, 
				newRow, 
				Math.min(
						m_desiredCaretCharacter, getTranslation(
								m_caretPosition.column,
								newRow).length())),
				e.isShiftDown(), false);
			m_desiredCaretCharacter = desired;
			break;
		}
		}
	}

	private void removeSelectedText() {
		Word w = (Word) m_wordsList.get(m_caretPosition.row);
		String oldTranslation = w.getLanguage(m_caretPosition.column);
		StringBuffer sb = new StringBuffer(oldTranslation);

		int leftSel = Math.min(m_caretPosition.character,
				m_selectionOrigin.character);
		sb.delete(leftSel, Math.max(m_caretPosition.character,
				m_selectionOrigin.character));
		w.setLanguage(m_caretPosition.column, sb.toString());
		moveCaretTo(new Position(m_caretPosition.column, m_caretPosition.row,
				leftSel), false, false);
	}

	private void putText(String text) {
		// No text edition is allowed if multiple cells are selected
		if (!m_multiSelectionActive) {
			// If there was any text selected it will get replaced
			// by this method
			if (m_selectionOrigin != null) removeSelectedText();

			// Find current word
			Word w = (Word) m_wordsList.get(m_caretPosition.row);

			// Insert the text
			String oldTranslation = w.getLanguage(m_caretPosition.column);
			w.setLanguage(m_caretPosition.column, oldTranslation.substring(0,
					m_caretPosition.character)
					+ text
					+ oldTranslation.substring(m_caretPosition.character));

			// Place caret after inserted text
			moveCaretTo(new Position(m_caretPosition.column,
					m_caretPosition.row, m_caretPosition.character
							+ text.length()), false, false);
		}
	}

	private void removeSelectedRows() {
		m_wordsList.subList(
				Math.min(m_caretPosition.row, m_selectionOrigin.row),
				Math.max(m_caretPosition.row, m_selectionOrigin.row) + 1)
				.clear();
		if (m_wordsList.size() == 0) {
			m_wordsList.add(0, new Word(null));
			moveCaretTo(new Position(0, 0, 0), false, false);
		} else {
			moveCaretTo(new Position(m_caretPosition.column, Math.min(Math.min(
					m_caretPosition.row, m_selectionOrigin.row), m_wordsList
					.size() - 1), 0), false, false);
		}
		Rectangle rect = this.getCellRectangle(m_caretPosition);
		this.repaint(0, rect.y, this.getWidth(), this.getHeight() - rect.y);
		this.recalcSize();
	}

	/***************************************************************************
	 * Clipboard support
	 */

	private void transferSelectionToClipboard(boolean removeSelected) {
		if (m_multiSelectionActive) {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(
					new WordsListSelection(m_wordsList.subList(Math.min(
							m_caretPosition.row, m_selectionOrigin.row),
							Math
									.max(m_caretPosition.row,
											m_selectionOrigin.row) + 1)), null);
			if (removeSelected) this.removeSelectedRows();
		} else {
			String text = ((Word) m_wordsList.get(m_caretPosition.row))
					.getLanguage(m_caretPosition.column);
			StringSelection ss = new StringSelection(text.substring(Math.min(
					m_caretPosition.character, m_selectionOrigin.character),
					Math.max(m_caretPosition.character,
							m_selectionOrigin.character)));
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss,
					null);
			if (removeSelected) this.removeSelectedText();
		}
	}

	public void cut() {
		if (m_selectionOrigin != null)
			this.transferSelectionToClipboard(true);
	}

	public void copy() {
		if (m_selectionOrigin != null)
			this.transferSelectionToClipboard(false);
	}

	public void paste() {
		if (!m_multiSelectionActive) {
			Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard()
					.getContents(null);
			try {
				if (t != null) {
					if (t
							.isDataFlavorSupported(WordsListSelection.g_wordsListFlavor)) {
						WordsListSelection sel = (WordsListSelection) t
								.getTransferData(WordsListSelection.g_wordsListFlavor);
						int firstRow;
						if (m_caretPosition.column == 0
								&& m_caretPosition.character == 0) {
							firstRow = m_caretPosition.row;
						} else {
							firstRow = m_caretPosition.row + 1;
						}
						m_wordsList.addAll(firstRow, sel.getWordsList());
						this.recalcSize();
						moveCaretTo(new Position(0, firstRow, 0), false, false);
						moveCaretTo(new Position(0, firstRow
								+ sel.getWordsList().size() - 1, this
								.getTranslation(
										0,
										firstRow + sel.getWordsList().size()
												- 1).length()), true, false);
						Rectangle rect = this.getCellRectangle(0, firstRow);
						this.repaint(0, rect.y, this.getWidth(), this
								.getHeight()
								- rect.y);
					} else if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
						this.putText((String) t
								.getTransferData(DataFlavor.stringFlavor));
					}
				}
			} catch (UnsupportedFlavorException ex) {
			} catch (IOException ex) {
			}
		}
	}

	/***************************************************************************
	 * Cell sizing and positioning
	 */

	private int m_cellTopMargin = 2;

	private int m_cellBottomMargin = 2;

	private int m_cellLeftMargin = 2;

	private int m_fontHeight = -1;

	private int getRowHeight(int row) {
		return m_cellTopMargin + m_fontHeight + m_cellBottomMargin;
	}

	private int getColumnWidth(int column) {
		return this.getWidth() / 2;
	}

	private Rectangle getCellRectangle(int column, int row) {
		return new Rectangle(column * (getColumnWidth(0) + 1), row
				* (getRowHeight(0) + 1), getColumnWidth(column),
				getRowHeight(row));
	}

	private Rectangle getCellRectangle(Position pos) {
		return this.getCellRectangle(pos.column, pos.row);
	}

	private Position findCellAt(int x, int y) {
		int column = Math.max(0, Math.min(1, x / (getColumnWidth(0) + 1)));
		int row = Math.max(0, Math.min(m_wordsList.size() - 1, y / (getRowHeight(0) + 1)));
		String translation = getTranslation(column, row) + " ";
		int textX = Math.max(0, x - m_cellLeftMargin - column
				* (getColumnWidth(0) + 1));
		int prevSize = 0;
		int clickCharacter;
		int i;
		for (i = 0; i < translation.length(); i++) {
			Rectangle2D rect = this.getFont().getStringBounds(translation, 0,
					i, m_fontRenderContext);
			if (prevSize <= textX && textX <= rect.getWidth()) {
				if (textX - prevSize < (rect.getWidth() - prevSize) / 2) {
					--i;
					break;
				} else {
					break;
				}
			}
			prevSize = (int) rect.getWidth();
		}
		clickCharacter = Math.min(i, translation.length() - 1);
		return new Position(column, row, clickCharacter);
	}

	private Position findCellAt(Point p) {
		return this.findCellAt(p.x, p.y);
	}

	/***************************************************************************
	 * Scrollable interface
	 */

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		return true;
	}

	public Dimension getPreferredScrollableViewportSize() {
		return this.getPreferredSize();
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return visibleRect.height
				- getScrollableUnitIncrement(visibleRect, orientation,
						direction);
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return this.getRowHeight(0) + 1;
	}
}