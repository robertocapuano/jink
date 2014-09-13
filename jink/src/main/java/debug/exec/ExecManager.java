package debug.exec;

import java.util.*;

import java.io.*;
import java.net.*;

import debug.bridge.*;

import tools.*;

// I Metodi di servizio sono eseguiti con il thread main
// Le invocazioni degli oggetti usando thread di lavoro.

public class ExecManager implements DebugOn
{
	public static final String VERSION = "0.2";

	private BufferedReader reader;
	private PrintWriter writer;
	
	private Map workspace = new HashMap();
	
	private final List classLoaders;
	
	public ExecManager()
	{
		classLoaders = new LinkedList();
	}
	
	private void connect()
	{
		try
		{
			Socket connection_sk = new Socket( "127.0.0.1", Bridge.JINK_PORT );
			
			InputStream in = connection_sk.getInputStream();
			OutputStream out = connection_sk.getOutputStream();
			attach( in, out );
		}
		catch (Exception e )
		{
			MDC.log( LEVEL, e );
		}
	}	

	public void attach( InputStream in, OutputStream out )
	{
		reader = new BufferedReader( new InputStreamReader( in ),1 );
		writer = new PrintWriter( new OutputStreamWriter( out  ) );
	}
			
	public void run() throws ExecException
	{
		try
		{
			MDC.log( "ExecManager started");

			while( true )
			{
				String agent_s = reader.readLine();
				MDC.log(  LEVEL, agent_s );
				Agent agent = (Agent) Bridge.decodeObject( agent_s );
				MDC.log( LEVEL, agent );
				
				Reply reply = agent.perform( this );
				MDC.log( LEVEL, reply );
				
				String reply_s = Bridge.encodeObject( reply ) ;
				MDC.log( LEVEL, reply_s );
				writer.println( reply_s );
				writer.flush();
			}
		}
		catch( IOException ioe )
		{
			throw new ExecException( ioe );
		}
		catch( BridgeException be )
		{
			throw new ExecException( be );
		}
	}
	
	/**
	 **l'ultimo classloader,i classloaders sono linkati come un albero
	 ** ed usano il delegation model.
	 */
	public ClassLoader getLastClassLoader( )
	{
		ClassLoader last = (ClassLoader) classLoaders.get( 0  );
		return last;
	}
	
	public void removeClassLoaders()
	{
		classLoaders.clear();
	}
	
	public void addClassLoader( ClassLoader classLoader )
	{
		classLoaders.add( classLoader );
	}
		
	public void addClassLoader( int pos, ClassLoader classLoader )
	{
		classLoaders.add( 0, classLoader );
	}		

	public Handler putTool( Object newObject )
	{
		Handler handler = new Handler( newObject );
		workspace.put(  handler, newObject );
		return handler;
	}
	
	public Object getTool( Handler handler )
	{
		return workspace.get( handler );
	}
	
	public Object removeObject( Handler handler )
	{
		return workspace.remove( handler );
	}
	
	public String toString()
	{
		String res = super.toString();
		res += "classLoaders: " + "[" + classLoaders.size() + "]";
		for (int i=0; i<classLoaders.size(); ++i )
			res += classLoaders.get(i);
		return res;
	}
	
	
		
	public static void main(String[] args ) 
	{
		try
		{
			Bridge bridge = new Bridge();
			ExecManager exec = new ExecManager( );// System.in, System.out );
			exec.connect();
			exec.run();
/*			Console con = new Console();
			System.setIn( con.getInputStream() );
			System.setOut( con.getOutputStream() );
			System.setErr( con.getOutputStream() );
*/
//			exec.run();
		}
		catch( Exception e )
		{
			MDC.log( e );
		}
	}
	
	
}


/*
** Cimitero del codice
				BufferedReader readek = new BufferedReader( new InputStreamReader( System.in ),1 );
				String line = readek.readLine();
				System.out.println( line );
				

*/
