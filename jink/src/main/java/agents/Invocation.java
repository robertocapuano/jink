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
package agents;

import java.io.Serializable;

import debug.bridge.*;

import debug.exec.ExecManager;

import java.lang.reflect.Method;

import tools.*;

public class Invocation implements Serializable, DebugOff
{
	protected final String name_s;
	protected final String[] signature_a;
	protected final Handler selfHandler;
	protected final Serializable[] args_a;
	protected final boolean instance;
	
	public Invocation( Handler _handler, String[] _signature_a, Serializable[] _args_a )
	{
		this( _handler, "<init>", _signature_a, _args_a, true );
	}

	public Invocation( Handler _handler, String _name_s, String[] _signature_a, Serializable[] _args_a )
	{
		this( _handler, _name_s, _signature_a, _args_a, true );	
	}

	public Invocation( Handler _handler, String _name_s, String[] _signature_a, Serializable[] _args_a, boolean _instance )
	{
		selfHandler = _handler;
		name_s = _name_s;
		signature_a = _signature_a;
		args_a = _args_a;
		instance = _instance;
	}

	public Object getSelf( ExecManager exec )
	{
		return exec.getTool( selfHandler );
	}
	
	public String getName()
	{
		return name_s;
	}
	
	public Class[] getSignature( ClassLoader cl ) throws BridgeException
	{
		for (int i=0; i<signature_a.length; ++i )
		{
			MDC.log( LEVEL, signature_a[i] );
		}
		
		try
		{
			Class[] types = new Class[signature_a.length];
			
			for ( int i=0;  i<signature_a.length; ++i )
			{
				Class type;
				
				if (signature_a.equals("boolean")) { type = Boolean.TYPE; }
				else if ( signature_a[i].equals("byte")) { type = Byte.TYPE; }
				else if ( signature_a[i].equals("char")) { type = Character.TYPE; }
				else if ( signature_a[i].equals("short")) { type = Short.TYPE; }
				else if ( signature_a[i].equals("int") ) { type = Integer.TYPE; }
				else if	( signature_a[i].equals("long")) { type = Long.TYPE; }
				else if ( signature_a[i].equals("float")) { type = Float.TYPE; }
				else if ( signature_a[i].equals("double")) { type = Double.TYPE; }
				else { 	type = Class.forName( signature_a[i], true, cl ); }
				
				types[i] = type;
			}
			
			return types;
		}
		catch( ClassNotFoundException cnfe )
		{
			throw new BridgeException( cnfe );
		}
	}
	
	public Object[] getArgs( ExecManager exec)
	{
		Object[] cargs_a = new Object[args_a.length];
		
		for ( int i=0; i<cargs_a.length; ++i )
		{
			Object arg = args_a[i];
			
			if (arg instanceof Handler)
			{
				arg = exec.getTool( (Handler) arg  );
			}
			
			cargs_a[i] = arg;
		}
		
		return cargs_a;
	}
	
	public boolean isInstance()
	{
		return instance;
	}
	
	public String toString()
	{
		String res = "<" + getClass();
		res += "args:" + args_a.length;
		res += "name:" + name_s;
		res += "sig:" + signature_a.length;
		
		res += "selfHandler:" + selfHandler;
		res += ">";
		return res;
	}
		
}