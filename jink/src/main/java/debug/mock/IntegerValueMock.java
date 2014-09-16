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


public class IntegerValueMock implements IntegerValue
{
	int value;
	
	public IntegerValueMock( int _value )
	{
		value = _value;
	}
	
	public int value()
	{
		return value;
	}
	
	public boolean booleanValue()
	{
		return value!=0;
	}
	
	public byte byteValue()
	{
		return (byte) value;
	}
	
	public char charValue()
	{
		return (char) value;
	}
	
	public short shortValue()
	{
		return (short) value;
	}
	
	public long longValue()
	{
		return (long) value;
	}
	
	public int intValue()
	{
		return (int) value;
	}
	
	public float floatValue()
	{
		return (float) value;
	}

	public double doubleValue()
	{
		return (double) value;
	}

	public Type type() 
	{
		return null;
	}
	
	public VirtualMachine virtualMachine() 
	{
		return null;
	}
	
	public String toString() 
	{
		return ""+value;
	}
	
	public int compareTo( Object that )
	{
		IntegerValueMock ivm = (IntegerValueMock) that;
		
		return value - ivm.value;
	}
			
	public boolean equals( Object that )
	{
		return super.equals( that );
	}
	
	public static void main( String[] args )
	{
		IntegerValue value = new IntegerValueMock( 34 );
		
		System.out.println( "<"+value+">");
		System.out.println( "<"+value.intValue() +">" );
	}
}
