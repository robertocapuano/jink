package debug.gui.workbench;

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;

import java.util.*;

import java.awt.Rectangle;
import java.awt.Color;
import java.util.Map;

import org.jgraph.JGraph;
import org.jgraph.graph.*;
import org.jgraph.event.*;

import debug.gui.*;
import debug.gui.model.*;
import debug.model.*;
import debug.gui.event.*;

import tools.*;

public class Workbench implements Disposable, DebugOn, GUIEventsSource, GUIListener
{
	public final static String VERSION = "0.2";
	protected final JFrame frame;
	protected final JTabbedPane tabbedPane;
	protected final Vector tabs = new Vector();
	
	public Workbench( JComponent[] _tabs)
	{
		frame = new JFrame( "Workbench " + VERSION );
		frame.setDefaultLookAndFeelDecorated( true );
		frame.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE  );

		frame.setBounds( getBounds() );
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP ,JTabbedPane.WRAP_TAB_LAYOUT  );
		for (int i=0; i<_tabs.length; ++i)
		{
			tabbedPane.add( _tabs[i] );
			tabs.add( _tabs[i]);
			if (tabs.get(i) instanceof GUIEventsSource )
			{
				((GUIEventsSource)tabs.get(i)).addGUIListener(this);
			}
		}
		
		JPanel cp = (JPanel) frame.getContentPane();
		cp.add( tabbedPane, BorderLayout.CENTER );
		
		frame.setVisible( true );
	}
	
	public void dispose()
	{
		for ( Iterator i=tabs.iterator(); i.hasNext(); )
		{
			Disposable d = (Disposable) i.next();
			d.dispose();
		}
	}
	
	public void show()
	{
		frame.setVisible( true );
	}
	
	public void hide()
	{
		frame.setVisible( false );
	}	


	////	
	protected LinkedList listeners = new LinkedList();

	public void addGUIListener( GUIListener listener )
	{
		listeners.add( listener );
	}
	
	public void removeGUIListener( GUIListener listener )
	{
		listeners.remove( listener );
	}
	
	public void fire( Object selection )
	{
		for ( Iterator i=listeners.iterator(); i.hasNext(); )
		{
			GUIListener listener = (GUIListener) i.next();
			listener.selected( selection );
		}
	}
	////

	public void selected( Object selection )
	{
		fire( selection );
	}
	
	protected Rectangle getBounds()
	{
		int x = (int) ( 2 * GUILayout.step_x ) ;
		int y = (int) ( 1 * GUILayout.step_y );
		int width = 5*GUILayout.step_x;
		int height = 6*GUILayout.step_y;
		
		Rectangle r = new Rectangle( x, y, width, height );
		return r;
	}
	
}
