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
package debug.gui.command;

import javax.swing.*;
import java.io.File;
import java.awt.event.*;

import agents.*;

import debug.gui.event.*;

import debug.model.thread.*;
import debug.model.object.*;
import debug.model.*;
import debug.model.event.*;

import debug.gui.*;

import tools.*;

public class StepSwitchCommand extends AbstractAction implements DebugOn, GUIListener, Disposable
{
	private final static String STEP_S = " Step Thread ";
	private final static String UNSTEP_S = " Cont Thread ";
	
	protected ModelManager modelMgr;

	protected ThreadModel thread_m;
		
	public StepSwitchCommand( ModelManager _modelMgr )
	{
		super( STEP_S );
		modelMgr = _modelMgr;
		modelMgr.addModelListener( modelListener );
		setEnabled( false );
	}

	public void dispose()
	{
		modelMgr.removeModelListener( modelListener );
		modelMgr = null;
		thread_m = null;
	}
		
	//// inter-gui communication :)
	public void selected( Object selection )
	{
		if (selection!=null)
			DC.log( LEVEL, selection );

		try
		{
			if (selection!=null && selection instanceof ThreadModel)
			{
				thread_m = (ThreadModel) selection;
				DC.log( LEVEL, thread_m );
				
				if (thread_m.isStepMode())
					putValue( NAME, UNSTEP_S );
				else
					putValue( NAME, STEP_S );
					
				setEnabled( true );
				return; // ok
			}
		}
		catch( StateException se ) { DC.log( se );} 
			
		thread_m  = null;
		setEnabled( false );
		return;
	}
	
	////
	public void actionPerformed( ActionEvent e)
	{
		if (thread_m!=null)
		{
			try
			{
				if (thread_m.isStepMode())
					modelMgr.removeStepMode( thread_m );
				else
					modelMgr.stepMode( thread_m );

				selected( thread_m );
			}
			catch( ModelException me )	{	DC.log( me );	}
		}
	}
		

	protected ModelListener modelListener = new ModelListener()
	{
		public void threadNew( ThreadNewEvent threadNewEvent )
		{
		}
		
		public void threadJump( ThreadJumpEvent threadJumpEvent )
		{
		}
		
		public void threadStep( ThreadStepEvent threadStepEvent )
		{
				DC.log( LEVEL, threadStepEvent );
/*			try
			{
				DC.log( LEVEL, threadStepEvent );
				ThreadModel thread_m = threadStepEvent.getThreadModel();
				modelMgr.removeStepMode( thread_m );
			}
			catch( ModelException me ) { DC.log( me ); }
	*/
		}
		
		public void threadEnd( ThreadEndEvent threadEndEvent )
		{}
		
		public void vmLaunch( VMLaunchEvent vmLaunchEvent )
		{}
		
		public void vmQuit( VMQuitEvent vmQuitEvent )
		{}
		
		public void newHandler( NewHandlerEvent newHandlerEvent )
		{}
	};


}



