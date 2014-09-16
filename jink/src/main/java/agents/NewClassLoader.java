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

import java.net.URL;
import java.io.*;
import java.net.URLClassLoader;

import debug.exec.*;

import tools.*;

public class NewClassLoader extends AbstractClassLoader
{
	public NewClassLoader( String[] _path_a )
	{
		super( _path_a );
	}

	public NewClassLoader( String _path )
	{
		super( _path );
	}

	ClassLoader methodSelector( ExecManager server, URL[] url_a )
	{
		URLClassLoader classLoader = new URLClassLoader( url_a );
		server.removeClassLoaders();
		server.addClassLoader( classLoader );
		return classLoader;
	}

	public static void main( String[] args ) throws Exception
	{
		NewClassLoader ncl = new NewClassLoader( new String[] { "workspace/build", "lib" } );
		String encoded = debug.bridge.Bridge.encodeObject( ncl );
		System.out.println( ">" + encoded );
//		NewClassLoader ncl2 = (NewClassLoader) debug.bridge.Bridge.decodeObject( encoded );
		System.out.println( ">" + ncl );
		Agent agent = (Agent) debug.bridge.Bridge.decodeObject( encoded );
		System.out.println( "<" + agent );
	}
	
}