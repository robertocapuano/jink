package debug.model.object;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.monitor.*;
import debug.model.thread.*;

/**
 ** ObjectReference non più valido, quindi non interrogabile.
 ** Come suo surrogato abbiamo una shallow-copy.
 ** Rappresenta il this della precedente invocazione di dmetodo.
 */
public class SnapshotState implements ObjectState
{
	/**
	 ** Snapshot dello stato dell'oggetto.
	 */
	protected final Map fields;
	
	/**
	 ** Monitor snapshot
	 */
	protected final MonitorModel monitorModel;

	/**
	 ** Snapshot dei runners
	 */
	 protected final List runners;
	/**
	 ** numero di messaggi da ricevere perchè diventi History
	 */
	protected final static int TRANSITION_BOUND = 4;
	protected int transition_msg_count = 0;
	
	/**
	 ** time
	 */
	protected final int time;
	
	protected SnapshotState( LiveState liveState ) throws StateException, OperationException
	{
		fields = liveState.getSnapFields();

		MonitorModel live_monitor = liveState.getMonitorModel();
		monitorModel = live_monitor.newWithTransition();
		
		runners = liveState.getRunners();
	
		time = liveState.getTime();
	}

	public int getTime()
	{
		return time;
	}
	
//	private Map refToModel( Map 
	/**
	 ** Dopo che per N passi un oggetto è nello stato Snapshot passa allo stato
	 ** History in cui non ha più riferimenti a dati.
	 */
	public ObjectState transition()
	{
		if (++transition_msg_count == TRANSITION_BOUND )
		{
			HistoryState next_state = new HistoryState( this );
			return next_state;
		}
		else
		{
			return this;
		}
	}

	public Value getWrappedValue() throws StateException
	{
		throw new StateException();
	}

	public DetailModel getField( Field field ) throws OperationException
	{
		// **todo: inserire l'uso di BackEndModel
		// se l'oggetto
		try
		{
			DetailModel model = (DetailModel) fields.get( field );
			return model;
		}
		catch( Exception e )
		{
			throw new OperationException( e );
		}
	}
	
	public void setField( Field field, DetailModel model ) throws StateException
	{
		throw new StateException();
	}
	
	public Map getFields()
	{
		return fields;
	}
	
	public boolean isLocked() throws OperationException
	{
		return monitorModel.isLocked();
	}

	public MonitorModel getMonitorModel()
	{
		return monitorModel;
	}
	
	public List getRunners()
	{
		return runners;
	}
	
	public void enter( ThreadModel thread_m ) throws StateException
	{
		throw new StateException();
	}
	
	public void leave( ThreadModel thread_m ) throws StateException
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
		res = "<" + getClass() + ": ";
		res += "values:" + fields +",";
		res += "monitor:" + monitorModel + ",";
		res += "msg_count:" + transition_msg_count + ">"; 
		return res;
	}

}