/*
 * Session.java
 *
 * Created on 16 styczeñ 2004, 20:57
 */

package mylang.data;

import mylang.MyLang;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Stores all information needed to perform a session. Manages questions, checks
 * correctness of answers, updates words and dictionaries stats, etc.
 * @author herrmic
 */
public class Session extends JComponent
{
	/** Indicates teach mode.
	 *  Teach session ends when all words are translated correctly.
	 */	
	public static final int TEACH_MODE = 1;
	/** Indicates test mode.
	 *  Test session ends when all words are translated once.
	 */	
	public static final int TEST_MODE = 2;
	/**
	 * <CODE>Calendar</CODE> object that stores the amount of time that has ellapsed
	 * since beginning of the session.
	 */	
	public static final String TIME_ELAPSED_PROPERTY = "SessionTimeElapsed";
	
	// This session mode
	private int m_mode;
	// Languages of questions and answers
	private int m_languageOfQuestion;
	private int m_languageOfAnswer;

	// Number of answers given
	private int m_answers;
	// How many of the answers were correct
	private int m_correct;
	// List of words that are yet to be translated
	private ArrayList m_wordsLeft;
	// List of words that were mistranslated
	private ArrayList m_wordsFailed;
	// Current question
	private Word m_question;

	// Set of dictionaries that are used for this session
	private DictionarySet m_dset;
	
	// For tracking the time
	private javax.swing.Timer m_timerElapsed;
	private Calendar m_timeElapsed;
	
	// Needed to ask questions in an unpredictable manner
	private Random m_random;

	/** Creates and prepares a new instance of Session.
	 * @param dset Dictionaries that will be used for the session.
	 * @param mode Mode of the session.
	 * @param loq In which language questions will be asked.
	 */
	public Session(DictionarySet dset, int mode, int loq)
	{
		// Check, if the object is not null
		if(dset == null)
			throw new NullPointerException("DictionarySet cannot be null");
		
		// Make sure, that the given mode is valid one
		if((mode != TEACH_MODE) && (mode != TEST_MODE))
			throw new IndexOutOfBoundsException("Unknown mode");
		
		// First check, if the given number is within valid range
		if(loq < 0 || loq > 1)
			throw new IndexOutOfBoundsException("Invalid language number");
		
		// Remember the parameter values
		m_dset = dset;
		m_mode = mode;
		m_languageOfQuestion = loq;
		m_languageOfAnswer = (loq == 0) ? 1 : 0;
		
		// Fill the wordsLeft list with all enabled words
		m_wordsLeft = new ArrayList();
		for(Iterator i = m_dset.getWordsList().iterator(); i.hasNext();)
		{
			Word w = (Word)i.next();
			if(w.getEnabled())
				m_wordsLeft.add(w);
		}
		
		// Empty the wordsFailed list
		m_wordsFailed = new ArrayList();
		
		// Clean all the variables
		m_answers = 0;
		m_correct = 0;
		m_question = null;
				
		// Prepare the RNG
		m_random = new Random();
		
		// Prepare the timer and associated variables
		m_timeElapsed = new GregorianCalendar();
		m_timeElapsed.set(Calendar.HOUR, 0);
		m_timeElapsed.set(Calendar.MINUTE, 0);
		m_timeElapsed.set(Calendar.SECOND, 0);
		m_timerElapsed = new javax.swing.Timer(1000, new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				m_timeElapsed.add(Calendar.SECOND, 1);
				firePropertyChange(
					TIME_ELAPSED_PROPERTY, null, m_timeElapsed);
			}
		});
	}
	
	/** Gets the associated DictionarySet.
	 * @return DictionarySet used by the object.
	 */
	public DictionarySet getDictionarySet()
	{
		return m_dset;
	}

	/** Gets the session's mode.
	 * @return Mode of the session.
	 */
	public int getMode()
	{
		return m_mode;
	}
	
	/**
	 * Gets the number of the language that is used to ask the questions.
	 * @return language of the questions.
	 */	
	public int getLanguageOfQuestion()
	{
		return m_languageOfQuestion;
	}
	
	/**
	 * Gets total number of answers during the session.
	 * @return number of answers that were given during this session.
	 */	
	public int getTotalAnswers()
	{
		return m_answers;
	}
	
	/**
	 * Amount of correct answers that were given during this session.
	 * @return Number of correct answers.
	 */	
	public int getCorrectAnswers()
	{
		return m_correct;
	}
	
	/**
	 * Gets minimal amount of questions that will be asked before the session can be
	 * finished. Usually each answer desreases this number. Exception is teach mode
	 * where only correct answers affect this number.
	 * @return Minimal number of questions that will be asked before the session finishes.
	 */	
	public int getNumberOfQuestionsLeft()
	{
		return m_wordsLeft.size();
	}
	
	/**
	 * The text of the current question.
	 * @return The text of the current question.
	 */	
	public String getCurrentQuestionText()
	{
		return m_question.getLanguage(m_languageOfQuestion);
	}
	
	/**
	 * The text of the expected (correct) answer.
	 * @return The text of the expected (correct) answer.
	 */	
	public String getExpectedAnswerText()
	{
		return m_question.getLanguage(m_languageOfAnswer);
	}

	/**
	 * Begins the session. Initializes session data and starts the time counter.
	 */
	public void start()
	{
		// Clear all stats
		m_answers = 0;
		m_correct = 0;
		
		// Start the timer
		m_timerElapsed.start();
	}

	/**
	 * Picks new question (uses random number generator).
	 * @return <CODE>true</CODE> if the question was picked successfully, <CODE>false</CODE> if
	 * there are no more questions left.
	 */	
	public boolean askNewQuestion()
	{
		// Check if there are any questions left
		if(m_wordsLeft.size() == 0)
		{
			// Session is finished
			return false;
		}
		else
		{
			// Select then ask next question
			int num = m_random.nextInt(m_wordsLeft.size());
			// Try not to repeat the current word
			if (m_wordsLeft.size() > 1) {
				if ((Word)m_wordsLeft.get(num) == m_question) {
					// The selected word repeated, go to next one
					num++;
					if (num == m_wordsLeft.size())
						num = 0;
				}
			}
			m_question = (Word)m_wordsLeft.get(num);
			
			return true;
		}
	}

	/**
	 * Processes the given answer and compares it to the expected one, then modifies
	 * appropriate counters. Depending on the session's mode and the correctness of the
	 * given answer, the question may be removed from the questions list.
	 * <B>IMPORTANT:</B> each call to this method must be preceded by <CODE>askNewQuestion()</CODE>
	 * call.
	 * @param answer User's answer.
	 * @throws NullPointerException Thrown when there is no question selected. Usually this mean that
	 * <CODE>askNewQuestion()</CODE> wasn't called before.
	 * @return <CODE>true</CODE> if the answer was correct, <CODE>false</CODE> otherwise.
	 */	
	public boolean processAnswer(String answer) throws NullPointerException
	{
		// First check, if some question was asked
		if(m_question == null)
			throw new NullPointerException("Question not asked");
			
		// Extract single words from user's and expected answers
		String[] userTab = answer.trim().split(";");
		String[] answerTab = m_question.getLanguage(m_languageOfAnswer).split(";");
		
		boolean correct = false;
		// Behavior of this method depends on user settings
		if(MyLang.getPrefRequireAllTranslations())
		{
			// Complete answer is required
			if(isArraySubsetOf(userTab, answerTab)
			&& isArraySubsetOf(answerTab, userTab))
				correct = true;
		}
		else
		{
			// Incomplete answer will suffice
			if(isArraySubsetOf(userTab, answerTab))
				correct = true;
		}

		// Answer correctness has been evaluated
		if(correct)
		{
			// Answer was correct
			m_wordsLeft.remove(m_question);
			m_correct++;
		}
		else
		{
			// Answer was wrong; in test mode we want the question to appear
			// again
			if(m_mode == TEST_MODE)
				m_wordsLeft.remove(m_question);
			m_wordsFailed.add(m_question);
		}
		
		// Increase answers counter then procees to a next question
		m_answers++;
		
		// Tell the caller if the answer was ok
		return correct;
	}
	
	private boolean isArraySubsetOf(String[] array, String[] set)
	{
		// For each string of the array check, if there is equal string in
		// the set
		boolean found;
		for(int i = 0; i < array.length; i++)
		{
			found = false;
			for(int j = 0; j < set.length; j++)
			{
				// Comparision is case-insensitive, this may change in the
				// future
				if(array[i].trim().compareToIgnoreCase(set[j].trim()) == 0)
				{
					found = true;
					break;
				}
			}
			if(!found)
				return false;
		}
		return true;
	}
	
	/**
	 * Ends the session, modifies stats of all involved dictionaries and words and
	 * saves the changed dictionaries back on the disk.
	 */	
	public void finalizeSession()
	{
		// Stop the timer
		m_timerElapsed.stop();
		
		// Clear stats of all enabled words, then increase score value by one
		// for each wrong answer
		for(Iterator i = m_dset.getWordsList().iterator(); i.hasNext();)
		{
			Word w = (Word)i.next();
			if(w.getEnabled())
				w.setLastStat(new Stat(0));
		}
		for(Iterator i = m_wordsFailed.iterator(); i.hasNext();)
		{
			Word w = (Word)i.next();
			w.setLastStat(new Stat(w.getLastStat().getScore() + 1));
		}
		
		// Generate new stat for dictionary
		for(Iterator i = m_dset.getDictionaries().iterator(); i.hasNext();)
		{
			mylang.data.Dictionary dict = (mylang.data.Dictionary)i.next();
			try
			{
				// Count how many of the failures are connected to each dictionary
				int failures;
				failures = 0;
				for(Iterator j = m_wordsFailed.iterator(); j.hasNext();)
				{
					Word w = (Word)j.next();
					if(dict.getWordsList().contains(w))
						failures++;
				}
				// Update dictionary stats and save changes to disk
				dict.getStats().add(new Stat(failures, new Integer(m_mode), m_timeElapsed));
				dict.write(dict.getFile());
			}
			catch(java.io.IOException ex)
			{
			}
		}
	}
}
