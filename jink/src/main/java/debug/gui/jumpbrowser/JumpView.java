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
