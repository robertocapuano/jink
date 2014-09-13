package debug.model;

import com.sun.jdi.*;

import java.io.Serializable;

import debug.label.Label;
import debug.label.AbstractLabel;
import debug.bridge.Handler;

import debug.model.primitive.*;

/** 
 ** Singolo elemento modellato
 */
abstract public class DetailModel extends AbstractLabel implements Serializable, Label
{
	protected final int uid;
	protected Handler handler;

	protected DetailModel( DetailModel that )
	{
		this( that.uid, that.handler );
	}
	
	protected DetailModel( int _uid, Handler _handler )
	{
		uid = _uid;
		handler = _handler;
	}
	
	protected DetailModel( int _uid )
	{
		this( _uid, null );
	}

	public int getUID()
	{
		return uid;
	}
	
	abstract public Value getWrappedValue() throws StateException, OperationException;
	abstract public void transition() throws OperationException, StateException;
	
	/**
	 ** L'handler individua gli oggetti creati dall'utenti, chiamata anche workspace.
	 */
	public boolean hasHandler()
	{
		return handler != null;
	}
	
	public void setHandler( Handler _handler )
	{
		handler = _handler;
	}

	public Handler getHandler()
	{
		return handler;
	}
	
	public String shortDescription()
	{
		String desc = tools.Stringer.extractClassName(getClass().getName()) + ' ';
		if ( hasLabel() ) desc += getLabel() + ' ';
		return desc;
	}
	
	public String longDescription()
	{
		String desc = shortDescription();
		desc += "h:" + handler + " ";
		desc += "uid:" + uid + " ";
		
		return desc;
	}
	
	
	public String toString()
	{
		return longDescription();
	}

	
}

