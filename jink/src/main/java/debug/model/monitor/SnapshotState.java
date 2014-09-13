package debug.model.monitor;

import debug.model.*;
import debug.model.object.ObjectModel;
import debug.model.thread.ThreadModel;

import com.sun.jdi.*;
/**
 ** Questo stato è uno snapshot, da intendere come immutabile una volta settato.
 */

abstract class SnapshotState implements MonitorState
{
	public MonitorState transition()
	{
		return this;
	}
	
	public boolean isLive()
	{
		return false;
	}

	public Value getWrappedValue() throws OperationException
	{
		throw new OperationException();
	}
	
}