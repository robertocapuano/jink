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

import debug.mock.*;

import debug.model.object.ObjectModel;

public class ArrayModel extends ObjectModel
{
	transient protected final int length;
	
	public ArrayModel( ArrayReference  array_ref ) throws OperationException
	{
		super( array_ref.hashCode(),
			null,
		new LiveState( array_ref ),
		new LinkedList(),
		array_ref.referenceType().visibleMethods()
		);
		
		length = array_ref.length();
	}

	public ArrayModel( ArrayModel that )
	{
		super( that );
		length = that.length;
	}

	public DetailModel getComponent( int index ) throws StateException, OperationException
	{
		ArrayState astate = (ArrayState) state;
		return astate.getComponent( index );
	}
	
	public DetailModel[] getComponents( ) throws StateException, OperationException
	{
		return ((ArrayState) state).getComponents();
	}

	public void setComponent( int index, DetailModel component ) throws StateException, OperationException
	{
		ArrayState astate = (ArrayState) state;
		astate.setComponent( index, component );
	}

	public int getLength()
	{
		return length;
	}
	
	public String toString()
	{
		String res = super.toString();
		res += "length:"+getLength();

		return res;
	}
	
	public static void main( String[] args ) throws Exception
	{
		Vector components = new Vector();
		for (int i=0; i<3; i++ )
			components.add( new IntegerValueMock(i) );
		
		ArrayReference array = new ArrayReferenceMock( null, components );

		ArrayModel model = new ArrayModel( array );
	
		model.transition();
		model.transition();
		model.transition();
					
		System.out.println( "" + model );
	
	}
}