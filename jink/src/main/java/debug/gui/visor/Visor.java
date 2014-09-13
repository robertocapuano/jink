package debug.gui.visor;

import javax.swing.*;

import java.awt.event.*;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.util.*;

import debug.gui.*;
import debug.gui.event.*;

import tools.*;

public class Visor implements GUIListener, DebugOff
{
	public final static String VERSION = "0.5";
	protected final JFrame frame;
	protected final JTabbedPane tabbedPane;
	protected final Vector tabs = new Vector();
	
	protected Map typeToTab = new HashMap();
	protected Map nameToTab = new HashMap();
	
	public Visor( JComponent[] _tabs)
	{
		frame = new JFrame( "Visor " + VERSION );
		frame.setDefaultLookAndFeelDecorated( true );
		frame.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE  );

		frame.setBounds( getBounds() );
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP ,JTabbedPane.WRAP_TAB_LAYOUT  );
		for (int i=0; i<_tabs.length; ++i)
		{
			tabbedPane.add( _tabs[i] );
			nameToTab.put( _tabs[i], _tabs[i].getName() );
			tabs.add( _tabs[i]);
		}
		
		JPanel cp = (JPanel) frame.getContentPane();
		cp.add( tabbedPane, BorderLayout.CENTER );
		
	}
	
	public void add( JComponent tab, Class type )
	{
		tabs.add( tab );
		tabbedPane.add( tab );
		typeToTab.put( type, tab );
		nameToTab.put( tab.getName(), tab );
	}
	
	public void show()
	{
//		frame.pack();
		frame.setVisible( true );
	}
	
	public void hide()
	{
		frame.setVisible( false );
	}	
	public String[] getPanes()
	{
		return (String[]) tabs.toArray( new String[0] );
	}
	
	public void selected( Object selected )
	{
		DC.log( LEVEL, selected );
		JComponent tab;
		Class type = selected.getClass();
		
		if (type==String.class)
		{
			String name = (String) selected;
			tab = (JComponent) nameToTab.get( name );
		}
		else
		{		
			tab = (JComponent) typeToTab.get( type );
		}
			
		if ( tab != null )
			tabbedPane.setSelectedComponent( tab );
	}
	
	protected Rectangle getBounds()
	{
		int x = (int) ( 0 * GUILayout.step_x ) ;
		int y = (int) ( 1 * GUILayout.step_y );
		int width = 2*GUILayout.step_x;
		int height = 5*GUILayout.step_y;
		
		Rectangle r = new Rectangle( x, y, width, height );
		return r;
	}
	
}
