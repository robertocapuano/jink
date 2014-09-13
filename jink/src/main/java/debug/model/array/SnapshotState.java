package debug.model.array;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.monitor.*;
import debug.model.primitive.*;
import debug.model.object.ObjectState;

class SnapshotState extends debug.model.object.SnapshotState implements ArrayState
{
	protected final DetailModel[] components;

	SnapshotState( LiveState live ) throws OperationException, StateException
	{
		super( live );
		
		components = live.getSnapComponents();
	}

/*	public int getLength()
	{
		return components.length;
	}
*/	
	public ObjectState transition() //throws StateException //, OperationException
	{
		return new HistoryState( this );
	}
	
	public DetailModel getComponent( int index ) //throws OperationException
	{
		return components[index];
	}
	
	public void setComponent( int index, DetailModel model ) throws StateException
	{
		throw new StateException();
	}
	
	// Doppia conversione di components: prima da lista a vettore poi di nuovo in lista
	public DetailModel[] getComponents()
	{
		return components;
	}
	
	public DetailModel[] getSnapComponents() 
	{
		return components;
	}

	public String toString()
	{
		String res;
		res = "<" + super.toString();
		res += " components:"+ components + ",";
		res += ">";
		
		return res;
	}

}