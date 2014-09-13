package debug.bridge;

import java.io.Serializable;

public class Handler implements Serializable, Cloneable
{
	public static final Handler error = new Handler( 0 );

	protected int handler;
		
	public Handler( int _handler )
	{
		handler = _handler;
	}
	
	public Handler( Object object )
	{
		this( object.hashCode() );
	}
	
	public boolean equals( Object that )
	{
		if (that!=null && that instanceof Handler )
		{
			Handler that_h = (Handler) that;
			return that_h.handler == handler;
		}
		return false;
	}

	public int hashCode()
	{
		return handler;
	}
		
	public int getHandler()
	{
		return handler;
	}
	
	public String toString()
	{
		return ""+handler;
	}
}
