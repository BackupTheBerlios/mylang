/*
 * WindowSplash.java
 *
 * Created on 8 luty 2004, 21:57
 */

package mylang.gui;

import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author  herrmic
 */
public class WindowSplash extends Window
{
	
	private Image m_splashImage;
		
	private static boolean m_paintCalled = false;
		
	/** Creates a new instance of WindowSplash */
	public WindowSplash(Frame owner, Image splashImage)
	{
		super(owner);
		m_splashImage = splashImage;
		
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(m_splashImage, 0);
		try
		{
			mt.waitForID(0);
		}
		catch(InterruptedException ex)
		{
		}
		int imgWidth = m_splashImage.getWidth(this);
		int imgHeight = m_splashImage.getHeight(this);
		setSize(imgWidth, imgHeight);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - imgWidth) / 2, (screenSize.height - imgHeight) / 2);
	}
	
	public void update(Graphics g)
	{
		g.setColor(getForeground());
		paint(g);
	}
	
	public void paint(Graphics g)
	{
		g.drawImage(m_splashImage, 0, 0, this);
		if(!m_paintCalled)
		{
			m_paintCalled = true;
			synchronized(this)
			{
				notifyAll();
			}
		}
	}
	
	public static Frame splash(Image splashImage)
	{
		Frame f = new Frame();
		WindowSplash w = new WindowSplash(f, splashImage);
		w.toFront();
		w.show();
		if(!EventQueue.isDispatchThread())
		{
			synchronized(w)
			{
				while(!m_paintCalled)
				{
					try
					{
						w.wait();
					}
					catch(InterruptedException ex)
					{
					}
				}
			}
		}
		return f;
	}
}
