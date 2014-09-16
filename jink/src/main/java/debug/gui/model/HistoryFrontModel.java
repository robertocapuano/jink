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
package debug.gui.model;

import java.util.*;
import javax.swing.*;

import debug.model.ModelManager;
import debug.model.BackEndModel;
import debug.model.event.*;
import debug.model.thread.*;

import tools.*;
import debug.gui.Disposable;

public class HistoryFrontModel extends AbstractListModel implements DebugOff, Disposable
{
	// core model
	protected final Vector threads;

	protected final ModelManager modelMgr;
	
	HistoryFrontModel( ModelManager _modelMgr )
	{
		modelMgr = _modelMgr;
		modelMgr.addModelListener( modelController );
		
		BackEndModel backEndModel = modelMgr.getBackEndModel();
		List dead_threads = backEndModel.getDeadThreads();
		threads = new Vector( dead_threads );
	}
	
	public void dispose()
	{
		modelMgr.removeModelListener( modelController );
		threads.clear();
	}
	
	
	protected ModelListener modelController = new ModelListener()
	{
		public void threadNew( ThreadNewEvent threadNewEvent )
		{
		}
		
		public void threadJump( ThreadJumpEvent threadJumpEvent )
		{}
		
		public void threadStep( ThreadStepEvent threadStepEvent )
		{}
		
		public void threadEnd( ThreadEndEvent threadEndEvent )
		{
			DC.log( LEVEL, ""+ threadEndEvent );
			
			ThreadModel thread_m = threadEndEvent.getThreadModel();
			threads.add( thread_m );
			fireIntervalAdded( HistoryFrontModel.this, threads.size()-1, threads.size()-1 );
		}
		
		public void vmLaunch( VMLaunchEvent vmLaunchEvent )
		{
			threads.clear();
		}
		
		public void vmQuit( VMQuitEvent vmQuitEvent )
		{
			threads.clear();
			fireContentsChanged( HistoryFrontModel.this, 0, 0 );
		}
		
		public void newHandler( NewHandlerEvent newHandlerEvent )
		{}
	};
	
	public Object getElementAt( int index )
	{
		return threads.get( index );
	}
	
	public int getSize()
	{
		return threads.size();
	}
}
