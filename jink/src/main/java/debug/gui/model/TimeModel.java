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

import debug.model.*;
import debug.model.event.*;

import debug.gui.event.*;

public class TimeModel
{
	ModelManager modelMgr;
	BackEndModel backEndModel;
	
	protected int gtime;
	protected int ltime;
	protected boolean freezed;
	
	TimeModel( ModelManager _modelMgr )
	{
		modelMgr = _modelMgr;
		modelMgr.addModelListener( modelListener );
		backEndModel = modelMgr.getBackEndModel();
		ltime = gtime = gtime();
	}
	
	public int gtime() { return backEndModel.getGTime(); }
	
	public void now()
	{
		if ( ltime != gtime )
		{
			gtime = gtime();
			ltime = gtime;
			fireNow( newTimeEvent() );
		}
	}
	
	public boolean isNow()
	{
		return gtime == ltime;
	}
	
	public boolean continuum()
	{
		return isNow() && ! isFreezed();
	}
	
	private void updateGTime( )
	{
		if (gtime!=gtime())
		{
			boolean continuum = continuum();
			gtime = gtime();
			fireJump( newTimeEvent() );

			if ( continuum )
				setLTime( gtime );
		}
	}
	
	public void setLTime( int _ltime )
	{
		if (_ltime!=ltime)
		{
			ltime = _ltime;
			TimeEvent te = newTimeEvent();
			fireLeap( te );
			if (ltime==gtime())
				fireNow( te );
		}
	}
	
	public int getLTime( )
	{
		return ltime;
	}
	
	public void setFreezed( boolean _freezed )
	{
		if (freezed!=_freezed)
		{
			freezed = _freezed;
			fireFreezed( newTimeEvent() );
		}
	}
	
	public boolean isFreezed()
	{
		return freezed;
	}
	
	public void dispose()
	{
		modelMgr.removeModelListener( modelListener );
	}
		
	////
	protected List listeners = new LinkedList();
	
	public void addTimeListener( TimeListener listener )
	{
		listeners.add( listener );
	}
	
	public void removeTimeListener( TimeListener listener )
	{
		listeners.remove( listener );
	}
	
	public void fireFreezed( TimeEvent te )
	{
		for ( Iterator i=listeners.iterator(); i.hasNext(); )
		{
			TimeListener listener = (TimeListener) i.next();
			listener.freezed( te );
		}
	}
	
	public void fireJump( TimeEvent te )
	{
		for ( Iterator i=listeners.iterator(); i.hasNext(); )
		{
			TimeListener listener = (TimeListener) i.next();
			listener.jump( te );
		}
	}
	
	public void fireLeap( TimeEvent te )
	{
		for ( Iterator i=listeners.iterator(); i.hasNext(); )
		{
			TimeListener listener = (TimeListener) i.next();
			listener.leap( te );
		}
	}

	public void fireNow( TimeEvent te )
	{
		for ( Iterator i=listeners.iterator(); i.hasNext(); )
		{
			TimeListener listener = (TimeListener) i.next();
			listener.now( te );
		}
	}

	TimeEvent newTimeEvent()
	{
		return new TimeEvent( gtime, ltime, freezed, continuum() );
	}

	ModelListener modelListener = new ModelListener()
	{
		public void threadNew( ThreadNewEvent threadNewEvent )
		{}
		
		public void threadJump( ThreadJumpEvent threadJumpEvent )
		{
			updateGTime();
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
}
