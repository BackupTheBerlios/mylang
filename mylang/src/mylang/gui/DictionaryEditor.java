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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JComponent;

import mylang.data.Word;
import mylang.data.WordsContainer;

/**
 * @author herrmic
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DictionaryEditor extends JComponent implements WordsContainer {

	private ArrayList m_wordsList = new ArrayList();

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

		this.enableEvents(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
		this.setFont(new Font("Monospaced", Font.PLAIN, 12));
		this.setFocusTraversalKeysEnabled(false);
	}

	private Dimension m_preferredDimension = new Dimension(300, 200);

	public Dimension getPreferredSize() {
		return m_preferredDimension;
	}

	private class Position {

		public static final int NO_CHARACTER = -1;

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
	}

	private boolean isWholeCellSelected(int column, int row) {
		if (!m_multiSelectionActive) return false;
		if (Math.min(m_caretPosition.row, m_selectionOrigin.row) <= row
				&& row <= Math.max(m_caretPosition.row, m_selectionOrigin.row))
				return true;
		return false;
	}

	private void paintCell(Graphics2D g2, Rectangle rect, int column, int row) {
		String content = ((Word) m_wordsList.get(row)).getLanguage(column);
		if (isWholeCellSelected(column, row)) {
			// Draw whole cell as selected
			g2.setColor(Color.BLUE);
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
				g2.setColor(Color.BLUE);
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

				System.out.println(preContent + "," + selContent + ","
						+ postContent);

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
	}

	private Stroke m_gridStroke = new BasicStroke(1, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER, 10, new float[] { 1, 2 }, 0);

	private Stroke m_defaultStroke = new BasicStroke(1);

	private int m_cellTopMargin = 2;

	private int m_cellBottomMargin = 2;

	private int m_cellLeftMargin = 2;

	private Position m_caretPosition = new Position(0, 0, 0);

	private Position m_selectionOrigin = null;

	private int m_desiredCaretCharacter = 0;

	private boolean m_multiSelectionActive = false;

	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.WHITE);
		g2.fillRect(0, 0, this.getWidth(), this.getHeight());

		int fontHeight = g2.getFontMetrics().getHeight();
		int cellHeight = m_cellTopMargin + fontHeight + m_cellBottomMargin;
		int[] cellWidth = new int[2];
		cellWidth[0] = (this.getWidth() - 1) / 2;
		cellWidth[1] = (this.getWidth() - 1) - cellWidth[0];

		int n = 0;
		Rectangle rect = new Rectangle();
		for (Iterator it = m_wordsList.iterator(); it.hasNext();) {
			Word w = (Word) it.next();
			int y = n * (cellHeight + 1);

			// Draw both cells in current row
			rect.setBounds(0, y, cellWidth[0], cellHeight);
			paintCell(g2, rect, 0, n);
			rect.setBounds(cellWidth[0] + 1, y, cellWidth[1], cellHeight);
			paintCell(g2, rect, 1, n);

			// Draw cells' borders
			g2.setStroke(m_gridStroke);
			g2.setColor(Color.LIGHT_GRAY);
			g2.drawLine(0, y + cellHeight, cellWidth[0] - 1, y + cellHeight);
			g2.drawLine(cellWidth[0], y, cellWidth[0], y + cellHeight);
			g2.drawLine(cellWidth[0] + 1, y + cellHeight, cellWidth[0] + 1
					+ cellWidth[1], y + cellHeight);
			g2.setStroke(m_defaultStroke);

			++n;
		}
	}

	public void processKeyEvent(KeyEvent e) {
		if (e.getID() == KeyEvent.KEY_TYPED) {
			if (!Character.isISOControl(e.getKeyChar())) {
				if (!m_multiSelectionActive) {
					if (m_selectionOrigin != null) removeSelectedText();

					// Get word pointed by caret
					Word w = (Word) m_wordsList.get(m_caretPosition.row);

					// Insert typed character
					String oldTranslation = w
							.getLanguage(m_caretPosition.column);
					w.setLanguage(m_caretPosition.column, oldTranslation
							.substring(0, m_caretPosition.character)
							+ e.getKeyChar()
							+ oldTranslation
									.substring(m_caretPosition.character));
					++m_caretPosition.character;
					m_desiredCaretCharacter = m_caretPosition.character;
				}
			}
		} else if (e.getID() == KeyEvent.KEY_PRESSED) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
			case KeyEvent.VK_RIGHT:
			case KeyEvent.VK_UP:
			case KeyEvent.VK_DOWN:
			case KeyEvent.VK_HOME:
			case KeyEvent.VK_END:
				moveCaretByKey(e);
				break;
			case KeyEvent.VK_TAB:
				if (m_multiSelectionActive) break;
				m_selectionOrigin = null;
				m_multiSelectionActive = false;
				if (!e.isShiftDown()) {
					if (m_caretPosition.column == 0) {
						m_caretPosition.column = 1;
						m_caretPosition.character = 0;
					} else {
						if (m_caretPosition.row < m_wordsList.size() - 1) {
							++m_caretPosition.row;
							m_caretPosition.column = 0;
							m_caretPosition.character = 0;
						}
					}
				} else {
					if (m_caretPosition.column == 0) {
						if (m_caretPosition.row > 0) {
							--m_caretPosition.row;
							m_caretPosition.column = 1;
							m_caretPosition.character = 0;
						}
					} else {
						m_caretPosition.column = 0;
						m_caretPosition.character = 0;
					}
				}
				m_desiredCaretCharacter = m_caretPosition.character;
				break;
			case KeyEvent.VK_BACK_SPACE: {
				if (m_multiSelectionActive) break;

				if (m_selectionOrigin == null) {
					if (m_caretPosition.character > 0) {
						// Get word pointed by caret
						Word w = (Word) m_wordsList.get(m_caretPosition.row);

						// Insert typed character
						String oldTranslation = w
								.getLanguage(m_caretPosition.column);
						w.setLanguage(m_caretPosition.column, oldTranslation
								.substring(0, m_caretPosition.character - 1)
								+ oldTranslation
										.substring(m_caretPosition.character));
						--m_caretPosition.character;
						m_desiredCaretCharacter = m_caretPosition.character;
					}
				} else {
					removeSelectedText();
				}
				break;
			}
			case KeyEvent.VK_DELETE: {
				if (m_multiSelectionActive) break;

				// Get word pointed by caret
				Word w = (Word) m_wordsList.get(m_caretPosition.row);
				String oldTranslation = w.getLanguage(m_caretPosition.column);
				if (m_selectionOrigin == null) {
					if (m_caretPosition.character < oldTranslation.length()) {
						// Insert typed character
						w
								.setLanguage(
										m_caretPosition.column,
										oldTranslation.substring(0,
												m_caretPosition.character)
												+ oldTranslation
														.substring(m_caretPosition.character + 1));
					}
				} else {
					removeSelectedText();
				}
				break;
			}
			}
		}
		// Redraw the editor
		this.invalidate();
		this.repaint();
	}

	private void moveCaretByKey(KeyEvent e) {
		Word currentWord = (Word) m_wordsList.get(m_caretPosition.row);
		String currentTranslation = currentWord
				.getLanguage(m_caretPosition.column);
		Position oldPos = new Position(m_caretPosition);

		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if (m_multiSelectionActive && e.isShiftDown()) break;
			if (m_caretPosition.character > 0) {
				--m_caretPosition.character;
			} else {
				if (e.isShiftDown()) {
					m_multiSelectionActive = true;
				} else {
					if (m_caretPosition.column == 1) {
						m_caretPosition.column = 0;
						m_caretPosition.character = currentWord.getLanguage(0)
								.length();
					} else if (m_caretPosition.column == 0) {
						if (m_caretPosition.row > 0) {
							--m_caretPosition.row;
							m_caretPosition.column = 1;
							m_caretPosition.character = ((Word) m_wordsList
									.get(m_caretPosition.row)).getLanguage(
									m_caretPosition.column).length();
						}
					}
				}
			}
			m_desiredCaretCharacter = m_caretPosition.character;
			break;
		case KeyEvent.VK_RIGHT:
			if (m_multiSelectionActive && e.isShiftDown()) break;
			if (m_caretPosition.character < currentTranslation.length()) {
				++m_caretPosition.character;
			} else {
				if (e.isShiftDown()) {
					m_multiSelectionActive = true;
				} else {
					if (m_caretPosition.column == 0) {
						m_caretPosition.column = 1;
						m_caretPosition.character = 0;
					} else if (m_caretPosition.column == 1) {
						if (m_caretPosition.row < m_wordsList.size() - 1) {
							++m_caretPosition.row;
							m_caretPosition.column = 0;
							m_caretPosition.character = 0;
						}
					}
				}
			}
			m_desiredCaretCharacter = m_caretPosition.character;
			break;
		case KeyEvent.VK_UP:
			if (e.isShiftDown() && !m_multiSelectionActive) {
				m_multiSelectionActive = true;
				break;
			}
			if (m_caretPosition.row > 0) {
				--m_caretPosition.row;
				int len = ((Word) m_wordsList.get(m_caretPosition.row))
						.getLanguage(m_caretPosition.column).length();
				if (m_desiredCaretCharacter > len) {
					m_caretPosition.character = len;
				} else {
					m_caretPosition.character = m_desiredCaretCharacter;
				}
			}
			break;
		case KeyEvent.VK_DOWN:
			if (e.isShiftDown() && !m_multiSelectionActive) {
				m_multiSelectionActive = true;
				break;
			}
			if (m_caretPosition.row < m_wordsList.size() - 1) {
				++m_caretPosition.row;
				int len = ((Word) m_wordsList.get(m_caretPosition.row))
						.getLanguage(m_caretPosition.column).length();
				if (m_desiredCaretCharacter > len) {
					m_caretPosition.character = len;
				} else {
					m_caretPosition.character = m_desiredCaretCharacter;
				}
			}
			break;
		case KeyEvent.VK_HOME:
			if (m_multiSelectionActive && e.isShiftDown()) break;
			m_caretPosition.character = 0;
			m_desiredCaretCharacter = 0;
			break;
		case KeyEvent.VK_END:
			if (m_multiSelectionActive && e.isShiftDown()) break;
			int len = currentTranslation.length();
			m_caretPosition.character = len;
			m_desiredCaretCharacter = len;
			break;
		}
		
		if (m_selectionOrigin != null && !e.isShiftDown()) {
			m_selectionOrigin = null;
			m_multiSelectionActive = false;
		} else if (m_selectionOrigin == null && e.isShiftDown()) {
			m_selectionOrigin = oldPos;
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
		m_selectionOrigin = null;
		m_caretPosition.character = leftSel;
		m_desiredCaretCharacter = leftSel;
	}

	protected void processMouseEvent(MouseEvent e) {
		this.grabFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mylang.data.WordsContainer#getWordsList()
	 */
	public ArrayList getWordsList() {
		return m_wordsList;
	}
}