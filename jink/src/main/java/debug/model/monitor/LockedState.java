package debug.model.monitor;

import debug.model.*;
import debug.model.object.ObjectModel;
import debug.model.thread.ThreadModel;

import com.sun.jdi.*;

/**
 ** Questo stato è uno snapshot, da intendere come immutabile una volta settato.
 ** Ci può può essere un problema di discretizzazione: l'istante in cui è fatto lo snapshot è rappresentativo per tutto il passo.
 ** 
 */
 
class LockedState extends SnapshotState
{
	final protected ThreadModel owner;
	final protected ThreadModel[] waitSet;
	
	protected LockedState( LiveState liveState ) throws OperationException
	{
		assert liveState.isLocked();
		owner = liveState.getOwner();
		waitSet = liveState.getWaitSet();
	}
	
	
	public ThreadModel getOwner()
	{
		return owner;
	}

	public ThreadModel[] getWaitSet()
	{
		return waitSet;
	}
	
	public boolean isLocked() 
	{
		return true;
	}
	
	public String toString()
	{
		String res;
		res = "<" + getClass() + ":";
		res += "state:locked";
		res += "owner:"+owner;
		res += "wait:"+waitSet;
		res += ">";
	
		return res;
	}
	
}




// Cimitero del codice
/*
	void setLocked() throws StateException
	{
		state = state.newLocked();
	}

	 ** Crea un modello di un oggetto unlocked.
	
	void setUnlocked() throws StateException
	{
		state = state.newUnlocked();	
	}

	void setMonitorLive( ObjectReference objectReference )
	{
		state = new MonitorLiveState( objectReference );
	}
	
	 ** Crea un modello di un oggetto locked
	MonitorModel( ThreadModel owner )
	{
		setLocked( owner );
	}

	MonitorModel()
	{
		setUnlocked();
	}
	ObjectReference getMonitored()
	{
		return monitored;
	}

*/
