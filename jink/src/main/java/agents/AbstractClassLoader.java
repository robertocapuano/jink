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
	 ** Il constructor è usato dal debugger e readObject dal debuggee
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