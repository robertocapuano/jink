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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.io.*;

import debug.mock.*;
import debug.model.monitor.*;
import debug.model.*;
import debug.model.thread.*;

import debug.bridge.Handler;

import debug.runtime.RuntimeManager;

import tools.*;

public class ObjectModel extends DetailModel implements Serializable
{
	// invocato da ModelManager
	public static void initVMFeatures( RuntimeManager runtime )
	{
	}

	/**
	 ** Stato
	 */
	protected ObjectState state;
	
	/**
	 ** Modello di classe
	 */
	protected final ObjectModel classModel;
	
	// per gli oggetti contiene i metodi di istanza
	// per i meta-oggetti contiene i metodi di classe
	protected final Method[] methods;

	/**
	 ** Stati passati
	 ** altri riferimenti sullo stato
	 ** Gli altri oggetti State associati
	 ** Verrr� usato per gli alias, stesse viste per lo stesso modello
	 ** Tutti gli alias condividono lo stesso vettore.
	 */
	final protected LinkedList alias;

	public ObjectModel( ObjectReference _reference ) throws OperationException
	{ 
		this( _reference.hashCode(),
		       (ObjectModel) BackEndModel.getShared().referenceToModel( _reference.referenceType().classObject() ),
		       new LiveState( _reference ),
		       new LinkedList(),
				_reference.referenceType().visibleMethods()
		);
	}
	
	protected ObjectModel( int _uid, ObjectModel _classModel, ObjectState _state, LinkedList _alias,  List _methods )
	{
		super( _uid );

		classModel = _classModel;
		methods = filterMethods( _methods );
		state = _state;
		alias = _alias;
		
		alias.add( this );
//		init( _classModel, _state, _alias, _methods );
	}

	public ObjectModel( ObjectModel that )
	{
		super( that );
		
//		init( that.getClassModel(), that.state, that.alias, that.methods );

		classModel = that.getClassModel();
		methods = that.methods;
		state =  that.state;
		alias = that.alias;

		alias.add( this );
	}
	
	public int getTime()
	{
		return state.getTime();
	}
	
	/**
	 ** Solo per gli sviluppatori
	 */
	protected ObjectState getState()
	{
		return state;
	}
	
	// Gestione Handler
	// Settiamo l'handler per il primo alias
	public void setHandler( Handler _handler )
	{
		super.setHandler( _handler );
		
		if ( alias.size()>1 && alias.get(1)!=this )
		{
			ObjectModel first_alias = (ObjectModel) alias.get(1);
			first_alias.setHandler( _handler);
		}
	}
	
	
	//// Alias
	// **fixme: esiste sempre un oggetto live?
	public ObjectModel getLiveModel() throws OperationException
	{
		ObjectModel possibly_live = (ObjectModel) alias.getFirst();
		if (possibly_live.isLive())
			return possibly_live;
		else
			throw new OperationException();
	}
	
	public ObjectModel getAncientAlias()
	{
		int idx = alias.size() > 1 ? 1 : 0;
		return (ObjectModel) alias.get( idx );
	}			
	
	public ObjectModel getRecentAlias()
	{
		try
		{
			return getLiveModel();
		}
		catch( OperationException oe )
		{
			int idx = alias.size() -1;
			return (ObjectModel) alias.get( idx );
		}
	}
	
	public ObjectModel getAliasAtTime( int ltime ) 
	{
		Iterator i = alias.iterator();
		ObjectModel object_m = (ObjectModel) i.next();		
		if (object_m.getTime()==ltime)
			return object_m;
		
		object_m = null;		
			
		for ( ; i.hasNext(); )
		{
			ObjectModel prev_m = object_m;
			object_m = (ObjectModel) i.next();		

			int time = object_m.getTime();
			
			if (time==ltime)
				return object_m;
			else
			if (time>ltime)
				return prev_m;
			else
				continue;
		}
		
		return object_m;
	}
	//// end Alias
	
/*	protected void init( ObjectModel _classModel, ObjectState _state, LinkedList _alias, Method[] _methods )
	{
		classModel = _classModel;
		
		methods = _methods;
	
		state = _state;
		alias = _alias;
	}
*/	
	
	protected Method[] filterMethods( List in_methods_l )
	{
		List out_methods_l = new LinkedList();
		
		for ( Iterator it=in_methods_l.iterator(); it.hasNext(); )
		{
			Method method = (Method) it.next();
			if (!method.isStatic() && !method.name().equals("<init>") )
				out_methods_l.add( method );
		}
		
		Method[] methods_a = (Method[]) out_methods_l.toArray( new Method[0] );
		return methods_a;
	}
	
	
	public ObjectModel getClassModel()
	{
		return classModel;
	}
	
	public boolean isInstance()
	{
		return true;
	}
	 
	public ObjectModel snapshot() throws StateException, OperationException
	{
		try
		{
			// usiamo questa tecnica per il polimorfismo
			Class clazz = this.getClass();
			Constructor snapper = clazz.getConstructor( new Class[] { clazz } );
			ObjectModel snap_m = (ObjectModel) snapper.newInstance( new Object[] { this } );
			// (ndp: in ObjC � solo una riga!)
			snap_m.transition();
			
			return snap_m;
		}
		catch( IllegalAccessException iae )
		{
			throw new OperationException( iae );
		}
		catch( NoSuchMethodException nsme )
		{
			throw new OperationException( nsme );
		}
		catch( InstantiationException ie)
		{
			throw new OperationException( ie );
		}
		catch( InvocationTargetException ite )
		{
			throw new OperationException( ite );
		}
	}

	public void transition() throws StateException, OperationException
	{
		state = state.transition();
	}
	
	// help method di static referenceInvalid()
	public void transitionToHistory() throws StateException, OperationException
	{
		while ( state instanceof LiveState || state instanceof SnapshotState ) 
		{
			state = state.transition();
		}
	}

	public Value getWrappedValue() throws StateException
	{
		return state.getWrappedValue();
	}
	
	public DetailModel getField( Field field ) throws StateException, OperationException
	{
		return state.getField( field );
	}
	
	public Map getFields() throws OperationException, StateException
	{
		return state.getFields();
	}
	
	public Method[] getMethodDescriptors( )
	{
		return methods;
	}
	
	public void setField( Field field, DetailModel model ) throws StateException, OperationException
	{
		state.setField( field, model );
	}
	
	public MonitorModel getMonitorModel() throws StateException
	{
		return state.getMonitorModel();
	}
	
	/**
	 ** Restituisce i thread che eseguono codice di questo oggetto.
	 ** Se l'oggetto � uno snapshot � un sigleton, altrimenti una lista.
	 */
	public List getRunners() throws StateException
	{
		return state.getRunners();
	}
	
	public void enter( ThreadModel thread_m ) throws StateException
	{
		state.enter( thread_m );
	}
	
	public void leave( ThreadModel thread_m ) throws StateException
	{
		state.leave( thread_m );
	}
	
/*
	public void setHandler( Handler _handler )
	{
		super.setHandler( _handler );
				
		for ( Iterator i=alias.iterator(); i.hasNext() ; )
		{
			ObjectModel alterego = (ObjectModel) i.next();
			alterego.setHandler( _handler );
		}
	}
*/

	public String getStateName()
	{
		return Stringer.extractClassState( getState().getClass().getName() );
	}
	
	public String shortDescription()
	{
		String desc = super.shortDescription();
		desc += classModel.shortDescription();
		return desc;
	}
	
	public String longDescription()
	{
		String desc = super.longDescription();
		desc += "state:" + getStateName();
		desc += "time:" + getTime();
		return desc;
	}
	
	
	public String toString()
	{
		return shortDescription();
	}
	
	//// Stato
	public boolean isLive()
	{
		return state.isLive();
	}
	
	public boolean isSnapshot()
	{
		return state.isSnapshot();
	}
	
	public boolean isHistory()
	{
		return state.isHistory();
	}
	
	public boolean isDead()
	{
		return state.isDead();
	}

	// usato solo per la deserializzazione
	
	private int alias_index;
	
	private void writeObject( ObjectOutputStream out ) throws IOException, ClassNotFoundException
	{
//		out.defaultWriteObject();
		try
		{
			ObjectModel snap_m = this.snapshot();
			alias_index = alias.size()-1;
			out.writeInt( alias_index );
		}
		catch( ModelException me )
		{
			DC.log( me );
		}
	}

	private void readObject( ObjectInputStream in ) throws IOException, ClassNotFoundException
	{
//		in.defaultReadObject();
		alias_index = in.readInt();
	}
	
	protected Object readResolve() throws ObjectStreamException
	{
		ObjectModel object_m = (ObjectModel) BackEndModel.getShared().uidToModel( uid );
		ObjectModel snap_m = (ObjectModel) object_m.alias.get( alias_index );
		return snap_m;
	}
	
	public static void main( String[] args ) throws Exception
	{
/*		ObjectReference mock_a, mock_b;

		ReferenceType mock_type_a, mock_type_b;
		
		{
			Field[] fields = new Field[] { new FieldMock("i") };
			Value[] values = new Value[] { new IntegerValueMock(71) };
			List fields_l = Arrays.asList( fields );
			List values_l = Arrays.asList( values );
			List methods_l = new LinkedList();
			
			mock_type_a = new ReferenceTypeMock( "MockType", fields_l, methods_l );		
			mock_a = new ObjectReferenceMock( mock_type_a, values_l );
		}

		{
			Field[] fields = new Field[] { new FieldMock("a"), new FieldMock( "b"), new FieldMock("c") };
			Value[] values = new Value[] { new IntegerValueMock(34), new IntegerValueMock(56), mock_a };
			List fields_l = Arrays.asList( fields );
			List values_l = Arrays.asList( values );
			List methods_l = new LinkedList();
			
			mock_type_b = new ReferenceTypeMock( "MockType", fields_l, methods_l );		
			mock_b = new ObjectReferenceMock( mock_type_b, values_l );
		}

		ObjectModel mockModel = new ObjectModel( mock_b );

		System.out.println( "" + mockModel );

		ObjectModel mockModelB = mockModel.newWithTransition();
		System.out.println( "" + mockModel );
		System.out.println( "" + mockModelB );
		mockModelB.transition();
		System.out.println( "" + mockModelB );
		mockModelB.transition();
		System.out.println( "" + mockModelB );
		mockModelB.transition();
		System.out.println( "" + mockModelB );
		mockModelB.transition();
		System.out.println( "" + mockModelB );
		mockModelB.transition();
		System.out.println( "" + mockModelB );
*/	
	}

}


/**
 ** Pre-live object, un oggetto per cui esiste un riferimento
 ** ma che ancora non � usato
 */

/*
class ReferenceState implements ObjectState
{
	
}
*/


