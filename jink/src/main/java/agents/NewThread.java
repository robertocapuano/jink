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

import java.lang.reflect.*;
import com.sun.jdi.*;

import debug.bridge.*;
import debug.exec.*;

import tools.*;

// Il constructor verr� eseguito sulla vm del debugger
// gli altri metodi nella vm debuggee.

public class NewThread implements Agent, DebugOff
{
	private final Handler run_handler;
	private static int how_many_threads;

	public static String getPrefix()
	{
		return "worker-";
	}
	
	// per adesso istanziamo solo primitives ed oggetti
	public NewThread( Handler _run_handler )
	{
		run_handler = _run_handler;
	}
	
	// Eseguito nella vm debuggee da ExecManager.
	public Reply perform( ExecManager exec )
	{
		try
		{
			Object object = exec.getTool( run_handler );
			
			if ( object instanceof Runnable )
			{
				Runnable runnable = (Runnable) object;
				Thread tool = new Thread( runnable, getPrefix() + how_many_threads++ );

				// questa riga start va tolta in quanto lo start � dato dall'utente
				tool.start();
				Handler handler = exec.putTool( tool );
				return new Reply( handler );
			}
			else
			{
				return new Reply( Handler.error );
			}
		}
		catch( Exception e )
		{
			MDC.log( e );
			return new Reply(e);
		}
	}
	
	public String toString()
	{
		String res ="";
		res += super.toString();
		res += "run_handler: " + run_handler;
		return res;
	}

	public static void main( String[] args ) //throws Exception
	{
	}

}
