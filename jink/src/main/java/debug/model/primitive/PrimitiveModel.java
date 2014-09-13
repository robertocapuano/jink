package debug.model.primitive;

import com.sun.jdi.*;

import debug.model.*;

public class PrimitiveModel extends DetailModel
{
	private PrimitiveValue primitiveValue;
	
	public PrimitiveModel( PrimitiveValue pv)
	{
		super( pv.hashCode() );
		primitiveValue = pv;
	}
	
	public Value getWrappedValue() 
	{
		return primitiveValue;
	}
	
	public void transition() throws StateException
	{
		return;
	}

	public String shortDescription()
	{
		String desc = "v:" + primitiveValue;
		return desc;
	}

	public String longDescription()
	{
		String desc = super.longDescription();
		desc += "v:" + primitiveValue;
		return desc;
	}
	
	public String toString()
	{
		return longDescription();
	}
}


