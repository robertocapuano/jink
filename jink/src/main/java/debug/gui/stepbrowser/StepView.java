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
package debug.gui.stepbrowser;

import com.sun.jdi.Location;
import com.sun.jdi.AbsentInformationException;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import debug.model.*;
import debug.model.event.*;

import debug.gui.*;
import debug.gui.event.*;

import tools.*;

class StepView extends JPanel implements Disposable, DebugOff
{
	public static final String VERSION = "0.1";
	protected ModelManager modelMgr;
	protected Location loc;
	
	StepView( ModelManager _modelMgr ) throws GUIException
	{
		super( new GridLayout(5,1,5,5) );
		modelMgr = _modelMgr;
		modelMgr.addModelListener( modelListener );
		clear();
	}

	public void dispose()
	{
		modelMgr.removeModelListener( modelListener );
		modelMgr = null;
	}
	
	protected void clear()
	{
		removeAll();
		add( new JLabel( "loc:") );
		add( new JLabel( "type:" ) );
		add( new JLabel( "line:" ) );
		add( new JLabel( "source:" ) );
		add( new JLabel( "path:" ) );
		revalidate();
	}
	
	protected void update()
	{
		removeAll();
		add( new JLabel( "loc: " + loc ) );
		add( new JLabel( "type: " + loc.declaringType() ) );
		add( new JLabel( "line: " + loc.lineNumber() ) );
		try { add( new JLabel( "source: " + loc.sourceName() ) ); }
		catch( AbsentInformationException aie )
		{  add( new JLabel( "source: ?"  ) ); }
		try { add( new JLabel( "path: " + loc.sourcePath() ) ); }
		catch( AbsentInformationException aie )
		{  add( new JLabel( "path: ?"  ) ); }
		revalidate();
	}
	
	protected ModelListener modelListener = new ModelListener()
	{
		public void threadNew( ThreadNewEvent threadNewEvent )
		{}
		
		public void threadJump( ThreadJumpEvent threadJumpEvent )
		{}
		
		public void threadStep( ThreadStepEvent threadStepEvent )
		{
			loc = threadStepEvent.getLocation();
			update();
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
