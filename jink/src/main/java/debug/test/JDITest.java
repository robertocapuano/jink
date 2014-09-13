package debug.test;

import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;

import tools.*;
import debug.event.*;

import java.util.*;

public class JDITest implements JDIListener, DebugOff
{
	public JDITest()
	{
	}
	
	// JDIListener
	public boolean accessWatchpoint(AccessWatchpointEventSet e)
	{
		return false;	
	}
	
	public boolean classPrepare(ClassPrepareEventSet e)
	{
		return false;	
	}

	public boolean classUnload(ClassUnloadEventSet e)
	{
		return false;	
	}
	
	public boolean exception(ExceptionEventSet e)
	{
			return false;	
}
	
	/**
	** Applicare attenzione al caso di thread avviati senza oggetto this.
	** Di interesse sono MethodEntryEvent, MethodExitEvent, StepEvent
	*/
	public boolean locationTrigger( final LocationTriggerEventSet e)
	{		return false;	
}
	
	public boolean modificationWatchpoint(ModificationWatchpointEventSet e)
	{
		return false;	
	}
	
	public boolean threadStart( ThreadStartEventSet e)
	{		return false;	
}
	
	public boolean threadDeath(ThreadDeathEventSet e)
	{
/*		try
		{
			ThreadReference threadReference = e.getThread();

			log.log( DEBUG, "" + threadReference );
//       	Inspector.inspect( e );
			if ( threadReference.name().equals("DestroyJavaVM") )
				return;
  
			model.kill( threadReference );
		}
		catch (VMDisconnectedException vmde)
		{
			log.log( DEBUG, ""+vmde );
		}
*/							
		return false;	
	}
	

	public boolean vmDeath(VMDeathEventSet e)
	{
		return false;	
	}
	
	public boolean vmDisconnect(VMDisconnectEventSet e)
	{
//        	log.log( DEBUG, "" );
//        	com.mdcs.joi.Inspector.inspect( e );
		
		return false;	
		
	}
	
	public boolean vmStart(VMStartEventSet e)
	{
		return false;	
	}


	
}