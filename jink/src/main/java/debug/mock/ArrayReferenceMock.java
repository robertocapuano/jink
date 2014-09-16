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
package debug.mock;

import java.util.*;

import com.sun.jdi.*;

public class ArrayReferenceMock implements ArrayReference
{
	List components;
	ReferenceType referenceType;
		
	public ArrayReferenceMock( ReferenceType type, List _components )
	{
		referenceType = type;
		components = _components;
	}
	
	public void disableCollection()
	{
		System.out.println( "disableCollection()" );
		return;
	}
	
	public void enableCollection()
	{
		System.out.println( "enableCollection()" );
		return;
	}
	
	public int entryCount()
	{
		return 0;
	}
	
	public int length()
	{
		return components.size();
	}
	
	public Value getValue( int index )
	{
		return (Value) components.get( index );
	}
	
	public List getValues( int index, int length )
	{
		List res = new Vector(length);
		
		for ( int i=0; i<length; ++i )
		{
			res.set( i, components.get( index + i ) );
		}
		
		return res;
	}
	
	public List getValues()
	{
		return components;
	}

	public void setValue( Field field, Value value )
	{
		return;
	}

	public void setValues (int index, List  values, int srcIndex, int length ) throws InvalidTypeException ,ClassNotLoadedException 
    {
    	return;
    }
    	
	public void setValues (List  values) throws InvalidTypeException ,ClassNotLoadedException
	{
	}

	public Value getValue( Field field )
	{
		return null;
	}
	
	public Map getValues( List fields )
	{
		return null;
	}
	
	public Value invokeMethod( ThreadReference thread, Method method, List arguments, int options )
	{
		return null;
	}
	
	public boolean isCollected()
	{
		return false;
	}
	
	public ThreadReference owningThread()
	{
		// un lock virtuale
//		return new ThreadReferenceMock( "ll" );
		return null;
	}
	
	
	public ReferenceType referenceType()
	{
		return referenceType;
	}
	
	public void setValue( int index, Value value )
	{
		components.set( index, value );
	}
	
	public long uniqueID()
	{
		return (long) hashCode();
	}
	
	
	public List waitingThreads()
	{
		return null;
	}
	
	public Type type()
	{
		return referenceType;
	}
	
	public String toString()
	{
		String res;
		
		res = "<" + getClass() + ",";
		res += "components: "+ components + ",";
		res += "type:" + referenceType;
		res += ">";
		
		return res;
	}
	
	public VirtualMachine virtualMachine()
	{
		return null;
	}

	public boolean equals( Object that )
	{
		return super.equals( that );
	}
	
	
	public static void main( String[] args )
	{
		Vector components = new Vector();
		for (int i=0; i<3; i++ )
			components.add( new IntegerValueMock(i) );
		
		ArrayReference array = new ArrayReferenceMock( null, components );
		
		System.out.println( "" + array );
	}
}
