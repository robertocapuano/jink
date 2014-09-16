/* 
 * Copyright (c) 2014, Roberto Capuano <roberto@2think.it>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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