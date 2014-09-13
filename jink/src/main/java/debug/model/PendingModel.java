package debug.model;

//import debug.link.model.primitive.*;

import com.sun.jdi.*;

public class PendingModel extends DetailModel
{
	private static PendingModel shared = new PendingModel();
	
	public static PendingModel getShared()
	{
		return shared;
	}
	
	protected PendingModel()
	{
		super( 0 );
	}
	
	public Value getWrappedValue() throws StateException
	{
		throw new StateException();
	}
	
	public void transition() throws StateException
	{
		return;
	}
	
	public String toString()
	{
		return shortDescription();
	}
}
