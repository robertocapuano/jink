package debug.model.array;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.monitor.*;
import debug.model.primitive.*;
import debug.model.object.ObjectState;

class HistoryState extends debug.model.object.HistoryState implements ArrayState
{
	HistoryState( SnapshotState snapshotState )
	{
		super( snapshotState );
	}

	public DetailModel getComponent( int index ) throws StateException
	{
		throw new StateException();
	}
	
	public void setComponent( int index, DetailModel component ) throws StateException
	{
		throw new StateException();
	}
	
	public DetailModel[] getComponents() throws StateException
	{
		throw new StateException();
	}
	 
/*	public int getLength() throws StateException
	{
		throw new StateException();
	}
*/	 
	public ObjectState transition() //throws StateException, OperationException
	{
//		throw new StateException();
		return this;
	}
	
}