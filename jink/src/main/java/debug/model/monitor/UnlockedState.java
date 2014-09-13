package debug.model.monitor;

import debug.model.*;
import debug.model.object.ObjectModel;
import debug.model.thread.ThreadModel;

import com.sun.jdi.*;
/**
 ** Questo stato è uno snapshot, da intendere come immutabile una volta settato.
 */

class UnlockedState extends SnapshotState
{
	protected UnlockedState( LiveState liveState) throws OperationException
	{
		assert ! liveState.isLocked();
	}

	public boolean isLocked()
	{
		return false;
	}
	
	public ThreadModel getOwner() throws OperationException
	{
		throw new OperationException();
	}
	
	public ThreadModel[] getWaitSet() throws OperationException
	{
		throw new OperationException();
	}

	public String toString()
	{
		String res;
		res = "<" + getClass() + ":unlocked>";
		return res;
	}

}