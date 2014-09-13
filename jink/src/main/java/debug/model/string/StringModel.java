package debug.model.string;

import com.sun.jdi.*;

import debug.model.*;

public class StringModel extends DetailModel
{
	protected final StringReference stringReference;

	public StringModel( StringReference sr)
	{
		super( sr.hashCode() );
		stringReference = sr;
	}
	
	public Value getWrappedValue() 
	{
		return stringReference;
	}
	
	public void transition() throws StateException
	{
		return;
	}

	public String longDescription()
	{
		String desc = super.longDescription();
		desc += "s:" + stringReference.value();
		return desc;
	}
	
	public String toString()
	{
		return longDescription();
	}
}
