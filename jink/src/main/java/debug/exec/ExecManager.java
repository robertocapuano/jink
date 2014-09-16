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
package debug.exec;

import java.util.*;

import java.io.*;
import java.net.*;

import debug.bridge.*;

import tools.*;

// I Metodi di servizio sono eseguiti con il thread main
// Le invocazioni degli oggetti usando thread di lavoro.

public class ExecManager implements DebugOn
{
	public static final String VERSION = "0.2";

	private BufferedReader reader;
	private PrintWriter writer;
	
	private Map workspace = new HashMap();
	
	private final List classLoaders;
	
	public ExecManager()
	{
		classLoaders = new LinkedList();
	}
	
	private void connect()
	{
		try
		{
			Socket connection_sk = new Socket( "127.0.0.1", Bridge.JINK_PORT );
			
			InputStream in = connection_sk.getInputStream();
			OutputStream out = connection_sk.getOutputStream();
			attach( in, out );
		}
		catch (Exception e )
		{
			MDC.log( LEVEL, e );
		}
	}	

	public void attach( InputStream in, OutputStream out )
	{
		reader = new BufferedReader( new InputStreamReader( in ),1 );
		writer = new PrintWriter( new OutputStreamWriter( out  ) );
	}
			
	public void run() throws ExecException
	{
		try
		{
			MDC.log( "ExecManager started");

			while( true )
			{
				String agent_s = reader.readLine();
				MDC.log(  LEVEL, agent_s );
				Agent agent = (Agent) Bridge.decodeObject( agent_s );
				MDC.log( LEVEL, agent );
				
				Reply reply = agent.perform( this );
				MDC.log( LEVEL, reply );
				
				String reply_s = Bridge.encodeObject( reply ) ;
				MDC.log( LEVEL, reply_s );
				writer.println( reply_s );
				writer.flush();
			}
		}
		catch( IOException ioe )
		{
			throw new ExecException( ioe );
		}
		catch( BridgeException be )
		{
			throw new ExecException( be );
		}
	}
	
	/**
	 **l'ultimo classloader,i classloaders sono linkati come un albero
	 ** ed usano il delegation model.
	 */
	public ClassLoader getLastClassLoader( )
	{
		ClassLoader last = (ClassLoader) classLoaders.get( 0  );
		return last;
	}
	
	public void removeClassLoaders()
	{
		classLoaders.clear();
	}
	
	public void addClassLoader( ClassLoader classLoader )
	{
		classLoaders.add( classLoader );
	}
		
	public void addClassLoader( int pos, ClassLoader classLoader )
	{
		classLoaders.add( 0, classLoader );
	}		

	public Handler putTool( Object newObject )
	{
		Handler handler = new Handler( newObject );
		workspace.put(  handler, newObject );
		return handler;
	}
	
	public Object getTool( Handler handler )
	{
		return workspace.get( handler );
	}
	
	public Object removeObject( Handler handler )
	{
		return workspace.remove( handler );
	}
	
	public String toString()
	{
		String res = super.toString();
		res += "classLoaders: " + "[" + classLoaders.size() + "]";
		for (int i=0; i<classLoaders.size(); ++i )
			res += classLoaders.get(i);
		return res;
	}
	
	
		
	public static void main(String[] args ) 
	{
		try
		{
			Bridge bridge = new Bridge();
			ExecManager exec = new ExecManager( );// System.in, System.out );
			exec.connect();
			exec.run();
/*			Console con = new Console();
			System.setIn( con.getInputStream() );
			System.setOut( con.getOutputStream() );
			System.setErr( con.getOutputStream() );
*/
//			exec.run();
		}
		catch( Exception e )
		{
			MDC.log( e );
		}
	}
	
	
}


/*
** Cimitero del codice
				BufferedReader readek = new BufferedReader( new InputStreamReader( System.in ),1 );
				String line = readek.readLine();
				System.out.println( line );
				

*/
