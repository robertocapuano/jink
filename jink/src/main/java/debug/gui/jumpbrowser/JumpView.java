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
package debug.gui.jumpbrowser;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import debug.model.ModelManager;
import debug.model.thread.JumpModel;

import debug.gui.*;
import debug.gui.event.*;

import tools.*;

public class JumpView extends JPanel implements GUIEventsSource, GUIListener, Disposable, DebugOff
{
	public static final String VERSION = "0.2";

	//// begin listeners
	protected final List listeners = new LinkedList();
	
	public void addGUIListener( GUIListener listener )
	{
		listeners.add( listener );
	}
	
	public void removeGUIListener( GUIListener listener )
	{
		listeners.remove( listener );
	}
	
	protected void fire( Object selection )
	{
		for ( Iterator i = listeners.iterator(); i.hasNext(); )
		{
			GUIListener listener = (GUIListener) i.next();
			
			listener.selected( selection );
		}
	}
	////
	
	protected JumpModel model;
	
	public void selected( Object selection )
	{
		if (selection == null )
		{
			model = null;
			clear();
			fire( selection );
		}
		else
		if (selection instanceof JumpModel )
		{
			DC.log( LEVEL, selection );
			model = (JumpModel) selection;
			update();
			fire( selection );
		}
	};
	
	protected void clear()
	{
		removeAll();
		add( new JLabel( "time:" ) );
		add( new JLabel( "") );
		add( new JLabel( "object:" ) );
		add( new JLabel( "method:" ) );
		revalidate();
	}
	
	protected void update()
	{
		removeAll();
		add( new JLabel( "time: " + model.getTime() ) );
		add( new JLabel( model.isEnterStep() ? "invoke" : "return" ) );
		add( new JLabel( "object:" + model.getObject().longDescription() ) );
		add( new JLabel( "method:" + model.getMethod() ) );
		revalidate();
	}
	
	public JumpView(  ) throws GUIException
	{
		super( new GridLayout(5,1,5,5) );
	}

	public void dispose()
	{
	}
}
