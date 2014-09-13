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