package debug.model.classobject;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.monitor.*;

import debug.model.object.ObjectState;

/**
 ** Dopo che per N passi un oggetto è nello stato Snapshot passa allo stato
 ** History in cui non ha più riferimenti a dati.
 **
 */
public class HistoryState extends debug.model.object.HistoryState implements ClassState
{
	/**
	 ** MonitorModel
	 */
//	final MonitorModel monitorModel;
	
	HistoryState( SnapshotState snapshotState )
	{
		super( snapshotState);
	}
	
	public boolean isRunnable() throws StateException
	{
		throw new StateException();
	}
	
	/**
	 ** Dopo History per adesso non c'è niente.
	 */ 
	public ObjectState transition()
	{
		return this;
	}

	public String toString()
	{
		String res = "<" + getClass() + ": ";
		res += ">";
		
		return res;
	}

}
