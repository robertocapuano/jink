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
