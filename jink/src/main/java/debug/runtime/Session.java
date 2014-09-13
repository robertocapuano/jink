package debug.runtime;

import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;
import com.sun.jdi.connect.LaunchingConnector;
import com.sun.jdi.connect.Connector;
import com.sun.jdi.connect.VMStartException;
import com.sun.jdi.connect.IllegalConnectorArgumentsException;

import java.io.*;
import java.util.Map;
import javax.swing.SwingUtilities;

import debug.gate.GateManager;

import tools.*;
import java.util.*;

class Session implements DebugOff
{
	private VirtualMachine vm; // = null;
	private OutputStream upStream; // = null;
	private InputStream downStream; // = null;

//	private final static String[] entry_excludes = { "com.mindprod.*", "tools.*", "debug.exec.*","java.*", "javax.*", "sun.*", "com.sun.*", "apple.*" };
//	private final static String[] exit_excludes = { "com.mindprod.*", "tools.*", "debug.exec.*","java.*", "javax.*", "sun.*", "com.sun.*", "apple.*" };
//	private final static String[] entry_excludes = { "com.mindprod.*", "tools.*", "debug.bridge.*", "debug.event.*", "debug.exec.*","debug.gate.*","debug.gui.*","debug.link.*","debug.mock.*","debug.model.*","debug.runtime.*","debug.test.*","java.*", "javax.*", "sun.*", "com.sun.*", "apple.*" };
//	private final static String[] exit_excludes = { "com.mindprod.*", "tools.*", "debug.bridge.*", "debug.event.*", "debug.exec.*","debug.gate.*","debug.gui.*","debug.link.*","debug.mock.*","debug.model.*","debug.runtime.*","debug.test.*","java.*", "javax.*", "sun.*", "com.sun.*", "apple.*" };

	private final static String[] entry_excludes = { "com.mindprod.*", "tools.*", "debug.*", "java.*", "javax.*", "sun.*", "com.sun.*", "apple.*" };
	private final static String[] exit_excludes = { "com.mindprod.*", "tools.*", "debug.*", "java.*", "javax.*", "sun.*", "com.sun.*", "apple.*" };
	private final static String[] step_excludes = { "com.mindprod.*", "tools.*", "debug.*", "java.*", "javax.*", "sun.*", "com.sun.*", "apple.*" };
	
	protected final boolean canGetCurrentContendedMonitor, canGetMonitorInfo, canGetOwnedMonitorInfo;
	
	Session( String vmArgs, String cmdLine ) throws SessionException
	{
		try
		{
			vm = spawn( vmArgs, cmdLine );
			
			canGetCurrentContendedMonitor = vm.canGetCurrentContendedMonitor();
			canGetMonitorInfo = vm.canGetMonitorInfo();
			canGetOwnedMonitorInfo = vm.canGetOwnedMonitorInfo();
			
			upStream = vm.process().getOutputStream();
			downStream = vm.process().getInputStream();
			
			attachEventRequests();
		}
		catch( SessionException se )
		{
			detach();
			throw se;
		}	
	}
	
/*	void attach( GateManager gate )
	{
		gate.attach( downStream, upStream );
	}
*/	
	void attach( DebuggeeConsole debuggeeConsole ) throws SessionException
	{
		try
		{
			debuggeeConsole.attach( downStream, upStream );
		}
		catch( IOException ioe )
		{
			throw new SessionException( ioe );
		}
		
	}

	// Stop
    void detach()
    {
    	if ( downStream != null )
    	{
    		try { downStream.close(); } catch( IOException ioe ) { DC.log( ioe ); }
    		downStream = null;
    	}
    	
    	if ( upStream != null )
    	{
    		try { upStream.close();  } catch( IOException ioe ) { DC.log( ioe ); }
    		upStream = null;
    	}

		if ( vm != null )
		{
			try
			{
				vm.dispose();
			}
			catch (VMDisconnectedException ee) {}
		}
			
		if ( vm.process() != null )
		{
			vm.process().destroy();
		}
    }

	private VirtualMachine spawn( String vmArgs, String cmdLine ) throws SessionException
	{
		VirtualMachine vm;
		
        try
        {
			VirtualMachineManager manager = Bootstrap.virtualMachineManager();

			LaunchingConnector connector = manager.defaultConnector();
			
			Map arguments = connector.defaultArguments();
			DC.log(LEVEL, arguments );
			((Connector.Argument)arguments.get("options")).setValue(vmArgs);
			((Connector.Argument)arguments.get("main")).setValue(cmdLine);
        
            vm = connector.launch( arguments );
            return vm;
        }
        catch (IOException ioe)
        {
        	throw new SessionException( ioe );
        }
        catch (IllegalConnectorArgumentsException icae )
        {
        	throw new SessionException( icae );
        }
        catch (VMStartException vmse )
        {
        	throw new SessionException( vmse );
        }
	}
	
	EventQueue getEventQueue()
	{	
		return vm.eventQueue();
	}
	
	
	private void attachEventRequests( ) throws SessionException
    {
		EventRequestManager erm = vm.eventRequestManager();
		
		ClassPrepareRequest classPrepareRequest = erm.createClassPrepareRequest();
		//### We must allow the deferred breakpoints to be resolved before
		//### we continue executing the class.  We could optimize if there
		//### were no deferred breakpoints outstanding for a particular class.
		//### Can we do this with JDI?
		classPrepareRequest.setSuspendPolicy( EventRequest.SUSPEND_ALL );
		classPrepareRequest.enable();
		
		ClassUnloadRequest classUnloadRequest = erm.createClassUnloadRequest();
		classUnloadRequest.setSuspendPolicy( EventRequest.SUSPEND_NONE );
		classUnloadRequest.enable();
		
		ThreadStartRequest threadStartRequest = erm.createThreadStartRequest();
		threadStartRequest.setSuspendPolicy(EventRequest.SUSPEND_NONE);
		threadStartRequest.enable();
		
		ThreadDeathRequest threadDeathRequest = erm.createThreadDeathRequest();
		threadDeathRequest.setSuspendPolicy(EventRequest.SUSPEND_NONE);
		threadDeathRequest.enable();
		
		ExceptionRequest exceptionRequest = erm.createExceptionRequest(null, false, true);
		exceptionRequest.setSuspendPolicy(EventRequest.SUSPEND_ALL);
		exceptionRequest.enable();

		// Added by bob
		
		// MethodEntry
		MethodEntryRequest methodEntryRequest = erm.createMethodEntryRequest();
		for ( int i=0; i<entry_excludes.length; ++i )
			methodEntryRequest.addClassExclusionFilter( entry_excludes[i] );
		methodEntryRequest.enable();
		
		//MethodExit
		MethodExitRequest methodExitRequest = erm.createMethodExitRequest();
		for ( int i=0; i<exit_excludes.length; ++i )
			methodExitRequest.addClassExclusionFilter( exit_excludes[i] );
		methodExitRequest.enable();
		
		// end of modification
    }


	public void attachStepRequest( ThreadReference threadRef ) throws RuntimeException
	{
		try
		{	
			EventRequestManager erm = vm.eventRequestManager();
			
			StepRequest stepRequest = erm.createStepRequest( threadRef, StepRequest.STEP_LINE, StepRequest.STEP_INTO );
			for ( int i=0; i<step_excludes.length; ++i )
				stepRequest.addClassExclusionFilter( step_excludes[i] );
//			stepRequest.addCountFilter( 1 );
			stepRequest.enable();
		}
		catch (	DuplicateRequestException dre )
		{ throw new RuntimeException( dre ); }
		
	}
	

	public void removeStepRequest( ThreadReference threadRef )
	{
		EventRequestManager erm = vm.eventRequestManager();
		List requests = erm.stepRequests();
		for ( Iterator i=requests.iterator(); i.hasNext(); )
		{
			StepRequest request = (StepRequest)i.next();
			if (request.thread().equals(threadRef))
			{
				erm.deleteEventRequest( request);
				break;						
			}
		}
	}

	public void resume() throws SessionException
	{
		try
		{
			vm.resume();
		}
		catch( VMDisconnectedException ee )
		{
			throw new SessionException( ee );
		}
	}
	
	public void suspend() throws SessionException
	{
		try
		{
			vm.suspend();
		}
		catch( VMDisconnectedException ee )
		{
			throw new SessionException( ee );
		}
	}
	
	
	
}
/*			List connectors = manager.launchingConnectors();
			for ( Iterator it = connectors.iterator(); it.hasNext(); )
			{
				DC.log( it.next() );
			}
			DC.log( connector );			
			DC.log( connector.defaultArguments() );			
*/			
