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
