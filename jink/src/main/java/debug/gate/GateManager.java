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
package debug.gate;

import java.net.*;
import java.io.*;
import java.util.*;

import debug.exec.Agent;
import debug.exec.Reply;

//import debug.agents.*;

import debug.bridge.*;

import tools.*;

public class GateManager implements DebugOn
{
	public static final String VERSION = "0.2";

	private BufferedReader reader;
//	private PrintStream writer;
	private PrintWriter writer;
	
	public GateManager()
	{
	}
	
/*	public void attach()
	{
		new Thread( new Runnable()
		{
			public void run()
			{
				listenConnection();
			}
		} ).start();
	}
*/	
	public void listenConnection()
	{
		try
		{
			ServerSocket server_sk = new ServerSocket( Bridge.JINK_PORT );
			Socket connection_sk = server_sk.accept();
			InputStream in = connection_sk.getInputStream();
			OutputStream out = connection_sk.getOutputStream();

			attach( in, out );
		}
		catch (Exception e )
		{
			DC.log( LEVEL, e );
		}
	}

	protected void attach( InputStream downStream, OutputStream upStream )
	{
		reader = new BufferedReader( new InputStreamReader( downStream ),1 );
//		writer = new PrintStream(  upStream, true);
		writer = new PrintWriter( new OutputStreamWriter( upStream ) );
	}
	
	public void detach()
	{
		reader = null;
		writer = null;
	}

	public Reply cross( Agent agent ) throws BridgeException
	{
		try
		{
			DC.log(LEVEL, agent);
			String agent_s = Bridge.encodeObject( agent );
			
			writer.println( agent_s );
			writer.flush();
							
			String reply_s = reader.readLine();
			
			Reply reply_r = (Reply) Bridge.decodeObject( reply_s );
			DC.log( LEVEL, reply_r );
			return reply_r;
		}
		catch( IOException ioe )		
		{
			throw new BridgeException( ioe );
		}
	}

	public static void main( String[] args ) throws Exception
	{
/*		try
		{
			GateManager gate = new GateManager();
			gate.attach( System.in, System.out );
			
			NewClassLoader ncl1 = new NewClassLoader( "workspace/lib" );
			Reply r1 = gate.cross( ncl1 );
		
			AddClassLoader acl1 = new AddClassLoader( "lib" );
			Reply r2 = gate.cross( acl1 );
			
			gate.detach();
		}
		catch( Exception e )
		{
			DC.log( e );
		}
*/
	}

/*
	public static void main1( String[] args ) throws Exception
	{
		try
		{
			GateManager manager = new GateManager();
			Reply r1_i = new Reply( new Boolean(true) );
			Reply r2_i = new Reply( new Integer(123) );
	
			String reply_s = Bridge.encodeObject( r1_i ) + "\n";
			reply_s += Bridge.encodeObject( r2_i) + "\n";
			
			ByteArrayInputStream bais = new ByteArrayInputStream( reply_s.getBytes() );
			
			manager.attach( bais, System.out );
			
			NewClassLoader ncl1 = new NewClassLoader( 12, "http://2" );
			NewClassLoader ncl2 = new NewClassLoader( 1, "http://" );
			Reply r1_o = manager.cross( ncl1 );
			Reply r2_o = manager.cross( ncl2 );
			assert r1_i.equals( r1_o );
			assert r2_i.equals( r2_o );
			manager.detach();
		}
		catch( Exception e )
		{
			DC.log( e );
		}
	}
*/

}