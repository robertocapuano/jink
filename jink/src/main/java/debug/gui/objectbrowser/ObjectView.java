package debug.gui.objectbrowser;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import debug.model.ModelManager;
import debug.model.object.ObjectModel;

import debug.gui.*;

import tools.*;
import debug.gui.event.*;

public class ObjectView extends JPanel implements GUIEventsSource, GUIListener, Disposable, DebugOff
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
	
	protected ObjectModel model;
	
	public void selected( Object selection )
	{
		if (selection == null )
		{
			model = null;
			clear();
			fire( selection );
		}
		else
		if (selection instanceof ObjectModel )
		{
			DC.log( LEVEL, selection );
			model = (ObjectModel) selection;
			update();
			fire( selection );
		}
	};
	
	protected void clear()
	{
		removeAll();
		add( new JLabel( "handler:" ) );
		add( new JLabel( "uid:") );
		add( new JLabel( "state:") );
		add( new JLabel( "object:" + model ) );
		revalidate();
	}
	
	protected void update()
	{
		removeAll();
		add( new JLabel( "handler:"+ model.getHandler() ) );
		add( new JLabel( "uid:"+model.getUID()) );
		add( new JLabel( "state:"+model.getStateName() ));
		add( new JLabel( "object:" + model ) );
		revalidate();
	}
	
	public ObjectView(  ) throws GUIException
	{
		super( new GridLayout(5,1,5,5) );
	}

	public void dispose()
	{
	}
}
