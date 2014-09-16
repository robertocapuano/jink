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

import debug.bridge.Handler;
import debug.exec.*;

import tools.*;

// Il constructor verr� eseguito sulla vm del debugger
// gli altri metodi nella vm debuggee.

public class NewClass implements Agent
{
	private final String class_s;
	
	// per adesso istanziamo solo primitives ed oggetti
	public NewClass( String _class_s )
	{
		class_s = _class_s;
	}
	
	// Eseguito nella vm debuggee da ExecManager.
	public Reply perform( ExecManager exec )
	{
		try
		{
			ClassLoader classLoader = exec.getLastClassLoader();
			Class class_c = Class.forName( class_s, true, classLoader );
			Object tool = class_c;
			Handler handler = exec.putTool( tool );
			return new Reply( handler );
		}
		catch( Exception e )
		{
			MDC.log( e );
			return new Reply( e );
		}
	}
	
	public String toString()
	{
		String res ="";
		res += super.toString();
		res += "class_s " + class_s + ":";
		
		return res;
	}
	
	public static void main( String[] args ) throws Exception
	{
	}
}

