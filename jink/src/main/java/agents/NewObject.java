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

public class NewObject implements Agent
{
	protected final Invocation invocation;
	
	public NewObject( Invocation _invocation )
	{
		invocation = _invocation;
	}
	
	// Eseguito nella vm debuggee da ExecManager.
	public Reply perform( ExecManager exec )
	{
		try
		{
			Class self_c = (Class) invocation.getSelf( exec );
			Class[] signature_ca = invocation.getSignature( exec.getLastClassLoader() );
			Object[] args_a = invocation.getArgs( exec );

			Constructor constructor_m = self_c.getDeclaredConstructor( signature_ca );
			constructor_m.setAccessible( true );
			
			Object tool = constructor_m.newInstance( args_a );
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
		String res = super.toString();
		res += "inv:" + invocation + ">";
		return res;
	}

			

	
	public static void main( String[] args ) throws Exception
	{
/*		String class_s = "A";
//		String[] signature_sa = new String[] { "I" };
		Object[] primitives_oa = new Object[] { Boolean.TRUE, new Character('a') };
		
		NewObject nco = new NewObject( class_s, primitives_oa );

		String encoded = debug.bridge.Bridge.encodeObject( nco );
		System.out.println( ">" + encoded );
		System.out.println( ">" + nco );
		Agent agent = (Agent) debug.bridge.Bridge.decodeObject( encoded );
		System.out.println( "<" + agent );
*/
	}
	
	
	
}

