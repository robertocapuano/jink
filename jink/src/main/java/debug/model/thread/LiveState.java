package debug.model.thread;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.object.*;

import debug.runtime.RuntimeManager;

class LiveState extends ThreadState
{
	protected static boolean canGetCurrentContendedMonitor, canGetOwnedMonitorInfo;

	public static void initVMFeatures( RuntimeManager runtime )
	{
		canGetCurrentContendedMonitor = runtime.canGetCurrentContendedMonitor();
		canGetOwnedMonitorInfo = runtime.canGetOwnedMonitorInfo();
	}

	////

	protected final ThreadReference threadReference;
	
	protected boolean stepMode = false;

	LiveState( ThreadReference _threadReference )
	{
		super( );
		threadReference = _threadReference;
	}

	public int getTime() 
	{
		return BackEndModel.getShared().getGTime();
	}
	
	public ObjectState transition() throws StateException, OperationException
	{
		ThreadState nextState = new SnapshotState( this );
		return nextState;
	}
	
	protected void interrupt()
	{
		threadReference.interrupt();
	}
	
	protected String getName()
	{
		return threadReference.name();
	}
	
	public Value getWrappedValue()
	{
		return threadReference;
	}

	protected void doEnterObject( ThreadModel thread_m, ObjectModel obj_mdl, Method method ) throws StateException, OperationException
	{
		if (timeline.size()>0)
		{
			JumpModel last_step = (JumpModel) timeline.getLast();
			last_step.snapshot();
		}

		JumpModel step = new JumpModel( thread_m, obj_mdl, method, false );
		obj_mdl.enter( thread_m ); ////
		timeline.add( step );
	}
	
	protected void doLeaveObject( ThreadModel thread_m, ObjectModel leave, ObjectModel enter, Method method ) throws OperationException, StateException
	{
		JumpModel last_step = (JumpModel) timeline.getLast();
		last_step.snapshot();

		leave.leave( thread_m );
		// ntp (note del programmatore)
		// mettendo enter al posto di leave (come sarebbe logico)
		// nella timeline viene aggiunto lo stato dell'oggento invocante
		// ed... insomma non mi sembra giusto.
		JumpModel step = new JumpModel( thread_m, leave, method, true );
		timeline.add( step );
	}
	
	protected ObjectModel[] getOwnedMonitors() throws OperationException
	{
		if (!canGetOwnedMonitorInfo)
			return new ObjectModel[0];
	
		try
		{
			List or_l = threadReference.ownedMonitors();
			List om_l = new LinkedList();
			
			BackEndModel rep = BackEndModel.getShared();
			
			for ( Iterator it=or_l.iterator(); it.hasNext(); )
			{
				ObjectReference or = (ObjectReference) it.next();
				DetailModel m = rep.referenceToModel( or );
				om_l.add( m );
			}
			
			return (ObjectModel[]) om_l.toArray( new ObjectModel[0] );
		}
		catch( UnsupportedOperationException uoe )
		{
			throw new OperationException( uoe );
		}
		catch( IncompatibleThreadStateException itse )
		{
			throw new OperationException( itse );
		}		
	}
	
	protected ObjectModel getContendedMonitor() throws OperationException
	{
		if (!canGetCurrentContendedMonitor)
			return null;
			
		try
		{
			ObjectModel om = null;
			ObjectReference or = threadReference.currentContendedMonitor();
			
			if ( or != null )
			{
				BackEndModel rep = BackEndModel.getShared();
				om = (ObjectModel) rep.referenceToModel( or );
			}
	
			return om;
		}
		catch( UnsupportedOperationException uoe )
		{
			throw new OperationException( uoe );
		}
		catch( IncompatibleThreadStateException itse )
		{
			throw new OperationException( itse );
		}		
	}

	// status
	protected boolean isRunnable()
	{
		return (threadReference.status() & ThreadReference.THREAD_STATUS_RUNNING) != 0;
	}
	
	protected boolean isBlocked()
	{
		return (threadReference.status() & ThreadReference.THREAD_STATUS_MONITOR) != 0;
	}
	
	protected boolean isSleeping()
	{
		return (threadReference.status() & ThreadReference.THREAD_STATUS_SLEEPING) != 0;
	}
	
	protected boolean isWaiting()
	{
		return (threadReference.status() & ThreadReference.THREAD_STATUS_WAIT) != 0;
	}

	public void setStepMode( boolean value )
	{
		stepMode = value;
	}
	
	public boolean isStepMode()
	{
		return stepMode;
	}

	//// Stato
	public boolean isLive()
	{
		return true;
	}
	
	public boolean isSnapshot()
	{
		return false;
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
		res += "tRef:" + threadReference + ",";
		res += ">";
		
		return res;
	}	
}
