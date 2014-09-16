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
package debug.agentsmgr;

import com.sun.jdi.*;
import com.sun.jdi.event.*;

import java.util.*;

import debug.event.*;
import debug.exec.*;
import debug.model.ModelManager;
import debug.gate.GateManager;
import debug.runtime.RuntimeManager;

import javax.swing.SwingUtilities;

import tools.*;

// main
import debug.bridge.*;
import java.io.Serializable;
import agents.*;
import debug.model.*;

public class AgentsManager implements DebugOff
{
	public static final String VERSION = "0.1";

	public AgentsManager( RuntimeManager runtime, ModelManager model )
	{
		GetHandlerJDIController ghjc = new GetHandlerJDIController( model );
		runtime.addJDIListener( ghjc );
	}
	
	
	public static void main( String[] args ) throws Exception
	{
/*
		RuntimeManager runtime = new RuntimeManager( "-classpath build:libs", "debug.exec.ExecManager", "" );// "debug.bridge.server.BridgeServer", "" );
		GateManager gate = new GateManager();
		gate.attach();

		DebuggeeConsole debuggeeConsole = new DebuggeeConsole();
		runtime.attach( debuggeeConsole );

		ModelManager model = new ModelManager( runtime );
		AgentsManager agents = new AgentsManager( runtime, model );
		
		runtime.resume();
		gate.waitConnection();
		
		{
			NewClassLoader ncl1 = new NewClassLoader( "workspace/build" );
			Reply r1 = gate.cross( ncl1 );		
			DC.log( LEVEL, r1 );
		}
				
		Handler class_h;

		{
			String class_s = "Hello";
			NewClass nc = new NewClass( class_s );
			Reply r1 = gate.cross( nc );
			DC.log( LEVEL, r1 );
			class_h = (Handler) r1.getEnveloped();
		}
		
		Handler self_h;
		
		{
			String[] sig_a = new String[0];
			Serializable[] args_a = new Serializable[0];
			Invocation inv = new Invocation( class_h, sig_a, args_a );
			NewObject no = new NewObject( inv );
			Reply r2 = gate.cross( no );		
			DC.log( LEVEL, r2 );
			self_h = (Handler) r2.getEnveloped();
		}

		{
			NewThread nt = new NewThread( self_h );
			Reply r1 = gate.cross( nt );
			DC.log( LEVEL, r1 );
		}					

		System.out.println( BackEndModel.getShared() );		
		

		{
			String method_s = "f";
			String[] sig_a = new String[0];
			Serializable[] args_a = new Serializable[0];
			Invocation inv = new Invocation( self_h, method_s, sig_a, args_a );
			InvokeMethod mi = new InvokeMethod( inv );
			Reply r = gate.cross( mi );
			DC.log( LEVEL, r );
		}

		{
			String method_s = "h";
			String[] sig_a = new String[] { "int" };
			Serializable[] args_a = new Serializable[] { new Integer( 12 ) };
			Invocation inv = new Invocation( self_h, method_s, sig_a, args_a );
			InvokeMethod mi = new InvokeMethod( inv );
			Reply r = gate.cross( mi );
			DC.log( LEVEL, r );
		}

		{
			String method_s = "i";
			String[] sig_a = new String[] { "java.lang.String", "int" };
			Serializable[] args_a = new Serializable[] { "Aibo", new Integer( 12 ) };
			Invocation inv = new Invocation( self_h, method_s, sig_a, args_a );
			InvokeMethod mi = new InvokeMethod( inv );
			Reply r = gate.cross( mi );
			DC.log( LEVEL, r );
		}
		
		Handler class_A;
		
		{
			String class_s = "A";
			NewClass nc = new NewClass( class_s );
			Reply r1 = gate.cross( nc );
			DC.log( LEVEL, r1 );
			class_A = (Handler) r1.getEnveloped();
		}
		
		Handler A_h;
		
		{
			String[] sig_a = new String[0];
			Serializable[] args_a = new Serializable[0];
			Invocation inv = new Invocation( class_A, sig_a, args_a );
			NewObject no = new NewObject( inv );
			Reply r2 = gate.cross( no );		
			DC.log( LEVEL, r2 );
			A_h = (Handler) r2.getEnveloped();
		}

		{
			String method_s = "j";
			String[] sig_a = new String[] { "A" };
			Serializable[] args_a = new Serializable[] { A_h };
			Invocation inv = new Invocation( self_h, method_s, sig_a, args_a );
			InvokeMethod mi = new InvokeMethod( inv );
			Reply r = gate.cross( mi );
			DC.log( LEVEL, r );
		}


		{
			RemoveObject ro = new RemoveObject( self_h );
			Reply r = gate.cross( ro );
			DC.log( LEVEL, r );
		}

*/

	}
	
}