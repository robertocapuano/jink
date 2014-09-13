package debug.gui.classbrowser;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.BorderLayout;
//import java.awt.FlowLayout;

import debug.gui.*;
import debug.gui.event.*;

import debug.gui.abstractlistinspector.SingleListView;

import tools.*;

public class ClassBrowser extends JPanel implements GUIEventsSource, GUIListener
{
	public static final String VERSION = "0.1";
	
	protected ClassPathsModel cp_model;
	protected ClassesModel c_model;
	protected final SingleListView cp_view, c_view;

	public void addGUIListener( GUIListener listener )
	{
		c_view.addGUIListener( listener );
	}
	
	public void removeGUIListener( GUIListener listener )
	{
		c_view.removeGUIListener( listener );
	}
	
	
	public void selected( Object selection )
	{
		cp_model.selected( selection );
	}
	
	public ClassBrowser() throws GUIException
	{
		super( new BorderLayout() );
		setName( "ClassBrowser" );

		cp_model = new ClassPathsModel();
		cp_view = new SingleListView( cp_model, new ClassPathsCellRenderer() );
		
		c_model = new ClassesModel( );
		cp_view.addGUIListener( c_model );
		
		c_view = new SingleListView( c_model, new ClassesCellRenderer() );
		
		add( cp_view, BorderLayout.NORTH );
		add( c_view , BorderLayout.CENTER );
		
	}
	
	public void dispose()
	{
		cp_view.removeGUIListener( c_model );
	}
	
}
