package agents;

import java.lang.reflect.*;
import com.sun.jdi.*;

import debug.bridge.Handler;
import debug.exec.*;

import tools.*;

// Il constructor verrˆ eseguito sulla vm del debugger
// gli altri metodi nella vm debuggee.

public class RemoveObject implements Agent
{
	protected final Handler handler;
	
	// per adesso istanziamo solo primitives ed oggetti
	public RemoveObject( Handler _handler )
	{
		handler = _handler;
	}
	
	// Eseguito nella vm debuggee da ExecManager.
	public Reply perform( ExecManager exec )
	{
		try
		{
			Object tool = exec.removeObject( handler );
			if ( tool != null )
				return new Reply( Boolean.TRUE );
			else
				return new Reply( Boolean.FALSE );
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
		res += "handler:" + handler;
		return res;
	}

	public static void main( String[] args ) throws Exception
	{
/*		String class_s = "A";
//		String[] signature_sa = new String[] { "I" };
		Object[] primitives_oa = new Object[] { Boolean.TRUE, new Character('a') };

		NewClass nc = new NewClass( class_s );
		Reply r1 = gate.cross( nc );
		DC.log( LEVEL, r1 );
		Integer class_handler_i = (Integer) r1.getEnveloped();
		int class_handler = class_handler_i.intValue();
		NewObject no = new NewObject( class_handler, primitives_oa );

		
		String encoded = debug.bridge.Bridge.encodeObject( no );
		System.out.println( ">" + encoded );
		System.out.println( ">" + no );
		Agent agent = (Agent) debug.bridge.Bridge.decodeObject( encoded );
		System.out.println( "<" + agent );
*/	}
	
	
	
}

