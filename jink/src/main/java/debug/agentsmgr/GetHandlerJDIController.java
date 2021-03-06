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
package debug.agentsmgr;

import com.sun.jdi.*;
import com.sun.jdi.event.*;

import java.util.*;

import debug.event.*;
import debug.model.ModelManager;
import debug.bridge.Handler;

//import javax.swing.SwingUtilities;

import tools.*;


//### This is a Hack, deal with it
class GetHandlerJDIController extends JDIAdapter implements DebugOn
{
	private final ModelManager model;
	
	public GetHandlerJDIController( ModelManager _model )
	{
		model = _model;
	}
	
	public boolean locationTrigger(LocationTriggerEventSet e)
	{
		for ( Iterator i = e.iterator(); i.hasNext(); )
		{
			LocatableEvent locatableEvent = (LocatableEvent) i.next();
			
			// Ci interessano solo eventi MethodExit
			if (locatableEvent instanceof MethodExitEvent )
			{
				MethodExitEvent exitEvent = (MethodExitEvent) locatableEvent;
				ThreadReference tref = exitEvent.thread();
	
				// unroll dello stack
				try
				{
					StackFrame top = tref.frame(0);
					if (top==null)
						continue;
					
					ObjectReference self = top.thisObject();
					if (self==null)
						continue;
						
//						long uid;
						
					String class_s = self.referenceType().name();
					if (class_s==null)
						continue;

					DC.log( LEVEL, class_s );
					 
					if ( class_s.equals("agents.NewObject") ||
						 class_s.equals("agents.NewThread") ||
						 class_s.equals("agents.InvokeMethod") ||
						 class_s.equals("agents.NewClass") ||
						 class_s.equals("agents.NewClassLoader") ||
						 class_s.equals("agents.AddClassLoader") )
					{
//							uid = self.uniqueID();
					
						LocalVariable tool_lv = top.visibleVariableByName( "tool" );
						if (tool_lv ==null)
						{
							DC.log( LEVEL, "(tool_lv ==null)");
							continue;
						}	
						ObjectReference tool_or = (ObjectReference) top.getValue( tool_lv );
						if (tool_or ==null)
						{
							DC.log( LEVEL, "(tool_or ==null)");
							continue;
						}	
						
						LocalVariable handler_lv = top.visibleVariableByName( "handler" );
						if (handler_lv ==null)
						{
							DC.log( LEVEL, "(handler_lv ==null)");
							continue;
						}	
						
						ObjectReference handler_or = (ObjectReference) top.getValue( handler_lv );
						if (handler_or==null)
						{
							DC.log( LEVEL, "(handler_or == null)" );
							continue;
						}
						
						ReferenceType handler_type = handler_or.referenceType();
						Field handler_field = handler_type.fieldByName("handler");
						
						IntegerValue handler_iv = (IntegerValue) handler_or.getValue( handler_field );
						if (handler_iv ==null)
						{
							DC.log( LEVEL, "(handler_iv ==null)");
							continue;
						}	

						int handler_i = handler_iv.intValue();
						DC.log(LEVEL, "tool_or:" + tool_or + " handler_i: " + handler_i );

						if (handler_i>0)
						{
							Handler handler = new Handler( handler_i );
							model.addHandler( tool_or, handler ); 
						}
						
//						return true;
					}
					
				}
				catch( Exception ex ) { DC.log( ex); }
			} // end MethodExitEvent
		}
		return false;
	}

}