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
package debug.model.thread;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.object.ObjectModel;
import debug.model.object.ObjectState;
import debug.model.monitor.MonitorModel;

abstract class ThreadState implements ObjectState
{
	/**
	 ** Passi fatti: l'ultima posizione diversa da null � in fondo al vettore.
	 */
	final protected LinkedList timeline;

	// ObjectState
//	protected final MonitorModel monitorModel;
//	protected final Field[] fieldDescriptors;
//	protected final List runners;
	
	protected ThreadState( ThreadState that )
	{
		this( that.timeline );
	}
	
	protected ThreadState()
	{
		this( new LinkedList() );
	}
	
	protected ThreadState( LinkedList _timeline )
	{
		timeline = _timeline;
	}
	
	protected LinkedList getTimeline()
	{
		return timeline;
	}
	
	protected int getTimelineDepth()
	{
		return timeline.size();
	}
	
	/**
	 ** I passi non sono successivi quindi l'n-simo passimo potrebbe venire 
	 */
	protected JumpModel getJump( int n ) throws OperationException
	{
		if ( timeline.size() <= n )
		{
			throw new OperationException( "local_timeline.size() >= n"	);
		}
		
		return (JumpModel) timeline.get( n );
	}

	abstract void interrupt() throws StateException;
	abstract String getName();

	abstract ObjectModel[] getOwnedMonitors() throws OperationException;
	abstract ObjectModel getContendedMonitor() throws OperationException;
	

	abstract protected void doEnterObject( ThreadModel thread_m, ObjectModel obj_mdl, Method method ) throws StateException, OperationException;
	abstract protected void doLeaveObject( ThreadModel thread_m, ObjectModel leave, ObjectModel enter, Method method ) throws OperationException, StateException;
	
	// status
	abstract boolean isRunnable();
	abstract boolean isBlocked();
	abstract boolean isSleeping();
	abstract boolean isWaiting();

	abstract boolean isStepMode() throws StateException;
	abstract void setStepMode( boolean value ) throws StateException;
	
	// anche in ObjectState
	public abstract ObjectState transition( ) throws StateException, OperationException;
	public abstract Value getWrappedValue() throws StateException;

	
	////
	//// Sezione ObjectState
	////
	
	/**
	 ** Gestione Campi
	 */
	public DetailModel getField( Field field ) throws StateException
	{
	 	throw new StateException();
	}
	
	public Map getFields() throws StateException
	{
		throw new StateException();
	}

	public void setField( Field field, DetailModel value ) throws StateException
	{
		throw new StateException();
	}
	
	// Sezione Monitor
	public boolean isLocked() throws StateException
	{
		throw new StateException();
	}
	
	public MonitorModel getMonitorModel() throws StateException
	{
		throw new StateException();
	}

	// Sezione Threads.
	/**
	 ** Restituisce tutti i threads che passano per questo oggetto 
	 ** Ha senso solo per gli oggetti lives.
	 ** Altrimenti restituisce solo l'oggetto ThreadModel a cui appartiene lo snapshot.
	 ** **todo: da verificare questa logica.
	 */
	public List getRunners() throws StateException
	{
		throw new StateException();
	}

	public void enter( ThreadModel thread_m ) throws StateException
	{
		throw new StateException();
	}
	
	public void leave( ThreadModel thread_m ) throws StateException
	{
		throw new StateException();
	}
	
}
