/* 
 * Copyright (c) 2014, Roberto Capuano <roberto@2think.it>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
	// **todo questo campo va spostato in ModelManager, per adesso l'informazione � vicino al codice
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
			** ed altri da gestire con pi� cura come ObjectReference, ThreadReference
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
			** ed altri da gestire con pi� cura come ObjectReference, ThreadReference
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
