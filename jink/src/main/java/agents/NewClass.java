package agents;

import java.lang.reflect.*;
import com.sun.jdi.*;

import debug.bridge.Handler;
import debug.exec.*;

import tools.*;

// Il constructor verrˆ eseguito sulla vm del debugger
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

