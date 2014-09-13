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