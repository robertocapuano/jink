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

import com.sun.jdi.*;

public class LocalVariableMock implements LocalVariable
{
	String name;
	Type type;
	
	public LocalVariableMock( String _name, Type _type )
	{
		name = _name;
		type = _type;
	}
	
	public boolean equals (Object  obj)
	{
		return false;
	}
	
	public int hashCode() 
	{
		return super.hashCode();
	}
		
	public String name() 
	{
		return name;
	}
	
	public String typeName () 
	{
		return "";
	}
	
	public Type type() throws ClassNotLoadedException 
	{
		return type;
	}
	
	
	public String signature ()
	{
		return "";
	}
	
	public boolean isVisible (StackFrame  frame) 
	{
		return true;
	}
	
	public boolean isArgument () 
	{
		return false;
	}
	
	public int compareTo (Object  o) 
	{
		return 0;
	}


	public VirtualMachine virtualMachine () 
	{
		return null;
	}
	
	
	public String toString ()
	{
		String res;

		res = "<" + getClass() + ": ";
		res += "name:" + name + ",";
		res += "type:" + type + ">";
		return res;
	}
	
	public static void main( String[] args )
	{
		Type intType = IntegerTypeMock.shared;
		
		LocalVariable lv = new LocalVariableMock("a",intType);
		
		System.out.println( "" + lv );	
	}
}
