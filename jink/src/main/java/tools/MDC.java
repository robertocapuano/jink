package tools;

import javax.swing.*;

import java.io.*;

/**
** Diagnostic Console
*/

public class MDC
{
	public static MDC shared = new MDC();
	private PrintWriter out;
	
	public static void log( boolean on, String msg )
	{
		if (on)	shared.ilog( msg );
	}

	public static void log( boolean on, Object o )
	{
		if (on)	shared.ilog( o.toString() );
	}

	public static void log( String s )
	{
		shared.ilog( s );
	}

	public static void log( Object o )
	{
		shared.ilog( o.toString() );
	}

	public static void show()
	{
	}
	
	public static void hide()
	{
	}
	

	private MDC()
	{
		try
		{
			out = new PrintWriter( new FileWriter( "dc.log", true ) );
		}
		catch( IOException ioe )
		{
			System.err.println( ioe );
		}
	}
	
	private void ilog( String msg )
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
		out.println( msg );
		out.flush();
	}

	public static void main( String[] args ) throws Exception
	{
		String line = "";
		for ( int c=0; c<100; ++c)
			line += "hello";
		
		BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ),1 );

		String a;
		
		do
		{
			for ( int i=0; i<104; ++i)
			{
				MDC.log( i%2==0, line );
			}
		}
		while ( (a = reader.readLine())  != null);

	}

	public static void main2( String[] args )
	{
		
		MDC.log( true, "hello");
		MDC.log( false, "hola" );
		MDC.log( true, "mao" );
		MDC.log( true, "jaj" );
	}
	
}
