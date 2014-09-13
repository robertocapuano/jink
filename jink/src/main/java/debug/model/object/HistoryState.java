package debug.model.object;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.monitor.*;
import debug.model.thread.*;

/**
 ** Dopo che per N passi un oggetto è nello stato Snapshot passa allo stato
 ** History in cui non ha più riferimenti a dati.
 **
 */
public class HistoryState implements ObjectState
{
	/**
	 ** MonitorModel
	 */
//	final MonitorModel monitorModel;
	
	protected  HistoryState( SnapshotState snapshotState )
	{
	}
	
	public int getTime()
	{
		return -1;
	}
	
	/**
	 ** Dopo History per adesso non c'è niente.
	 */ 
	public ObjectState transition()
	{
		return this;
	}

	public Value getWrappedValue() throws StateException
	{
		throw new StateException();
	}
	
	public DetailModel getField( Field field ) throws StateException
	{
		throw new StateException();
	}

	public void setField( Field field, DetailModel model ) throws StateException
	{
		throw new StateException();
	}

	public Map getFields() throws StateException
	{
		throw new StateException();
	}
	
	public MonitorModel getMonitorModel() throws StateException
	{
		throw new StateException();
	}
	
	public boolean isLocked() throws StateException
	{
		throw new StateException();
	}

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

	//// Stato
	public boolean isLive()
	{
		return false;
	}
	
	public boolean isSnapshot()
	{
		return false;
	}
	
	public boolean isHistory()
	{
		return true;
	}
	
	public boolean isDead()
	{
		return false;
	}

	
	public String toString()
	{
		String res = "<" + getClass() + ": ";
		res += ">";
		
		return res;
	}

}
