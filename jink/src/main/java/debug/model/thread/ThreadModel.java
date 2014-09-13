package debug.model.thread;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.object.ObjectModel;

import debug.runtime.RuntimeManager;


import debug.mock.*;

import tools.*;

import java.io.*;

/**
 ** Nel Thread vi e il concetto di history, viene fatto lo snapshot (shallow copy) degli oggetti
 ** solo per N stati precedenti a quello attuale.
 **
 */
public class ThreadModel extends ObjectModel implements DebugOff, Serializable
{
	public static void initVMFeatures( RuntimeManager runtime )
	{
		LiveState.initVMFeatures( runtime );
	}

	//// instance
	transient boolean stepMode = false;
	
//	BackEndModel objectRepository;

	public ThreadModel( ThreadReference tr ) throws OperationException
	{
		super( tr.hashCode(), 
				(ObjectModel) BackEndModel.getShared().referenceToModel( tr.referenceType().classObject() ),
		   		new LiveState( tr ),
				new LinkedList(),
				tr.referenceType().visibleMethods()

		 );
	}
	
	public ThreadModel( ThreadModel that ) throws OperationException
	{
		super( that );
		stepMode = that.stepMode;
	}
	
	public void doEnterObject( ObjectModel obj_mdl, Method method ) throws StateException, OperationException
	{
		((ThreadState)state).doEnterObject( this, obj_mdl, method );
	}

	public void doLeaveObject( ObjectModel leave, ObjectModel enter, Method method ) throws OperationException, StateException
	{
		((ThreadState)state).doLeaveObject( this, leave, enter, method );
	}
	
	public void interrupt() throws StateException
	{
		((ThreadState)state).interrupt();
	}
	
	public String getName()
	{	
		return ((ThreadState)state).getName();
	}
	
	public ObjectModel[] getOwnedMonitors() throws OperationException
	{
		return ((ThreadState)state).getOwnedMonitors();
	}
	
	public ObjectModel getContendedMonitor() throws OperationException
	{
		return ((ThreadState)state).getContendedMonitor();
	}
	
	//// timeline
	// questo metodo andrˆ tolto.
	public JumpModel getJump( int n ) throws OperationException
	{
		return ((ThreadState)state).getJump( n );
	}
	
	public int timelineDepth()
	{
		return ((ThreadState)state).getTimelineDepth();
	}

	public LinkedList getAllJumps()
	{
		return ((ThreadState)state).getTimeline();
	}
	
	public JumpModel getLastJump() throws OperationException
	{
		return getJump( timelineDepth() -1 );
	}
	
	/** 
	 ** Jim...e morto!
	 */
	public boolean isRunnable()
	{
		return ((ThreadState)state).isRunnable();
	}
	
	/**
	 ** In attesa di entrare nel monitor.
	 */
	public boolean isBlocked()
	{
		return ((ThreadState)state).isBlocked();
	}
	
	/**
	 ** Sssshhh, dorme
	 */
	public boolean isSleeping()
	{
		return ((ThreadState)state).isSleeping();
	}
	
	/**
	 ** Attesa di una notify
	 */
	public boolean isWaiting()
	{
		return ((ThreadState)state).isWaiting();
	}
	
	public void setStepMode( boolean value ) throws StateException
	{
		((ThreadState)state).setStepMode( value );
	}
	
	public boolean isStepMode() throws StateException
	{
		return ((ThreadState)state).isStepMode();
	}
	
	public String getStringStatus()
	{
		String status ="";
		
		status += isRunnable() ? 'R' : '-';
		status += isBlocked() ? 'B' : '-';
		status += isSleeping() ? 'S' : '-';
		status += isWaiting() ? 'W' : '-';
		return status;
	}
	// Gestione IncompatibleThreadStateException 
	void incompatibleThreadStateHandler( IncompatibleThreadStateException  itse )
	{
		DC.log( LEVEL, itse );
	}
	
	public String shortDescription()
	{
		String desc = super.shortDescription();
		desc += "t:" + getName();
		return desc;
	}
	
	public String longDescription()
	{
		String desc = super.longDescription() + ' ';
		desc += "status:" +getStringStatus() + ' ';
		return desc;
	}
	
	public String toString()
	{
		return longDescription();
	}
	
	public static void main( String[] args ) throws Exception
	{
/*		ThreadReference tr = new ThreadReferenceMock( "Helo" );

		ThreadModel tm = new ThreadModel( tr );
		System.out.println( "" + tm );
		
		tm.transition();
		System.out.println( "" + tm );

		tm.transition();
		System.out.println( "" + tm );
*/		
	}
}