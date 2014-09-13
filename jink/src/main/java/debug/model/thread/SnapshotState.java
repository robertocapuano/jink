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
