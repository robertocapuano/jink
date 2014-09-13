package debug.runtime;

import com.sun.jdi.*;
import com.sun.jdi.event.*;

import java.util.*;

import debug.event.*;

import javax.swing.SwingUtilities;

import tools.*;


class RuntimeJDIController implements JDIListener, DebugOn
{
	public boolean accessWatchpoint(AccessWatchpointEventSet e)
	{
		DC.log( LEVEL, e );
//         session.runtime.validateThreadInfo();
		return true;
	}

	public boolean classPrepare(ClassPrepareEventSet e) 
	{
		return false;
/*        	try
		{
			ReferenceType rt =  e.getReferenceType();
			DC.log( rt.sourceName() + rt.sourcePaths(null) );
		}
		catch( Exception ex )
		{}
*/
//        	DC.log( e );
//          wantInterrupt = false;
//          runtime.resolve(e.getReferenceType());
	}

	public boolean classUnload(ClassUnloadEventSet e)  {
//		DC.log( LEVEL, e );
		return false;
	}

	public boolean exception(ExceptionEventSet e)  {
		DC.log( LEVEL, e );
		return true;
	}

	public boolean locationTrigger(LocationTriggerEventSet e) 
	{
		return false;
	}

	public boolean modificationWatchpoint(ModificationWatchpointEventSet e)  {
		DC.log( LEVEL, e );
		return true;
	}

	public boolean threadDeath(ThreadDeathEventSet e)
	{
//		DC.log( LEVEL, e );
		return false;	
	}

	public boolean threadStart(ThreadStartEventSet e)
	{
		try
		{
			for ( Iterator i = e.iterator(); i.hasNext(); )
			{
				ThreadStartEvent s = (ThreadStartEvent) i.next();
				DC.log( LEVEL, s.thread().name() + "/" + s.thread().threadGroup().name() );
			}
		}
		catch( Exception ex )
		{
			DC.log( ex );
		}
		return false;	
	}

	public boolean vmDeath(VMDeathEventSet e)
	{
//		DC.log( LEVEL, e );
		//### Should have some way to notify user
		//### that VM died before the session ended.
//          wantInterrupt = false;
		return false;	
	}

	public boolean vmDisconnect(VMDisconnectEventSet e)  {
//		DC.log(LEVEL,  e );
		//### Notify user?
//          wantInterrupt = false;
//          session.runtime.endSession();
		return false;	
	}

	public boolean vmStart(VMStartEventSet e)  {
//		DC.log( LEVEL, e );
		//### Do we need to do anything with it?
//          wantInterrupt = false;
		return false;	
	}
}