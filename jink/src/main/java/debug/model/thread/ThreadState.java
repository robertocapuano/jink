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
	 ** Passi fatti: l'ultima posizione diversa da null è in fondo al vettore.
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
