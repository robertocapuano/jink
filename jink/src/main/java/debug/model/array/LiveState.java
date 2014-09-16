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
package debug.model.array;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.monitor.*;
import debug.model.primitive.*;
import debug.model.object.ObjectState;

class LiveState extends debug.model.object.LiveState implements ArrayState
{
	LiveState( ArrayReference ar ) throws  OperationException
	{
		super( ar );
	}

/*	public int getLength()
	{
		return ((ArrayReference)objectReference).length();
	}
*/	
	public DetailModel getComponent( int index ) throws OperationException
	{
		try
		{
			ArrayReference arrayReference = (ArrayReference) objectReference;
			Value value = arrayReference.getValue( index );

			if (value==null)
			{
				return PendingModel.getShared();
			}
			else
			if ( value instanceof PrimitiveValue )
			{
				PrimitiveValue pv = (PrimitiveValue) value;
				return new PrimitiveModel( pv );
			}
			else
			{
				ObjectReference or = (ObjectReference) value;
				DetailModel model = BackEndModel.getShared().referenceToModel( or );
				return model;
			}
			
		}
		catch( IndexOutOfBoundsException iobe )
		{
			throw new OperationException( iobe );
		}
	}
	
	public void setComponent( int index, DetailModel component ) throws OperationException, StateException
	{
		try
		{
			ArrayReference arrayReference = (ArrayReference) objectReference;
			arrayReference.setValue( index, component.getWrappedValue() );
		}
		catch( IndexOutOfBoundsException iobe )
		{
			throw new OperationException( iobe );
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

	public DetailModel[] getComponents() throws OperationException
	{
		try
		{
			ArrayReference arrayRef = (ArrayReference) objectReference;
			int len =arrayRef.length();

			DetailModel[] components_a = new DetailModel[len];
			
			for ( int i=0; i<len; i++ )
			{
				components_a[i] = getComponent( i );
			}

			return components_a;
		}
		catch ( IndexOutOfBoundsException iobe )
		{
			throw new OperationException( iobe );
		}
	}

	public DetailModel[] getSnapComponents() throws OperationException
	{
		try
		{
			ArrayReference arrayRef = (ArrayReference) objectReference;
			int len =arrayRef.length();

			DetailModel[] components_a = new DetailModel[len];
			
			for ( int i=0; i<len; i++ )
			{
				DetailModel component;
				
				if ( isDeepCopy() )
					component = getComponent( i );
				else
				{
					component = getPrimitiveComponentOrPending( i );
				}	
				components_a[i] = component;
			}

			return components_a;
		}
		catch ( IndexOutOfBoundsException iobe )
		{
			throw new OperationException( iobe );
		}
	}

	protected DetailModel getPrimitiveComponentOrPending( int index ) throws OperationException
	{
		try
		{
			ArrayReference arrayReference = (ArrayReference) objectReference;
			Value value = arrayReference.getValue( index );

			if (value==null)
			{
				return PendingModel.getShared();
			}
			if ( value instanceof PrimitiveValue )
			{
				PrimitiveValue pv = (PrimitiveValue) value;
				return new PrimitiveModel( pv );
			}
			else
			{
				return PendingModel.getShared();
			}
		}
		catch( IndexOutOfBoundsException iobe )
		{
			throw new OperationException( iobe );
		}
	}


	/**
	 ** go to Snapshot
	 */
	public ObjectState transition() throws StateException, OperationException
	{
		SnapshotState next_state = new SnapshotState( this );
		return next_state;
	}


}