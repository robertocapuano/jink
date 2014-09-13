package debug.runtime;

import javax.swing.SwingUtilities;

import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;
import com.sun.jdi.connect.*;

import java.io.*;
import java.util.*;

import debug.event.*;
//import debug.replies.*;
import debug.gate.GateManager;
import debug.exec.Reply;

import tools.*;


//import debug.bridge.*;


//import it.unisa.dia.tools.DebugOff;

import javax.swing.SwingUtilities;

public class RuntimeManager implements DebugOff
{
	public static final String VERSION = "0.4.0";
	
    protected Session session;
	protected EventDispatcher dispatcher; // = null;
    protected List jdiListeners = new LinkedList();
 	protected List addListeners = new LinkedList();
 	protected List removeListeners = new LinkedList();
 	
 	protected boolean suspended = true;
	protected boolean reqPause = false;
 	
	public RuntimeManager( String vmArgs, String className, String args ) throws RuntimeException
	{
		try
		{
			String cmdLine = className + " " + args;
			
			session = new Session( vmArgs, cmdLine );
			dispatcher = new EventDispatcher( this, session.getEventQueue() );
		}
		catch( SessionException se )
		{
			throw new RuntimeException( se );
		}
    }

	public void attach( DebuggeeConsole debuggeeConsole ) throws RuntimeException
	{
		try
		{
			session.attach( debuggeeConsole );
			dispatcher.start();
		}
		catch( SessionException se )
		{
			throw new RuntimeException( se );
		}
	}

/*	public void attach( GateManager gate )
	{
		dispatcher.start();
		session.attach( gate );
	}
*/
    /**
     ** Detach from VM.  If VM was started by debugger, terminate it.
     */
    public void detach() 
    { 
    	if (dispatcher != null )
    	{
			dispatcher.interrupt();
			dispatcher = null;
		}

    	if (session != null)
    	{
			session.detach();
			session = null;
//			invalidateThreadInfo();
//			notifySessionDeath();

		}
    }



	/**
	 ** **todo: questi tre metodi, con la relativa classe dei pendings.
	 ** I breakpoint, waitchpoint, ed exception sono richieste che hanno il seguente ciclo di vita:
	 ** 1. viene fatta la richiesta
	 ** 2. verifica se la classe  stata caricata nella vm: VirtualMachine.allClasses()
	 ** 3. se  presente viene usata la Location all'interno della classe per settarla
	 ** 4. altrimenti diventa pending, ed ad ogni evento ClassPrepare si verifica se pu˜ essere risolto.
	 */
	void addBreakpoint( String SourceFile, String SourcePath, int linenumber )
	{
	}
	
	void addWatchpoint( String SourceFile, String SourcePath, String field )
	{
	}
	
	void addException( String exceptionType_s )
	{
	}
	
	/**
	* Adds a JDIListener
	*/
	public void addJDIListener(JDIListener jl)
	{
		jdiListeners.add( jl );
	}
	
	/**
	* Removes a JDIListener
	*/
	public void removeJDIListener( JDIListener jl )
	{
		jdiListeners.remove( jl );
	}
	
	public Iterator getIteratorJDIListeners()
	{
		return jdiListeners.iterator();
	}

	
	//// RuntimeListeners
	protected List runtimeListeners = new LinkedList();
	
	public void addRuntimeListener( RuntimeListener listener )
	{
		runtimeListeners.add( listener );
	}
	
	public void removeRuntimeListener( RuntimeListener listener )
	{
		runtimeListeners.remove( listener );
	}
	
	void fireChangeEvent()
	{
		SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
        	{
				
				for ( Iterator i=runtimeListeners.iterator(); i.hasNext(); )
				{
					RuntimeListener runtimeListener = (RuntimeListener) i.next();
					runtimeListener.change( RuntimeManager.this );
				}
			}
		} );
	}
	

	// Controllo della vm debuggee

	public boolean isSuspended()
	{
		return suspended;
	}
	
	void setSuspended( boolean b )
	{
		if (b!=suspended)
		{
			suspended = b;
			fireChangeEvent();
		}
	}
	
	
	public void suspend() throws RuntimeException
	{
		if (!isSuspended())
		{
			try
			{
				setSuspended( true );
				session.suspend();
				dispatcher.suspend();
			}
			catch( SessionException se )
			{
				throw new RuntimeException( se );
			}
		}
	}

   	public void resume() throws RuntimeException
   	{
   		if (isSuspended())
   		{
			try
			{
				setSuspended( false );
				session.resume();
				dispatcher.resume();
			}
			catch( SessionException se )
			{
				throw new RuntimeException( se );
			}
		}
   	}
   	
/*	void resume( debug.event.AbstractEventSet aes ) throws RuntimeException
	{
		if (!(aes instanceof debug.event.LocationTriggerEventSet) || runMode)
		{
			resume();
		}
	}
*/	
	
	public boolean reqPause()
	{
		return reqPause;
	}
	
	public void setReqPause( boolean _reqPause )
	{
		if (reqPause!=_reqPause)
		{
			reqPause = _reqPause;
			fireChangeEvent();
		}
	}
	
	public void jump() throws RuntimeException
	{
		resume();
	}
	
	public void stepMode( ThreadReference threadRef ) throws RuntimeException
	{
		session.attachStepRequest( threadRef );
	}
	
	public void removeStepMode( ThreadReference threadRef )// throws RuntimeException
	{
		session.removeStepRequest( threadRef );
	}
	
	public void next() throws RuntimeException
	{
	}
	
	/**
	 ** ThreadReference
	 */
	public boolean canGetCurrentContendedMonitor()
	{
		return session.canGetCurrentContendedMonitor;
	}
	
	/**
	 ** ObjectReference
	 */
	public boolean canGetMonitorInfo() 
	{
		return session.canGetMonitorInfo;
	
	}
	
	/**
	 ** ThreadReference
	 */
	public boolean canGetOwnedMonitorInfo()
	{
		return session.canGetOwnedMonitorInfo;
	
	}
	
	public static void main( String[] args ) throws Exception
	{
/*		RuntimeManager runtime = new RuntimeManager( "-classpath build:libs", "debug.exec.ExecManager", "" );// "debug.bridge.server.BridgeServer", "" );

		GateManager gate = new GateManager();
		gate.attach();

		ModelManager model = new ModelManager( runtime );
		
//			debug.test.JDITest jdiTest = new debug.test.JDITest();
//			runtime.addJDIListener( jdiTest );
		

		DebuggeeConsole debuggeeConsole = new DebuggeeConsole();
//			runtime.attach( gate );
//			gate.attach( System.in, System.out );
		runtime.attach( debuggeeConsole );


		runtime.resume();
		gate.waitConnection();
		
		NewClassLoader ncl1 = new NewClassLoader( "workspace/build" );
		Reply r1 = gate.cross( ncl1 );		

		String class_s = "A";
		Object[] primitives_oa = new Object[] { new Integer(2) };
		String[] signature_sa = new String[] { "int" };
		
		NewObject nco = new NewObject( class_s,  signature_sa, primitives_oa );
		Reply r2 = gate.cross( nco );		
*/	
	}
	
  
}
//		DC.log( "" + Integer.toHexString( ((Integer)r2.getEnveloped()).intValue() ) );

    /*
     * Adds a JDIListener - at the specified position
    public void addJDIListener( int index, JDIListener jl )
    {
        jdiListeners.add(index, jl);
    }
     */
     
     
     
     
	/*
	* Adds a JDIListener
	public void addJDIListener(JDIListener jl)
	{
		synchronized( addListeners )
		{
			addListeners.add( jl );
		}	
	}
	
	* Removes a JDIListener
	public void removeJDIListener( JDIListener jl )
	{
		synchronized( removeListeners )
		{
			removeListeners.add( jl );
		}
	}
	
	public Iterator getIteratorJDIListeners()
	{
		synchronized( addListeners )
		{
			// l'unico problema  se un listeners si toglie e si aggiunge tra due invocazione di getIteratorJDIListeners()
			if (addListeners.size()>0)
			{
				jdiListeners.addAll( addListeners );
				addListeners.clear();
			}
		}
				
		synchronized( removeListeners )
		{
			if (removeListeners.size()>0)
			{
				jdiListeners.removeAll( removeListeners );
				removeListeners.clear();
			}
		}
	
		return jdiListeners.iterator();
	}
     
 */