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
