package debug.exec;

import java.io.Serializable;

public class Reply implements Serializable
{
	protected final Serializable enveloped;
	
	public Reply( Serializable _enveloped )
	{
		enveloped = _enveloped;
	}
	
	public Serializable getEnveloped()
	{
		return enveloped;
	}
	
	public String toString()
	{
		return getEnveloped().toString() ;
	}
	
	public boolean equals( Object that )
	{
		if (! (that instanceof Reply))
			return false;
			
		Reply that_r = (Reply) that;
		return this.enveloped.equals( that_r.enveloped );
	}
}