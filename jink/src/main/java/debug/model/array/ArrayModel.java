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