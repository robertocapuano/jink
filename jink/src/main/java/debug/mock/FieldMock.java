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

public class FieldMock implements Field
{
	String name;
	
	public FieldMock( String _name )
	{
		name = _name;
	}
	
	public boolean isTransient()
	{
		return false;
	}
	
	public boolean isVolatile()
	{
		return false;
	}
	
	public Type type()
	{
		return null;
	}
	
	public String typeName()
	{
		return null;
	}
	
	public ReferenceType declaringType()
	{
		return null;
	}

	public String name( )
	{
		return name;
	}
	
	public String signature()
	{
		return null;
	}
	
	public boolean isStatic()
	{
		return false;
	}
	
	public boolean isFinal() 
	{
		return false;
	}
	
	public boolean isSynthetic() 
	{
		return false;
	}

	public String toString()
	{
		return name;
	}
	
	public VirtualMachine virtualMachine()
	{
		return null;
	}
	
	public boolean isPackagePrivate()
	{
		return false;
	}
	
	public boolean isProtected()
	{
		return false;
	}
	
	public boolean isPublic()
	{
		return false;
	}
	
	public int modifiers()
	{
		return 0;
	}
	
	public boolean isPrivate() 
	{
		return false;
	}

	public int compareTo(Object o )
	{
		return 0;
	}

	public static void main( String[] args )
	{
		Field field = new FieldMock( "a" );
		
		System.out.println( "" + field );
		
	}	
}

