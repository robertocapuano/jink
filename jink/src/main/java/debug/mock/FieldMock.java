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

