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
