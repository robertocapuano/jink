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
package debug.model.monitor;

import debug.model.*;
import debug.model.object.ObjectModel;
import debug.model.thread.ThreadModel;

import com.sun.jdi.*;

import debug.runtime.RuntimeManager;

import tools.*;

public class MonitorModel extends DetailModel
{
	protected MonitorState state;
	
	public static void initVMFeatures( RuntimeManager runtime )
	{
		LiveState.initVMFeatures( runtime );
	}
	
	/**
	 ** Crea un modello di un oggetto live (look)
	 */
	public MonitorModel( ObjectReference objectReference )
	{
		super( objectReference.hashCode() );
		state = new LiveState( objectReference );
	}

	private MonitorModel( MonitorModel that )
	{
		super( that.getUID() );
		this.state = that.state;
	}

	/**
	 ** Factory Method
	 */
	public MonitorModel newWithTransition() throws OperationException, StateException
	{
		MonitorModel next = new MonitorModel( this );
		next.transition();
		return next;
	}
	
	
	public void transition() throws OperationException, StateException
	{
		state = state.transition();
	}
	
	public Value getWrappedValue() throws StateException, OperationException
	{
		return state.getWrappedValue();
	}

	public boolean isLive()
	{
		return state.isLive();
	}
	
	public boolean isLocked() throws OperationException
	{
		return state.isLocked();
	}
	
	public ThreadModel getOwner() throws OperationException
	{
		return state.getOwner();
	}
	
	
	public ThreadModel[] getWaitSet() throws OperationException, StateException
	{
		return state.getWaitSet();
	}

	public String getStateName()
	{
		return Stringer.extractClassState( state.getClass().getName() );
	}
	
	public String toString()
	{
		String res = super.toString();
		res += "state:" + getStateName();
		return res;		
	}

}





