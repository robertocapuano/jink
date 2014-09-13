package debug.gui.classloaderbrowser;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.GridLayout;

import debug.model.thread.ThreadModel;

import debug.gui.model.ClassesFrontModel;
import debug.gui.model.ClassLoadersFrontModel;
import debug.gui.model.FrontEndModel;

import debug.gui.*;

import debug.gui.abstractlistinspector.SingleListView;
import debug.gui.event.*;

import tools.*;

public class ClassLoaderBrowser extends JPanel implements GUIEventsSource, Disposable, GUIListener
{
	public static final String VERSION = "0.5.1";
	
	protected ClassLoadersFrontModel cl_model;
	protected ClassesFrontModel c_model;
	protected final SingleListView cl_view, c_view;

	public void addGUIListener( GUIListener listener )
	{
		c_view.addGUIListener( listener );
	}
	
	public void removeGUIListener( GUIListener listener )
	{
		c_view.removeGUIListener( listener );
	}
	
	public ClassLoaderBrowser( FrontEndModel frontEndModel ) throws GUIException
	{
		super( new GridLayout( 2, 1, 5, 5 ) );
		setName("ClassLoader");
		
//		frame.setBounds( this.getBounds() );
	
		cl_model = frontEndModel.getClassLoadersFrontModel();
		cl_view = new SingleListView( cl_model, new ClassLoadersCellRenderer() );
		
		c_model = frontEndModel.getClassesFrontModel();
		cl_view.addGUIListener( c_model );
		
		c_view = new SingleListView( c_model, new ClassesCellRenderer() );
		
		add( cl_view );
		add( c_view );
	}
	
	public void selected( Object selection )
	{
		int len =  cl_model.getSize();
		
		for ( int i=0; i<len; ++i )
		{
			if (  cl_model.getElementAt( i ).equals( selection ) )
			{
				cl_view.setSelectedIndex( i );
			}
		}
	
	}
	
	public void dispose()
	{
		cl_view.removeGUIListener( c_model );
	}
	
}
