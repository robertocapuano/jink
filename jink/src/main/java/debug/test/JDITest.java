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