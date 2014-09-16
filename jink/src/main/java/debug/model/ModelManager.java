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
package debug.model;

import javax.swing.*;

import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;

import java.util.*;

import debug.event.*;
import debug.runtime.*;
import debug.model.event.*;
import debug.bridge.Handler;

import debug.model.thread.ThreadModel;

import tools.*;

import debug.model.monitor.*;

import debug.model.object.LiveState;

// per il debug
import java.io.*;
import debug.gate.*;

public class ModelManager implements DebugOff
{
	public static final String VERSION = "0.8";

	protected final JDIListener jdiListener = new ModelJDIController( this );
	protected final BackEndModel backEndModel = BackEndModel.getShared();
	protected final RuntimeManager runtimeManager;
	
    protected final List listeners = new LinkedList();
	
	public BackEndModel getBackEndModel()
	{
		return backEndModel;
	}

	public ModelManager( RuntimeManager _runtime )
	{
		runtimeManager = _runtime;
		
		debug.model.monitor.MonitorModel.initVMFeatures( runtimeManager );
		debug.model.object.ObjectModel.initVMFeatures( runtimeManager );
		debug.model.thread.ThreadModel.initVMFeatures( runtimeManager );
		
		runtimeManager.addJDIListener( jdiListener );
	}


	// **TODO: verificare che non si siano race-condition qui.
    public void addModelListener( ModelListener l )
    {
		listeners.add( l );
	}

    public void removeModelListener( ModelListener l )
    {
    	listeners.remove( l );
    }

	/**
	 ** Chiamato dal package agents per unificare i moduli runtime e gate.
	 ** Vi sono due moduli: runtime, che gestisce jpda, e gate che gestisce gli agents inviati
	 ** in remoto ed eseguiti dal moduli exec.
	 */
	public void addHandler( ObjectReference oref, Handler handler ) throws OperationException
	{
		// l'oggetto viene inserito nel repository.
		DetailModel dModel = backEndModel.referenceToModel( oref );
		dModel.setHandler( handler );

		dispatchNewHandlerEvent( dModel );
		
		DC.log( LEVEL, dModel );
	}

	protected void dispatchEvent( final ModelEvent modelEvent )
	{
		for ( Iterator i=listeners.iterator(); i.hasNext(); )
		{
			ModelListener listener = (ModelListener) i.next();
			
			modelEvent.fire( listener ); // double dispatch
		}
	}

	void dispatchThreadJumpEvent( ThreadModel threadModel, Location loc )
	{
		dispatchEvent( new ThreadJumpEvent( threadModel, loc ) );
	}

	void dispatchThreadNewEvent( ThreadModel threadModel )
	{
		dispatchEvent( new ThreadNewEvent( threadModel ) );
	}

	void dispatchThreadEndEvent( ThreadModel threadModel )
	{
		dispatchEvent( new ThreadEndEvent( threadModel ) );
	}

	void dispatchThreadStepEvent( ThreadModel threadModel, Location loc )
	{
		dispatchEvent( new ThreadStepEvent( threadModel, loc ) );
	}

	void dispatchVMLaunchEvent()
	{
		dispatchEvent( new VMLaunchEvent() );
	}
	
	void dispatchVMQuitEvent()
	{
		dispatchEvent( new VMQuitEvent() );
	}

	void dispatchNewHandlerEvent( DetailModel dModel )
	{
		dispatchEvent( new NewHandlerEvent( dModel ) );
	}
	
	
	/////
	protected List modellerListeners = new LinkedList();
	
	public void addModellerListener( ModellerListener modellerListener )
	{
		modellerListeners.add( modellerListener );
	}
	
	public void removeModellerListener( ModellerListener modellerListener )
	{
		modellerListeners.remove( modellerListener );
	}
	
	protected void fireModellerEvent()
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				for ( Iterator i=modellerListeners.iterator(); i.hasNext(); )
				{
					ModellerListener modellerListener = (ModellerListener) i.next();
					modellerListener.change( ModelManager.this );
				}
			}
		} );
	}
	
	public boolean isDeepCopy()
	{
		return LiveState.isDeepCopy();
	}
	
	public void setDeepCopy( boolean _deepCopy )
	{
		if (LiveState.isDeepCopy() != _deepCopy)
		{	
			LiveState.setDeepCopy( _deepCopy );
			fireModellerEvent();
		}
	}

	public void stepMode( ThreadModel threadModel ) throws OperationException, StateException
	{
		try
		{
			if (!threadModel.isStepMode())
			{
				ThreadReference threadRef = (ThreadReference) threadModel.getWrappedValue();
				runtimeManager.stepMode( threadRef );
				threadModel.setStepMode( true );
			}
		}
		catch( debug.runtime.RuntimeException re )
		{
			throw new OperationException( re );
		}
	}
	
	public void removeStepMode( ThreadModel threadModel ) throws OperationException, StateException
	{
		ThreadReference threadRef = (ThreadReference) threadModel.getWrappedValue();
		threadModel.setStepMode( false );
		runtimeManager.removeStepMode( threadRef );
	}
		
	
/*	public static void main( String[] args ) throws Exception
	{
		RuntimeManager runtime = new RuntimeManager( "-classpath workspace/build", "Hello", "" );// "debug.bridge.server.BridgeServer", "" );

		GateManager gate = new GateManager();
		gate.attach();

		DebuggeeConsole debuggeeConsole = new DebuggeeConsole();
		runtime.attach( debuggeeConsole );

		ModelManager model = new ModelManager( runtime );
		
		runtime.resume();
//		gate.waitConnection();
		BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ),1 );
		reader.readLine();
		System.out.println( BackEndModel.getShared() );
	}
*/	
}