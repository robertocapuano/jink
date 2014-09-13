package agents;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import debug.exec.*;

import java.net.URL;
import java.io.*;
import java.net.URLClassLoader;

import tools.*;

public class AddClassLoader extends AbstractClassLoader
{
	public AddClassLoader( String[] _path_a )
	{
		super( _path_a );
	}

	public AddClassLoader( String _path )
	{
		super( _path );
	}

	ClassLoader methodSelector( ExecManager server, URL[] url_a )
	{
		ClassLoader last = server.getLastClassLoader();
		URLClassLoader classLoader = new URLClassLoader(  url_a, last );
		server.addClassLoader( 0, classLoader );
		return classLoader;
	}

	public static void main( String[] args ) throws Exception
	{
		AddClassLoader ncl = new AddClassLoader( "workspace/build" );
		String encoded = debug.bridge.Bridge.encodeObject( ncl );
		System.out.println( ">" + encoded );
//		AddClassLoader ncl2 = (AddClassLoader) debug.bridge.Bridge.decodeObject( encoded );
		System.out.println( ">" + ncl );
		Agent agent = (Agent) debug.bridge.Bridge.decodeObject( encoded );
		System.out.println( "<" + agent );
	}
	
}