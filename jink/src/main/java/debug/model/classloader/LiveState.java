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
package debug.model.classloader;

import com.sun.jdi.*;
import java.util.*;

import debug.model.*;

import debug.model.object.ObjectState;
import debug.model.classobject.ClassObjectModel;

import tools.*;

public class LiveState extends debug.model.object.LiveState implements ClassLoaderState, DebugOn
{
	protected LiveState( ClassLoaderReference clr ) throws OperationException
	{
		super( clr );
	}

	public ObjectState transition() throws StateException, OperationException
	{
		return new SnapshotState( this );
	}
	
	public List getDefinedClassNames()
	{
		LinkedList classnames_l = new LinkedList();
		
		ClassLoaderReference clRef = (ClassLoaderReference) objectReference;
		
		List classes = clRef.definedClasses();
		
		for ( Iterator i=classes.iterator(); i.hasNext(); )
		{
			ReferenceType refType = (ReferenceType) i.next();
			String classname_s = refType.name();
			
			classnames_l.add( classname_s );
		}
		
		return classnames_l;
	}
	
	public List getVisibleClassNames()
	{
		LinkedList classnames_l = new LinkedList();
		ClassLoaderReference clRef = (ClassLoaderReference) objectReference;
		List classes = clRef.visibleClasses();
		
		for ( Iterator i=classes.iterator(); i.hasNext(); )
		{
			ReferenceType refType = (ReferenceType) i.next();
			String classname_s = refType.name();
			classnames_l.add( classname_s );
		}
		
		return classnames_l;
	}
	
	public List getDefinedClassModels() throws OperationException
	{
		BackEndModel backEndModel = BackEndModel.getShared();
		LinkedList classmodels_l = new LinkedList();
		
		ClassLoaderReference clRef = (ClassLoaderReference) objectReference;
		
		List classes = clRef.definedClasses();
		
		for ( Iterator i=classes.iterator(); i.hasNext(); )
		{
			ReferenceType refType = (ReferenceType) i.next();
			ClassObjectModel classModel = (ClassObjectModel) backEndModel.referenceToModel( refType.classObject() );
			classmodels_l.add( classModel );
		}
		
		return classmodels_l;
	}
	
	public List getVisibleClassModels() throws OperationException
	{
		try
		{
			BackEndModel backEndModel = BackEndModel.getShared();
			LinkedList classmodels_l = new LinkedList();
			
			ClassLoaderReference clRef = (ClassLoaderReference) objectReference;
			
			List classes = clRef.visibleClasses();
			
			for ( Iterator i=classes.iterator(); i.hasNext(); )
			{
				ReferenceType refType = (ReferenceType) i.next();
				ClassObjectModel classModel = (ClassObjectModel) backEndModel.referenceToModel( refType.classObject() );
				classmodels_l.add( classModel );
			}
			
			return classmodels_l;
		}
		catch( ClassNotPreparedException cnpe )
		{
			DC.log( cnpe );
			throw new OperationException( cnpe );
		}
	}
	
}
/*
class SnapshotState extends ClassLoaderState
{
	final int uid;
	
	Value getWrappedValue() throws StateException
	{
		throw new StateException();
	}
	
	int getUID()
	{
		return 0;
	}


}

class HistoryState extends ClassLoaderState
{
	public Value getWrappedValue() throws StateException
	{
		throw new StateException();
	}	

	int getUID()
	{
		return 0;
	}

}
*/