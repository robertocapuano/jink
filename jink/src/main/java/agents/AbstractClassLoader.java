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

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import debug.exec.*;
import debug.model.*;
import debug.bridge.Handler;

import tools.*;

//import debug.replies.*;

public abstract class AbstractClassLoader implements Agent
{
	private final String[] path_a;
	
	/**
	 ** Il constructor � usato dal debugger e readObject dal debuggee
	 */
	public AbstractClassLoader( String[] _path_a )
	{
		path_a = _path_a;
	}

	public AbstractClassLoader( String _path )
	{
		this(  new String[] { _path } );
	}
		
	abstract ClassLoader methodSelector( ExecManager server, URL[] url_a );
	
	public Reply perform( ExecManager exec )
	{
		String classpath = "";
		
		try
		{
			URL[] url_a = new URL[path_a.length];
			
			for ( int i=0; i<url_a.length; ++i )
			{
				File path_f = new File( path_a[i] );
				url_a[i] = path_f.toURL();

				if (i>0)
					classpath += ":";
				else
					classpath += path_f;
			}

//			return new Reply( classpath );
			Object tool = this.methodSelector( exec, url_a );
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
		res += "path_a: ";
		
		for ( int i=0; i<path_a.length; i++ )
			res += path_a[i] + ":";
		return res;
		
	}
	
}