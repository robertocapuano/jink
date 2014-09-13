package agents;

import java.lang.reflect.*;
import com.sun.jdi.*;

import debug.bridge.*;
import debug.exec.*;

import tools.*;

// Il constructor verrà eseguito sulla vm del debugger
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

				// questa riga start va tolta in quanto lo start è dato dall'utente
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
