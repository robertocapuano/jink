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





