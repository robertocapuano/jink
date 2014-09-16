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
import debug.model.object.*;

class SnapshotState extends ThreadState
{
	protected final int time;
	protected final String name;
	protected final ObjectModel[] ownedMonitors;
	protected final ObjectModel contendedMonitor;
	protected final boolean blocked_b, sleeping_b, waiting_b;

	
	SnapshotState( LiveState live ) throws OperationException, StateException
	{
		super( live );

		time = BackEndModel.getShared().getGTime();
		name = live.getName();
		ownedMonitors = live.getOwnedMonitors();
		contendedMonitor = live.getContendedMonitor();

		blocked_b = live.isBlocked();
		sleeping_b = live.isSleeping();
		waiting_b = live.isWaiting();		
	}
	
	public int getTime()
	{
		return time;
	}

	public ObjectState transition()
	{
		return this;
	}

	public Value getWrappedValue() throws StateException
	{
		throw new StateException();
	}

	protected void interrupt() throws StateException
	{
		throw new StateException();
	}
	
	protected String getName()
	{
		return name;
	}
	
	protected ObjectModel[] getOwnedMonitors() 
	{
		return ownedMonitors;
	}
	
	protected ObjectModel getContendedMonitor() 
	{
		return contendedMonitor;
	}
	
	protected void doEnterObject( ThreadModel thread_m, ObjectModel obj_mdl, Method method ) throws StateException
	{
		throw new StateException();
	}

	protected void doLeaveObject( ThreadModel thread_m, ObjectModel leave, ObjectModel enter, Method method ) throws StateException
	{
		throw new StateException();
	}

	// status
	protected boolean isRunnable()
	{
		return false;
	}
	
	protected boolean isBlocked()
	{
		return blocked_b;
	}
	
	protected boolean isSleeping()
	{
		return sleeping_b;
	}

	protected boolean isWaiting()
	{
		return waiting_b;
	}

	public void setStepMode( boolean value ) throws StateException
	{
		throw new StateException();
	}
	
	public boolean isStepMode() throws StateException
	{
		throw new StateException();
	}

	//// Stato
	public boolean isLive()
	{
		return false;
	}
	
	public boolean isSnapshot()
	{
		return true;
	}
	
	public boolean isHistory()
	{
		return false;
	}
	
	public boolean isDead()
	{
		return false;
	}

	public String toString()
	{
		String res;
		res = "<" + getClass() + ":";
		res += "name:" + name + ",";
		res += "ownedMonitors:" + ownedMonitors + ",";
		
		if (contendedMonitor!=null)
			res += "contendedMonitor:" + contendedMonitor + ",";
		else
			res += "contendedMonitor:null,";

		res += "b:" + blocked_b + ",s:" + sleeping_b + ",w:" + waiting_b + ",";
		res += ">";
		
		return res;
	}	
}
