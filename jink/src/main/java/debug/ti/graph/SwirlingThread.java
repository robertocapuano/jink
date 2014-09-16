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
package debug.ti.graph;

import com.sun.jdi.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.util.Iterator;
import java.util.List;

import org.jgraph.graph.*;
import org.jgraph.JGraph;

import debug.model.*;
import debug.model.object.*;
import debug.model.thread.*;

import debug.runtime.*;

import debug.ti.frontmodel.*;

import tools.*;

class SwirlingThread implements DebugOn
{
	private final static Font font = new Font( "SansSerif", Font.BOLD, 10 );
	private final static Color[] color_cycle = new Color[] { Color.red, Color.magenta };

	protected final ObjectRenderer objectRenderer;

	protected RuntimeManager runtime;
	
	protected RuntimeListener runtimeListener = new RuntimeListener()
	{
		public void change( RuntimeManager runtime )
		{
			if ( runtime.isSuspended() )
				stopAnimation();
			else
				startAnimation();
		}
	};

	protected ActionListener timerListener = new ActionListener()
	{
		public void actionPerformed( ActionEvent e )
		{
			frameNumber = (frameNumber+1) % color_cycle.length;
	
			JGraph graph = objectRenderer.getGraph();
			graph.repaint();
		}
	};
		
	// animation section
	final double fps = 1/2;
	final int delay = 1000;
	final Timer timer = new Timer( delay, timerListener );

	int frameNumber = 0;
	
	SwirlingThread( RuntimeManager _runtime,  ObjectRenderer _objectRenderer )
	{
		runtime = _runtime;
//		runtime.addRuntimeListener( runtimeListener );
		
//		threadModel = _threadModel;
		objectRenderer = _objectRenderer;
		startAnimation();
	}
	
	
	public void startAnimation()
	{
		if (!timer.isRunning())
			timer.start();
	}
	
	public void stopAnimation()
	{
		if (timer.isRunning())
			timer.stop();
	}
	
	
	public int paint( Graphics2D g2, ObjectModel objectModel, int pos_x, int  pos_y, int width )
	{
		g2.setColor( color_cycle[ frameNumber] );
		
//		pos_y = objectRenderer.drawDesc( g2, font, objectModel.shortDescription(), pos_x, pos_y, width );

		try
		{
			List runners = objectModel.getRunners();
			
			for ( Iterator i=runners.iterator(); i.hasNext(); )
			{
				ThreadModel threadModel = (ThreadModel) i.next();
				pos_y = objectRenderer.drawDesc( g2, font, threadModel.shortDescription(), pos_x, pos_y, width );
			}
		}
		catch( StateException se ) { DC.log( LEVEL, se ); }
		
		return pos_y;
	}
	
}