package agents;

import java.lang.reflect.*;
import com.sun.jdi.*;

import debug.bridge.*;
import debug.exec.*;

import tools.*;

// Il constructor verrˆ eseguito sulla vm del debugger
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

