package debug.model.monitor;

import debug.model.*;
import debug.model.object.ObjectModel;
import debug.model.thread.ThreadModel;

import com.sun.jdi.*;

interface MonitorState
{
	MonitorState transition() throws OperationException, StateException;

	ThreadModel getOwner() throws OperationException;
	ThreadModel[] getWaitSet() throws OperationException, StateException;

	boolean isLive();
	boolean isLocked() throws OperationException;
	
	Value getWrappedValue() throws StateException, OperationException;
}