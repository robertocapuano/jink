package debug.model.object;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.primitive.*;
import debug.model.monitor.*;
import debug.model.thread.*;

import tools.*;

/**
 ** ObjectReference relativo al "this" del thread
 ** Da questo dovrebbe essere possibile analizzare gli altri oggetti.
 */
public class LiveState implements ObjectState
{
	////
	// **todo questo campo va spostato in ModelManager, per adesso l'informazione è vicino al codice
	private static boolean deepCopy = true; 
	public  static boolean isDeepCopy()
	{	return deepCopy;}
	public static void setDeepCopy( boolean _deepCopy ) // impostato in ModelManager
	{ deepCopy = _deepCopy; } 
	////
	
	protected final ObjectReference objectReference;
	protected final MonitorModel monitorModel;
	
	protected final Field[] fieldDescriptors;
	
	protected final List runners;
	
	protected LiveState( ObjectReference _objectReference ) throws OperationException
	{
		objectReference = _objectReference;
		monitorModel = new MonitorModel( objectReference );
		fieldDescriptors = initFieldDescriptors( _objectReference.referenceType() );
		runners = new LinkedList();
	}
	
	protected Field[] initFieldDescriptors( ReferenceType referenceType ) throws OperationException
	{
		try
		{
			// **todo: controllare che sia allFields() il metodo giusto qui
			List fields_l = referenceType.allFields();
			Field[] fields_a = (Field[]) fields_l.toArray( new Field[0] );
			return fields_a;		
		}
		catch (IllegalArgumentException iae )
		{
			throw new OperationException( iae );
		}
	}

	public int getTime()
	{
		return BackEndModel.getShared().getGTime();
	}
	
	/**
	 ** go to Snapshot
	 */
	public ObjectState transition() throws StateException, OperationException
	{
		SnapshotState next_state = new SnapshotState( this );
		return next_state;
	}
	
	public Value getWrappedValue()
	{
		return objectReference;
	}
	
/*
	ObjectReference getObjectReference()
	{
		return objectReference;
	}
*/	

	public DetailModel getField( Field field ) throws OperationException
	{
		try
		{
			Value value = objectReference.getValue( field );
			
			if ( value instanceof PrimitiveValue )
			{
				PrimitiveValue pv = (PrimitiveValue) value;
				return new PrimitiveModel( pv );
			}
			else
			if (value == null)
			{
				return PendingModel.getShared();
			}
			else
			{
				ObjectReference or = (ObjectReference) value;
				
				DetailModel model = BackEndModel.getShared().referenceToModel( or );
				return model;
			}
		}
		catch( IllegalArgumentException iae )
		{
			throw new OperationException( field.toString(), iae );
		}
		catch( OperationException oe )
		{
			throw new OperationException( field.toString(), oe );
		}
	}
	
	public void setField( Field field, DetailModel model ) throws OperationException, StateException
	{
		try
		{
			objectReference.setValue( field, model.getWrappedValue() );
		}
		catch( IllegalArgumentException iae )
		{
			throw new OperationException( iae );
		}
		catch( InvalidTypeException ite )
		{
			throw new OperationException( ite );
		}
		catch( ClassNotLoadedException cnle )
		{
			throw new OperationException( cnle );
		}
	}
	
	public Map getFields() throws OperationException
	{
		try
		{
			/*
			** qui ci mirrors "buoni" del tipo ByteValue
			** ed altri da gestire con più cura come ObjectReference, ThreadReference
			** questi ultimi possono generare Exceptions
			** ... per adesso rimandiamo il lavoro ...
			*/
			Map models = new HashMap();

			for ( int i=0; i<fieldDescriptors.length; ++i )
			{
				try // cerchiamo di prendere tutti i campi che non diano errori
				{
					DetailModel model;
	
					model = this.getField( fieldDescriptors[i] );
					
					models.put( fieldDescriptors[i], model );
				}
				catch( OperationException oe) { DC.log( oe ); }
			}
			
			return models;
		}
		catch (IllegalArgumentException iae )
		{
			throw new OperationException( iae );
		}
	}

	public Map getSnapFields() throws OperationException
	{
		try
		{
			/*
			** qui ci mirrors "buoni" del tipo ByteValue
			** ed altri da gestire con più cura come ObjectReference, ThreadReference
			** questi ultimi possono generare Exceptions
			** ... per adesso rimandiamo il lavoro ...
			*/
			Map models = new HashMap();

			for ( int i=0; i<fieldDescriptors.length; ++i )
			{
				DetailModel model;

				if (isDeepCopy())
					model = this.getField( fieldDescriptors[i] );
				else
					model = this.getPrimitiveFieldOrPending( fieldDescriptors[i] );
				
				models.put( fieldDescriptors[i], model );
			}
			
			return models;
		}
		catch (IllegalArgumentException iae )
		{
			throw new OperationException( iae );
		}
	}


	protected DetailModel getPrimitiveFieldOrPending( Field field ) throws OperationException
	{
		try
		{
			Value value = objectReference.getValue( field );
			
			if ( value instanceof PrimitiveValue )
			{
				PrimitiveValue pv = (PrimitiveValue) value;
				return new PrimitiveModel( pv );
			}
			else
			{
				// **todo per le stringhe possiamo anche restituire lo StringModel
				return PendingModel.getShared();
			}
		}
		catch( IllegalArgumentException iae )
		{
			throw new OperationException( iae );
		}
	}

	public boolean isLocked() throws OperationException
	{
		return monitorModel.isLocked();
	}
		
	public MonitorModel getMonitorModel()
	{
		return monitorModel;
	}

	/**
	 ** Restituisce i threads che eseguono codice di questo oggetto
	 */
	public List getRunners()
	{
		List list = new ArrayList();
		list.addAll( runners );
		return list;
	}
	
	public void enter( ThreadModel thread_m )
	{
		if (!runners.contains( thread_m ) )
			runners.add( thread_m );
	}
	
	public void leave( ThreadModel thread_m )
	{
		runners.remove( thread_m );
	}
	
	//// Stato
	public boolean isLive()
	{
		return true;
	}
	
	public boolean isSnapshot()
	{
		return false;
	}
	
	public boolean isHistory()
	{
		return false;
	}
	
	public boolean isDead()
	{
		return false;
	}
	
		
	public String toString()
	{
		String res = getClass() + ":";
		res += "objectRef: " + objectReference +",";
		res += "monitor:" + monitorModel + ",";
		return res;
	}
}
