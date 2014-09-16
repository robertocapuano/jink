/* 
 * Copyright (c) 2014, Roberto Capuano <roberto@2think.it>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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

