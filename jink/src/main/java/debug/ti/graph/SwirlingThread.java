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