package tools;

import javax.swing.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

/**
** Console
*/
public class Console
{
	public void show()
	{
		frame.setVisible( true );
	}
	
	public void hide()
	{
		frame.setVisible( false );
	}
	
	private final InputMediator inputMediator;
	private final OutputMediator outputMediator;
	private final JFrame frame;
	
	public InputStream getInputStream()
	{
		return inputMediator.getInputStream();
	}
	
	public PrintStream getOutputStream()
	{
		return outputMediator.getPrintStream();
	}
		
	
	public Console() throws IOException
	{	
		Exception e = new Exception();
		StackTraceElement[] ste = e.getStackTrace();
		StackTraceElement stack_frame = ste[ste.length-1];
		String class_s = stack_frame.getClassName();

		frame = new JFrame( "Application Console 0.3 - " + class_s );
		frame.setDefaultLookAndFeelDecorated( true );
		frame.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
		frame.setBounds( 100, 600, 320, 160);
		JTextArea area = new JTextArea();
		area.setEditable( false );
		JScrollPane scroller = new JScrollPane( area );

		JTextField field = new JTextField();
		
		outputMediator = new OutputMediator( area, scroller );
		inputMediator = new InputMediator( field, outputMediator.getPrintStream() );
		
		Container cp = frame.getContentPane();
		cp.add( scroller, BorderLayout.CENTER );
		cp.add( field, BorderLayout.SOUTH );
		
		frame.setVisible( true );
	}
	
	public static void main( String[] args ) throws IOException
	{
		Console con = new Console();
		System.setIn( con.getInputStream() );
		System.setOut( con.getOutputStream() );
		System.setErr( con.getOutputStream() );
		
		for (int i=0; i<20; i++ )
			System.out.println( i + " qwerty");

		BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ),1 );
			
		String line;
			String r ="";
			for (int i=0; i<20; i++ )
				r += " qwerty";

		while ( (line = reader.readLine())  != null)
		{
				System.out.println( r );
			System.err.println( line );
		}
	}
	

	class InputMediator implements ActionListener
	{
		private final PipedInputStream in;
		private final PrintStream writer;
		
		private final JTextField field;
		
		private final PrintStream history;
		
	//	private final JTextArea area;
		
		InputMediator( JTextField _field, PrintStream _history ) throws IOException
		{
			field = _field;
			history = _history;
			
			PipedOutputStream pos = new PipedOutputStream( );
			in = new  PipedInputStream( pos );
	//		pos.connect( in );
			writer = new PrintStream( pos, true );
			
			field.addActionListener( this );
		}
	
		public void actionPerformed( ActionEvent e )
		{
		
			String text = field.getText();
			field.setText( "" );
			writer.println( text );
			history.println( text );
			
		}
		
		InputStream getInputStream()
		{
			return in;
		}
	}
	
	class OutputMediator implements Runnable
	{
		private final JTextArea area;
		private final JScrollPane scroller;
		private final JScrollBar hscroller, vscroller;
		
		private final PrintStream writer;
		private final BufferedReader reader;
		
		OutputMediator( JTextArea _area, JScrollPane _scroller  ) throws IOException
		{
			scroller = _scroller;
			area = _area;
			
			vscroller = scroller.getVerticalScrollBar();
			hscroller = scroller.getHorizontalScrollBar();
			
			PipedOutputStream pos = new PipedOutputStream( );
			PipedInputStream pis = new  PipedInputStream( pos );
	//		pos.connect( pis );
			writer = new PrintStream( pos, true );
			reader = new BufferedReader( new InputStreamReader( pis ),1 );
			
			new Thread( this ).start();
		}
		
		public void run()
		{
			try
			{
				while (true)
				{
					String line = reader.readLine();
					area.append( line + "\n" );
	
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
		
		PrintStream getPrintStream()
		{
			return writer;
		}
	}



}

