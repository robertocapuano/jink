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