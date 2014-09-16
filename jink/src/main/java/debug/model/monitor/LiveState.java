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

import java.util.*;

import com.sun.jdi.*;

import debug.model.*;
import debug.model.object.ObjectModel;
import debug.model.thread.ThreadModel;

import debug.runtime.RuntimeManager;

import tools.*;

class LiveState implements MonitorState, DebugOff
{
	static boolean canGetMonitorInfo;
	
	static void initVMFeatures( RuntimeManager runtime )
	{
		canGetMonitorInfo = runtime.canGetMonitorInfo();
	}
	
	protected final ObjectReference monitored;
	
	protected LiveState( ObjectReference _monitored )
	{
		monitored = _monitored;
	}
	
	public MonitorState transition() throws OperationException, StateException
	{
		if ( isLocked() )
			return new LockedState( this );
		else
			return new UnlockedState( this );
	}

	public boolean isLive()
	{
		return true;
	}
	
	public ThreadModel getOwner() throws OperationException
	{
		try
		{
			if (!canGetMonitorInfo)
				throw new OperationException();

			ThreadReference locker = monitored.owningThread();
			if (locker == null )
				throw new OperationException();
				
			ThreadModel owner = (ThreadModel) BackEndModel.getShared().referenceToModel( locker );
			return owner;
		}
		catch( IncompatibleThreadStateException itse )
		{
			DC.log( itse );
			throw new OperationException( itse );
		}
		catch( UnsupportedOperationException uoe )
		{
			DC.log(uoe );
			throw new OperationException( uoe );
		}
	}

	public boolean isLocked() throws OperationException
	{
		if (!canGetMonitorInfo)
		{
			return false;
		}
		
		try
		{
			ThreadReference threadReference = monitored.owningThread();
			return threadReference != null;
		}
		catch( IncompatibleThreadStateException itse )
		{
			DC.log( itse );
			throw new OperationException( itse );
		}
		catch( UnsupportedOperationException uoe )
		{
			DC.log(uoe );
			throw new OperationException( uoe );
		}
	}


	public Value getWrappedValue() 
	{
		return monitored;
	}

	public ThreadModel[] getWaitSet() throws OperationException
	{
		if (!canGetMonitorInfo)
			return new ThreadModel[0];	
//			throw new OperationException();
		try
		{
			List wait_trl = monitored.waitingThreads();
			ThreadModel[] wait_a = new ThreadModel[ wait_trl.size() ];
			
			int c=0;
			
			for ( Iterator i=wait_trl.iterator(); i.hasNext(); )
			{
				ThreadReference wait_tr = (ThreadReference) i.next();
				
				ThreadModel wait_tm = (ThreadModel) BackEndModel.getShared().referenceToModel( wait_tr );
				wait_a[c++] = wait_tm;
			}
			
			return wait_a;
		}
		catch( IncompatibleThreadStateException itse )
		{
			throw new OperationException( itse );
		}
		catch( UnsupportedOperationException uoe )
		{
			DC.log( uoe );
			throw new OperationException( uoe );
		}
	}
	
	public String toString()
	{
		String res;
		res = "<" + getClass() + ":";
		res += "monitored:"+monitored;
		res += ">";
	
		return res;
	}
	
}