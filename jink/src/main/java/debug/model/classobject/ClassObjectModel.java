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
package debug.model.classobject;

import com.sun.jdi.*;

import java.util.*;
import java.io.Serializable;

import debug.mock.*;
import debug.model.monitor.*;
import debug.model.*;
import debug.model.object.ObjectModel;

import debug.model.classloader.*;

import tools.*;

public class ClassObjectModel extends  ObjectModel implements DebugOff, Serializable
{
	transient protected final String class_name;
	transient protected ClassLoaderModel classLoaderModel;
	
	public ClassObjectModel( ClassObjectReference _reference ) throws OperationException
	{
		super( _reference.hashCode(),
				null,
			   new LiveState( _reference ),
			   new LinkedList(),
				_reference.reflectedType().visibleMethods()
		);
		
		class_name = _reference.reflectedType().name();
		ClassLoaderReference cl_ref = _reference.reflectedType().classLoader();
		try 
		{	classLoaderModel = (ClassLoaderModel) BackEndModel.getShared().referenceToModel( cl_ref ); }
		catch( OperationException e ) { classLoaderModel = null; } // abbiamo raggiunto la cima del delegation model dei classloader

		DC.log( LEVEL, class_name );
	}
	
	public ClassObjectModel( ClassObjectModel that ) throws OperationException
	{
		super( that );
		class_name = that.class_name;
		classLoaderModel = that.classLoaderModel;
	}
	
	protected Method[] filterMethods( List in_methods_l )
	{
		List out_methods_l = new LinkedList();
		
		for ( Iterator it=in_methods_l.iterator(); it.hasNext(); )
		{
			Method method = (Method) it.next();
			if ( method.isStatic() || method.name().equals("<init>") )
				out_methods_l.add( method );
		}
		
		Method[] methods_a = (Method[]) out_methods_l.toArray( new Method[0] );
		return methods_a;
	}


	public ObjectModel getClassModel()
	{
		return this;
	}
	
	public ClassLoaderModel getClassLoaderModel()
	{
		return classLoaderModel;
	}
	
	public String getName()
	{
		return class_name;
	}
	
	public boolean isRunnable() throws OperationException, StateException
	{
		return ((ClassState)state).isRunnable();
	}

	public boolean isInstance()
	{
		return false;
	}
	
	public String shortDescription()
	{
		return class_name;
	}
	
	public String longDescription()
	{
		String desc = class_name;
		if (classLoaderModel!=null)
			desc += classLoaderModel.shortDescription();
		return desc;
	}
	
	public String toString()
	{
		return shortDescription();
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
			
			mock_type_a = new ReferenceTypeMock( "MockType", fields_l );		
			mock_a = new ObjectReferenceMock( mock_type_a, values_l );
		}

		{
			Field[] fields = new Field[] { new FieldMock("a"), new FieldMock( "b"), new FieldMock("c") };
			Value[] values = new Value[] { new IntegerValueMock(34), new IntegerValueMock(56), mock_a };
			List fields_l = Arrays.asList( fields );
			List values_l = Arrays.asList( values );
			
			mock_type_b = new ReferenceTypeMock( "MockType", fields_l );		
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


