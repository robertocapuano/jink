package debug.gui.preferences;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.util.*;

import debug.gui.*;
import debug.gui.event.*;
import debug.gui.model.*;
import debug.model.*;

public class PreferencesPanes 
{
	public static final String VERSION = "0.5";
	
	MonitorPane monitorPane;
	ReferencePane referencePane;
	TimePane timePane;
	RunPane runPane;
	DeepCopyPane deepPane;
	
	protected JFrame frame;
	
	public PreferencesPanes( JPanel[] panes )
	{
		
		frame = new JFrame("Preferences " + VERSION );
		frame.setDefaultLookAndFeelDecorated( true );
		frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE  );
		
		JPanel cp = (JPanel) frame.getContentPane();
		JTabbedPane tabbed = new JTabbedPane();
		for ( int i=0; i<panes.length; ++i )
		{
			tabbed.add( panes[i] );
		}

		cp.add( tabbed );
		
		Rectangle bounds = getBounds();
//		frame.setLocation( bounds.getLocation() );
		frame.setBounds( bounds );
//		frame.pack();
		frame.setVisible( true );
	}
	
	public void show()
	{
		frame.show();
	}
	
	public void hide()
	{
		frame.hide();
	}
	

	protected Rectangle getBounds()
	{
		int x = (int) ( 0 * GUILayout.step_x ) ;
		int y = (int) ( 7 * GUILayout.step_y );
		int width = 5*GUILayout.step_x;
		int height = 2*GUILayout.step_y;
		
		Rectangle r = new Rectangle( x, y, width, height );
		return r;
	}
	
}
