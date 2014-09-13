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
