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

import debug.model.*;
import debug.model.primitive.*;
import debug.model.monitor.*;

import debug.model.object.ObjectState;
import debug.model.classloader.ClassLoaderModel;

import tools.*;

/**
 ** ObjectReference relativo al "this" del thread
 ** Da questo dovrebbe essere possibile analizzare gli altri oggetti.
 */
public class LiveState extends debug.model.object.LiveState implements ClassState, DebugOn
{
	LiveState( ClassObjectReference _classReference  ) throws OperationException
	{
		super( _classReference  );
	}
	
	protected Field[] initFieldDescriptors( ReferenceType referenceType ) throws OperationException
	{
		try
		{
			// **todo: controllare che sia allFields() il metodo giusto qui
			List in_fields_l = referenceType.allFields();
			List out_fields_l = new LinkedList();
			
			for ( Iterator it=in_fields_l.iterator(); it.hasNext(); )
			{
				Field field = (Field) it.next();
				if ( field.isStatic() )
					out_fields_l.add( field );
			}
			
			Field[] fields_a = (Field[]) out_fields_l.toArray( new Field[0] );
			return fields_a;
		}
		catch (IllegalArgumentException iae )
		{
			throw new OperationException( iae );
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

	public boolean isRunnable() throws OperationException
	{
		try
		{
			ClassObjectReference classObjectRef = (ClassObjectReference) objectReference;
			ReferenceType refType = (ReferenceType) classObjectRef.reflectedType();
			if (refType instanceof ClassType)
			{
				ClassType classType = (ClassType) refType;
				
				List interfaces = classType.interfaces();
				
				for ( Iterator i=interfaces.iterator(); i.hasNext(); )
				{
					InterfaceType iType = (InterfaceType) i.next();
					if (iType.name().equals( "java.lang.Runnable" ) )
					{
						return true;
					}
				}
			}

			return false;
		}
		catch( Exception e )
		{
			throw new OperationException( e );
		}		
	}

}
