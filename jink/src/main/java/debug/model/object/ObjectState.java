package debug.model.object;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.monitor.*;
import debug.model.thread.*;

public interface ObjectState
{
	ObjectState transition() throws StateException, OperationException;

	int getTime();
	
	/**
	 ** Gestione Campi
	 */
	DetailModel getField( Field field ) throws StateException, OperationException;
	Map getFields() throws StateException, OperationException;

	 void setField( Field field, DetailModel value ) throws StateException, OperationException;
	/**
	 ** Resituisce l'ObjectReference se c'è
	 */
	Value getWrappedValue() throws StateException;
	
	// Sezione Monitor
	boolean isLocked() throws StateException, OperationException;
	MonitorModel getMonitorModel() throws StateException;

	// Sezione Threads.
	/**
	 ** Restituisce tutti i threads che passano per questo oggetto 
	 ** Ha senso solo per gli oggetti lives.
	 ** Altrimenti restituisce solo l'oggetto ThreadModel a cui appartiene lo snapshot.
	 ** **todo: da verificare questa logica.
	 */
	List getRunners() throws StateException;
	void enter( ThreadModel thread_m ) throws StateException;
	void leave( ThreadModel thread_m ) throws StateException;

	//// Stato
	boolean isLive();
	boolean isSnapshot();
	boolean isHistory();
	boolean isDead();

}

