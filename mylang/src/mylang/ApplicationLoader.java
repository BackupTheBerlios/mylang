/*
 * ApplicationLoader.java
 *
 * Created on 8 luty 2004, 21:51
 */

package mylang;

import java.awt.Frame;
import java.awt.Toolkit;
import java.net.URL;
import java.io.*;

/**
 *
 * @author  herrmic
 */
public class ApplicationLoader
{
	
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		Frame splashFrame = null;
		URL imageURL = ApplicationLoader.class.getResource("gui/resources/logoSplash.png");
		if(imageURL!=null)
		{
			splashFrame = mylang.gui.WindowSplash.splash(Toolkit.getDefaultToolkit().createImage(imageURL));
		}
		else
		{
			System.err.println("Can't load splash image.");
		}
		
		try
		{
			Class.forName("mylang.MyLang").getMethod("main", new Class[] {String[].class})
			.invoke(null, new Object[] {args});
		}
		catch(Throwable e)
		{
			e.printStackTrace();
			System.err.flush();
			System.exit(10);
		}
		if(splashFrame != null)
			splashFrame.dispose();
	}
	
}
