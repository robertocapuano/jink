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

public class StackFrameMock implements StackFrame // implements StackFrame
{
	ThreadReference threadReference;
	ObjectReference self;
	// LocalVariable to Value
	Map varsToValues;// = new HashMap();

	public StackFrameMock( ThreadReference _threadReference, ObjectReference _self, Map _varsToValues)
	{
		threadReference = _threadReference;
		varsToValues = _varsToValues;
		self = _self;
	}

	public Location location ()
	{
		return null;
	}
	
	public ThreadReference thread () 
	{
		return threadReference;
	}

	public ObjectReference thisObject () 
	{
		return self;
	}

	public List visibleVariables() throws AbsentInformationException 	
	{
		return null;
	}

	public LocalVariable visibleVariableByName(String  name) throws AbsentInformationException
	{
		return null;
	}
	
	
	public Value getValue (LocalVariable  variable) 
	{
		return null;
	}
	
	public Map getValues (List  variables) 
	{
		return null;
	}
	
	public void setValue (LocalVariable  variable, Value  value) throws InvalidTypeException ,ClassNotLoadedException
	{
	}
	
	public VirtualMachine virtualMachine ()
	{
		return null;
	}
	
	public String toString() 
	{
		String res;
		
		res = "<"+ getClass() + ":";
		if (threadReference != null)
			res += "\tthreadReference: " + threadReference.hashCode() + ", ";
		if (self!= null)
			res += "\tself: " + self.hashCode() + ", ";
			
		res += "\nvarsToValues: " + varsToValues;
		res += ">";
		
		return res;
	}	
	
	public static void main( String[] args )
	{
		Map locals = new HashMap();
		LocalVariable lv = new LocalVariableMock("a",IntegerTypeMock.shared);
		locals.put( lv, IntegerTypeMock.shared );

		StackFrame frame = new StackFrameMock( null, null, locals );

		System.out.println( "" + frame );
	}
	
	
	
}