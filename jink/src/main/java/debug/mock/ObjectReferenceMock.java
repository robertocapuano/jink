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

public class ObjectReferenceMock implements ObjectReference
{
	Map map = new HashMap();

	ReferenceType referenceType;
		
	public ObjectReferenceMock( ReferenceType type, List values_l )
	{
		referenceType = type;
		List fields_l = referenceType.allFields();
		
		for ( int i=0; i<fields_l.size(); i++ )
		{
			Object field = fields_l.get(i);
			Object value = values_l.get(i);
			
			map.put( field, value );
		}
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
	
	public Value getValue( Field field )
	{
		return (Value) map.get( field );
	}
	
	public Map getValues( List fields )
	{
		Map new_map = new HashMap();
		
		for ( Iterator i = fields.iterator(); i.hasNext(); )
		{
			Field field = (Field) i.next();
			Value value = getValue( (Field) field );
			new_map.put( field, value );
		}
		
		return new_map;
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
	
	public void setValue( Field field, Value value )
	{
		map.put( field, value );
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
		res += "map: "+ map;
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
		Field[] fields = new Field[] { new FieldMock("a"), new FieldMock( "b") };
		Value[] values = new Value[] { new IntegerValueMock(34), new IntegerValueMock(56) };
		List fields_l = Arrays.asList( fields );
		List values_l = Arrays.asList( values );
		List methods_l = new LinkedList();
	
		ReferenceType rt = new ReferenceTypeMock( "MockType", fields_l, methods_l );		
		ObjectReference or = new ObjectReferenceMock( rt, values_l );
		
		System.out.println( "" + or );
		
		System.out.println( "" + or.getValue( fields[0] ) );
		System.out.println( "" + or.getValues( fields_l ) );
	}
}
