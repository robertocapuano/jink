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

import com.sun.jdi.*;
import java.util.*;
import javax.swing.AbstractListModel;

import debug.model.*;
import debug.model.thread.*;
import debug.model.event.*;
import tools.*;

import debug.gui.*;
import debug.gui.event.*;

public class ThreadFrontModel extends AbstractListModel implements GUIListener, DebugOff, Disposable
{
	protected final ModelManager modelManager;
	protected final TimeModel timeModel;
	
	protected ThreadModel threadModel = null;
	
	protected int last_timeline = 0;
	
	protected int ltime = 0;
	protected boolean continuum = true;
	
	protected TimeListener timeListener = new TimeListener()
	{
		// cambiato ltime
		public void leap( TimeEvent time_e )
		{
			ltime = time_e.ltime;
			continuum = time_e.continuum;
			update( ltime );
		}
		
		// ltime=gtime
		public void now( TimeEvent time_e )
		{
			ltime = time_e.ltime;
			continuum = time_e.continuum;
			update( ltime );
		}
		
		// cambiato gtime
		public void jump( TimeEvent time_e )
		{
		}
		
		// cambiato freeze
		public void freezed( TimeEvent time_e )
		{
		}
	};
	
	protected void update( int new_timeline)
	{
		if (new_timeline<0)
			new_timeline =0;
		fireIntervalAdded( ThreadFrontModel.this, last_timeline, new_timeline );
		last_timeline = new_timeline;
	}

	protected ModelListener modelController = new ModelListener()
	{
		public void threadNew( ThreadNewEvent threadNewEvent )
		{}
		
		public void threadJump( ThreadJumpEvent threadJumpEvent )
		{
			if (!continuum)
				return;
				
			ThreadModel event_tm = threadJumpEvent.getThreadModel();

			if ( event_tm == threadModel )
			{
				update( threadModel.timelineDepth() -1 );
				
			}
		}
		
		public void threadStep( ThreadStepEvent threadStepEvent )
		{}
		
		public void threadEnd( ThreadEndEvent threadEndEvent )
		{}
		
		public void vmLaunch( VMLaunchEvent vmLaunchEvent )
		{}
		
		public void vmQuit( VMQuitEvent vmQuitEvent )
		{}
		
		public void newHandler( NewHandlerEvent newHandlerEvent )
		{}
	};
	
	public void selected( Object selection )
	{	
		threadModel = (ThreadModel) selection;
		
		if ( threadModel != null )
		{
			int new_timeline = threadModel.timelineDepth() -1;
			if (new_timeline<0)
				new_timeline =0;
			fireContentsChanged( ThreadFrontModel.this, 0, new_timeline );
			last_timeline = new_timeline;
		}
		else
		{
			fireContentsChanged( ThreadFrontModel.this, 0, 0 );
			last_timeline = 0;
		}
	}

	ThreadFrontModel( ModelManager _modelManager, TimeModel _timeModel )
	{
		modelManager = _modelManager;
		timeModel = _timeModel;
		
		modelManager.addModelListener( modelController );
		timeModel.addTimeListener( timeListener );
		ltime = timeModel.getLTime();
		continuum = timeModel.continuum();
//		try { threadModel = findMainThreadModel(); } catch( GUIException ge ) { DC.log( LEVEL, ge ); }

	}
		
	public void dispose()
	{
		threadModel = null;
		modelManager.removeModelListener( modelController );
		timeModel.removeTimeListener( timeListener );
	}
		
	// Start AbstractListModel
	public Object getElementAt(int index )
	{
		if (threadModel == null )
		{
			return "empty list";
		}
		else
		{	
			try
			{
				return threadModel.getJump( index );
			}
			catch( OperationException ge )
			{
				DC.log( ge );
				return ge.toString();
			}
		}
	}

	public int getSize( )
	{
		if (threadModel == null)
		{
			return 0;
		}
		if (!continuum)
		{
			return Math.min( ltime, threadModel.timelineDepth() );
		}
		else	
		{
			return threadModel.timelineDepth();
		}
	}
	// end AbstractListModel
	
/*	protected static ThreadModel findMainThreadModel( ) throws GUIException
	{
		BackEndModel backEndModel = BackEndModel.getShared();
		List live_threads = backEndModel.getLiveThreads();
		
		for ( Iterator i=live_threads.iterator(); i.hasNext(); )
		{ 
			ThreadModel thread_m = (ThreadModel) i.next();

			if (thread_m.getName().equals("main") )
				return thread_m;
		}
		
		throw new GUIException();
	}
*/
} 

