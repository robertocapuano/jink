package debug.gui.objectbrowser;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.GridLayout;

import debug.model.ModelManager;

import debug.gui.*;
import debug.gui.event.*;

import debug.gui.abstractlistinspector.*;

import debug.gui.model.FrontEndModel;
import debug.gui.model.FieldsFrontModel;
import debug.gui.model.MethodsFrontModel;

import tools.*;

public class ObjectBrowser extends JPanel implements GUIListener, Disposable
{
	public static final String VERSION = "0.2";
	
	protected FieldsFrontModel fields_m;
	protected MethodsFrontModel methods_m;
	protected SingleListView  fields_view, methods_view;
	protected ObjectView object_view;
	
	public ObjectBrowser( ) throws GUIException
	{
		super(new GridLayout( 3, 1, 5, 5 ) );
		setName( "Object" );
		setOpaque( true );
		
		object_view = new ObjectView( );

		fields_m = new FieldsFrontModel();
		fields_view = new SingleListView( fields_m, new FieldRenderer() );
		
		methods_m = new MethodsFrontModel();
		methods_view = new SingleListView( methods_m, new MethodRenderer() );

		object_view.addGUIListener( fields_m );
		object_view.addGUIListener( methods_m );
		
		add( object_view );
		add( fields_view ); 
		add( methods_view ); 
	}
	
	public void selected( Object selection )
	{
		object_view.selected( selection );
	}
	
	public void dispose()
	{
		object_view.removeGUIListener( fields_m );
		object_view.removeGUIListener( methods_m );
	}
	
	////	

	public void addObjectListener( GUIListener listener )
	{
		object_view.addGUIListener( listener );
	}
	
	public void removeObjectListener( GUIListener listener )
	{
		object_view.removeGUIListener( listener );
	}

	public void addMethodsListener( GUIListener listener )
	{
		methods_view.addGUIListener( listener );
	}
	
	public void removeMethodsListener( GUIListener listener )
	{
		methods_view.removeGUIListener( listener );
	}

	public void addFieldsListener( GUIListener listener )
	{
		fields_view.addGUIListener( listener );
	}
	
	public void removeFieldsListener( GUIListener listener )
	{
		fields_view.removeGUIListener( listener );
	}
	
	////
}
