package tools;

import javax.swing.*;

import java.awt.event.*;

import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.BorderLayout;

import java.io.*;

/**
** Diagnostic Console
*/

public class DC
{
	public static final String VERSION = "0.5";
	
	public static DC shared = new DC();
	
	public static void log( boolean on, String msg )
	{
		if (on)	shared.ilog( msg );
	}

	public static void log( boolean on, Object o )
	{
		if (on)		shared.ilog( (o!=null) ? o.toString() : "null" );
	}

	public static void log( boolean on, Exception e )
	{
		if (on)		log( e );
		else
		{ shared.ilog( e.getMessage() );	}
	}

	public static void log( String s )
	{
		shared.ilog( s );
	}

	public static void log( Object o )
	{
		shared.ilog( (o!=null) ? o.toString() : "null" );
	}


	public static void log( Exception e )
	{
		StringWriter stringer = new StringWriter();
		PrintWriter writer = new PrintWriter( stringer, true );
		e.printStackTrace( writer );
		writer.flush();
		shared.ilog( e.getMessage() );
		shared.ilog( stringer.toString() );
	}

	public static void show()
	{
		shared.frame.setVisible( true );
	}
	
	public static void hide()
	{
		shared.frame.setVisible( false );
	}
	

	private JFrame frame;
	private JTextArea area;
	private JScrollPane scroller;
    private final JScrollBar hscroller, vscroller;
    
	private JToolBar toolbar;
	private AbstractAction clear_a = new AbstractAction( "Clear" )
	{
		public void actionPerformed( ActionEvent e )
		{
			area.setText( null );
		}
	};
	
	private DC()
	{
		Exception e = new Exception();
		StackTraceElement[] ste = e.getStackTrace();
		StackTraceElement stack_frame = ste[ste.length-1];
		String class_s = stack_frame.getClassName();
	
		frame = new JFrame( "Diagnostic Console " + VERSION + " - " + class_s );
		frame.setDefaultLookAndFeelDecorated( true );
		frame.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
		frame.setBounds( getBounds() );
		area = new JTextArea();
		area.setEditable( false );

		scroller = new JScrollPane( area );
		vscroller = scroller.getVerticalScrollBar();
		hscroller = scroller.getHorizontalScrollBar();

		toolbar = new JToolBar();
		toolbar.add( clear_a );
		JPanel cp = (JPanel) frame.getContentPane();
		cp.add( toolbar, BorderLayout.NORTH );
		cp.add( scroller, BorderLayout.CENTER );

		frame.setVisible( true );
	}
	
	protected void ilog( String msg )
	{
		final int stack_pos = 2;
		Exception e = new Exception();
		
		StackTraceElement[] ste = e.getStackTrace();
		StackTraceElement frame = ste[stack_pos];
		String class_s = frame.getClassName();
		String method_s = frame.getMethodName();
		int line = frame.getLineNumber();
		
		String cmsg = line + " " +  method_s + "()" + "@" +  class_s +  ": " + msg;
		slog( cmsg );
	}

	private void slog( String msg )
	{
		area.append( msg + "\n" );
		area.invalidate();
		SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				vscroller.setValue(vscroller.getMaximum());
				hscroller.setValue(hscroller.getMinimum());
			}
		});

	}
/*
	public static void main3( String[] args )
	{
		DC.shared.hide();
		for ( int i=0; i<100; ++i)
		{
			DC.shared.slog( "" + i );
			for ( int c=0; c<100; ++c)
				DC.shared.log( "hello " +  c );
			DC.shared.log( "\n" );
		}
		DC.shared.show();		
	}

	public static void main( String[] args ) throws Exception
	{
		String line = "";
		for ( int c=0; c<100; ++c)
			line += "hello";
		BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );

		String a;
		
		do
		{
			for ( int i=0; i<104; ++i)
			{
				DC.log( i%2==0, line );
			}
		}
		while ( (a = reader.readLine())  != null);

	}

	public static void main2( String[] args )
	{
		
		DC.log( true, "hello");
		DC.log( false, "hola" );
		DC.log( true, "mao" );
		DC.log( true, "jaj" );
	}
*/
	protected Rectangle getBounds()
	{
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension d = toolkit.getScreenSize();
		
		int step_x = d.width / 10;
		int istep_x = step_x/10;
		int step_y = d.height / 10;
		int istep_y = step_y/10;
		
		int x = (int) ( 5 * (step_x+istep_x) );
		int y = (int) ( 5 * (step_y+istep_y) );
		int width = 4*step_x;
		int height = 2*step_y;
		
		Rectangle r = new Rectangle( x, y, width, height );
		return r;
	}

	
}
