/* 
 * Copyright (c) 2014, Roberto Capuano <roberto@2think.it>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package tools;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

/**
** Console
*/
public class DebuggeeConsole
{
	public static final String VERSION = "0.5";

	public void show()
	{
		frame.setVisible( true );
	}
	
	public void hide()
	{
		frame.setVisible( false );
	}
	
	private DownStreamMediator downStreamMediator;
	private UpStreamMediator upStreamMediator;
	private final JFrame frame;

	JTextArea area;
	JTextField field;
	JScrollPane scroller;
	
	public DebuggeeConsole(  ) 
	{	
		Exception e = new Exception();
		StackTraceElement[] ste = e.getStackTrace();
		StackTraceElement stack_frame = ste[ste.length-1];
		String class_s = stack_frame.getClassName();

		frame = new JFrame( "Debuggee Console " + VERSION);// + class_s );
		frame.setDefaultLookAndFeelDecorated( true );
		frame.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
		frame.setBounds( getBounds() );
		area = new JTextArea();
		area.setEditable( false );
		scroller = new JScrollPane( area );

		field = new JTextField();
		
		Container cp = frame.getContentPane();
		cp.add( scroller, BorderLayout.CENTER );
		cp.add( field, BorderLayout.SOUTH );
		
		frame.setVisible( false );
	}

	public void attach( InputStream downStream, OutputStream upStream ) throws IOException
	{
		downStreamMediator = new DownStreamMediator( area, scroller, downStream );
		upStreamMediator = new UpStreamMediator( field, downStreamMediator, upStream );
		frame.setVisible( true );
	}
	
	public static void main( String[] args ) throws IOException
	{
		DebuggeeConsole con = new DebuggeeConsole();
		con.attach( System.in, System.out);
	}
	
	class UpStreamMediator implements ActionListener
	{
		private final JTextField field;
		private final PrintWriter writer;
		private final DownStreamMediator downStreamMediator;
		
	//	private final JTextArea area;
		
		UpStreamMediator( JTextField _field, DownStreamMediator _downStreamMediator, OutputStream upStream ) throws IOException
		{
			field = _field;
			downStreamMediator = _downStreamMediator;
			writer = new PrintWriter( new OutputStreamWriter( upStream  ) );
			field.addActionListener( this );
		}
	
		public void actionPerformed( ActionEvent e )
		{
			String text = field.getText();
			field.setText( "" );
			downStreamMediator.println( text );
			writer.println( text );
			writer.flush();
		}
		
	}
	
	class DownStreamMediator implements Runnable
	{
		private final JTextArea area;
		private final JScrollPane scroller;
		private final JScrollBar hscroller, vscroller;
		
		private final BufferedReader reader;
		
		DownStreamMediator( JTextArea _area, JScrollPane _scroller, InputStream downStream ) throws IOException
		{
			scroller = _scroller;
			area = _area;
			
			vscroller = scroller.getVerticalScrollBar();
			hscroller = scroller.getHorizontalScrollBar();

			reader = new BufferedReader( new InputStreamReader( downStream ),1 );
			
			new Thread( this ).start();
		}
		
		public void println( String line )
		{
				synchronized( this ) {area.append( line + "\n" ); }
		}

		public void run()
		{
			try
			{
				while (true)
				{
					String line = reader.readLine();
					if (line==null)
						return;
					synchronized(this)	{ area.append( line + "\n" );}
	
					if (!reader.ready())
					{
						area.invalidate();
						SwingUtilities.invokeLater( new Runnable()
						{
							public void run()
							{
								vscroller.setValue(vscroller.getMaximum());
								hscroller.setValue(hscroller.getMinimum());
							}
						}
						);
					}
				}
			}
			catch( IOException ioe )
			{
				System.err.println( ioe );
			}		
		}
		
	}

	protected Rectangle getBounds()
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension d = toolkit.getScreenSize();
		
		int step_x = d.width / 10;
		int istep_x = step_x/10;
		int step_y = d.height / 10;
		int istep_y = step_y/10;
		
		int x = (int) ( 5 * (step_x+istep_x) );
		int y = (int) ( 7 * (step_y+istep_y) );
		int width = 4*step_x;
		int height = 2*step_y;
		
		Rectangle r = new Rectangle( x, y, width, height );
		return r;
	}


}

