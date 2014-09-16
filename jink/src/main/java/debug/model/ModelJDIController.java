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
package debug.model;

import com.sun.jdi.*;
import com.sun.jdi.event.*;
import com.sun.jdi.request.*;

import java.util.*;

import debug.event.*;
import debug.model.thread.*;
import debug.model.object.*;

import tools.*;

// start JDIListener
class ModelJDIController implements JDIListener, DebugOff
{
	private BackEndModel repository = BackEndModel.getShared();
	protected final ModelManager modelManager;
	
	ModelJDIController( ModelManager _modelManager )
	{
		modelManager = _modelManager;
	}
	
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
	
	// funzione di sostego di locationTrigger
	private ObjectModel thisModel( StackFrame frame ) throws OperationException
	{
		ObjectReference objRef = frame.thisObject();
		if ( objRef !=null)
		{
			return (ObjectModel) repository.referenceToModel( objRef );
		}
		else
		{
			// il metodo � static
			Location loc = frame.location();
			ReferenceType refType = loc.declaringType();
			ClassObjectReference classObjRef = refType.classObject();
			return (ObjectModel) repository.referenceToModel( classObjRef );
		}
	}

	/**
	** Applicare attenzione al caso di thread avviati senza oggetto this.
	** Di interesse sono MethodEntryEvent, MethodExitEvent, StepEvent
	** Discorso threads.
	** Vi sono diversi threads in esecuzione nella VM remota.
	** escludiamo i threads di sistema (gruppo system)
	** escludiamo i threads di AWT (iniziano per AWT-)
	** dei restanti threads escludiamo le invocazioni di metodi di classi di sistema (java.*, sun.*)
	** Oss. I threads creati dall'utenti hanno radice debug.agents.NewThread.getPrefix()
	** Per il thread main che serve ad exec per eseguire i comandi remoti dell'applicazione, escludiamo i passaggi nei package debug.*
	** 
	*/
	public boolean locationTrigger( final LocationTriggerEventSet e)
	{
		boolean care = false;
		
		for ( Iterator i = e.iterator(); i.hasNext(); )
		{
			LocatableEvent locatableEvent = (LocatableEvent) i.next();

			String class_s;
			
			try
			{
				ThreadReference tref = locatableEvent.thread();
				StackFrame top = tref.frame(0);
				if (top==null)
					continue;
						
				ObjectReference self = top.thisObject();
				if (self==null)
					continue;
							
				class_s = self.referenceType().name();
				if (class_s==null)
					continue;
			}
			catch( IncompatibleThreadStateException itse )
			{
				DC.log( itse );
				return false;
			}
			
			DC.log( LEVEL, locatableEvent );
			if ( !class_s.startsWith("debug.") && !class_s.startsWith("agents.") )
			{
				if (locatableEvent instanceof MethodEntryEvent )
				{
					MethodEntryEvent methodEntryEvent = (MethodEntryEvent) locatableEvent;
					Location loc = methodEntryEvent.location();
					
					ThreadReference threadRef = methodEntryEvent.thread();
					ThreadGroupReference groupRef = threadRef.threadGroup();
	
					if ( groupRef.name().equals( "system" ) )
					{
						// andiamo al prossimo
						continue;
					}
					
					try
					{
						if ( threadRef.frameCount() > 0 )
						{
							Method method = methodEntryEvent.method();
							ObjectModel self_m = thisModel( threadRef.frame(0) );
							ThreadModel thread_m = (ThreadModel) repository.referenceToModel( threadRef );

							DC.log( LEVEL, "t>" + thread_m + "s>" + self_m + " m>" + method );
							thread_m.doEnterObject( self_m, method );
							modelManager.dispatchThreadJumpEvent( thread_m, loc );

							care = true;
						}
					}
					catch( IncompatibleThreadStateException itse )
					{
						DC.log( itse );
					}
					catch( StateException se )
					{
						DC.log( se );
					}
					catch( OperationException oe )
					{
						DC.log( oe );
					}
				} // end MethodEntryEvent
				else
				if (locatableEvent instanceof MethodExitEvent )
				{
					MethodExitEvent methodExitEvent = (MethodExitEvent) locatableEvent;
					Location loc = methodExitEvent.location();
					ThreadReference threadRef = methodExitEvent.thread();
	
					ThreadGroupReference groupRef = threadRef.threadGroup();
					if ( groupRef.name().equals( "system" ) )
					{
						continue;
					}
					
					try
					{
						if ( threadRef.frameCount() > 0 )
						{
							Method method = methodExitEvent.method();
							ObjectModel self_om = thisModel( threadRef.frame(0) );
							ObjectModel caller_om = thisModel( threadRef.frame(1) );
							ThreadModel thread_m = (ThreadModel) repository.referenceToModel( threadRef );

							DC.log( LEVEL, "t>" + thread_m + "s>" + self_om + " c>" + caller_om + " m>" + method );
							thread_m.doLeaveObject( self_om, caller_om, method );			
//							thread_m.doLeaveObject( self_om, self_om, method );			
							modelManager.dispatchThreadJumpEvent( thread_m, loc );
							
							care = true;
						}
					}
					catch( IncompatibleThreadStateException itse )
					{
						DC.log( itse );
					}
					catch( StateException se )
					{
						DC.log( se );
					}
					catch( OperationException oe )
					{
						DC.log( oe );
					}
				} // end MethodExitEvent
				else
				// Gestione del Breakpoint
				// Nello startup del programma ExecutionManager.run()
				if (locatableEvent instanceof BreakpointEvent)
				{
					
					// eventi non di interesse
					
	/*        			BreakpointEvent breakpointEvent = (BreakpointEvent) locatableEvent;
					Location location = (Location) breakpointEvent.location();
					ClassType breakpoint_class = (ClassType) location.declaringType();
					breakpoint_class.
	*/      			
				}
				else
				if (locatableEvent instanceof StepEvent)
				{
					try
					{
						StepEvent stepEvent = (StepEvent) locatableEvent;
						Location loc = stepEvent.location();
						ThreadReference threadRef = stepEvent.thread();
						ThreadModel thread_m = (ThreadModel) repository.referenceToModel( threadRef );
						modelManager.dispatchThreadStepEvent( thread_m, loc );
						care = true;
					}
					catch( OperationException oe )
					{
						DC.log( oe );
					}
				}
			}
		}

		return care;
				
	} // end locationTrigger()
	
	public boolean modificationWatchpoint(ModificationWatchpointEventSet e)
	{
		return false;
	}
	
	public boolean threadStart( ThreadStartEventSet e)
	{
		DC.log( LEVEL, e );
		boolean care = false;
		try
		{

			for ( Iterator i = e.iterator(); i.hasNext(); )
			{
				ThreadStartEvent tStart_e = (ThreadStartEvent) i.next();

				ThreadReference tRef = (ThreadReference) tStart_e.thread();
				
				if ( tRef.name().equals("DestroyJavaVM") )
				{
					// Questo thread � inutile monitorarlo :)
					// viene avviato alla morte della vm.
					return false;
				}
				
				ThreadModel tModel_m = (ThreadModel) repository.referenceToModel( tRef );
				DC.log( LEVEL, "");
				modelManager.dispatchThreadNewEvent( tModel_m );
				care = true;
			}
		}
		catch (VMDisconnectedException vmde)
		{
			DC.log( vmde );
		}
		catch( OperationException oe )
		{
			DC.log( oe );
		}

		return care;
	}
	
	public boolean threadDeath(ThreadDeathEventSet e)
	{
		boolean care = false;
		BackEndModel repository = BackEndModel.getShared();
		
		try
		{
			for ( Iterator i = e.iterator(); i.hasNext(); )
			{
				ThreadDeathEvent tDeath_e = (ThreadDeathEvent) i.next();

				ThreadReference tRef = (ThreadReference) tDeath_e.thread();
				if (tRef!=null)
				{
					ThreadModel tModel = repository.killThread( tRef );
					modelManager.dispatchThreadEndEvent( tModel );
					care = true;
				}
				else
				{
					DC.log( LEVEL, "");
				}
					
			}
		}
		catch (VMDisconnectedException vmde)
		{
			DC.log(vmde );
		}
		catch( OperationException oe )
		{
			DC.log( oe );
		}
		catch( StateException oe )
		{
			DC.log( oe );
		}

		return care;
	}
	

	public boolean vmDeath(VMDeathEventSet e)
	{
		modelManager.dispatchVMQuitEvent( );
		return false;
	}
	
	public boolean vmDisconnect(VMDisconnectEventSet e)
	{
		return false;
	}
	
	public boolean vmStart(VMStartEventSet e)
	{
//			log.log( DEBUG, "" );
		modelManager.dispatchVMLaunchEvent(  );
		return false;

	}
	
}