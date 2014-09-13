package debug.gui.workspacebrowser;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.GridLayout;

import debug.model.thread.ThreadModel;
import debug.model.ModelManager;

import debug.gui.abstractlistinspector.*;
import debug.gui.*;

import debug.gui.abstractlistinspector.*;

import debug.gui.model.FrontEndModel;
import debug.gui.model.WorkspaceFrontModel;
import debug.gui.model.FieldsFrontModel;
import debug.gui.model.MethodsFrontModel;
import debug.gui.event.*;

import tools.*;

public class WorkspaceBrowser extends JPanel implements GUIListener, Disposable
{
	public static final String VERSION = "0.2.1";
	
	protected WorkspaceFrontModel workspace_m;
	protected FieldsFrontModel fields_m;
	protected MethodsFrontModel methods_m;
	protected SingleListView workspace_view, fields_view, methods_view;
	
	public WorkspaceBrowser( FrontEndModel frontEndModel ) throws GUIException
	{
		super( new GridLayout( 3, 1, 5, 5 ) );
		setName( "Workspace" );
		
		workspace_m = frontEndModel.getWorkspaceFrontModel( );
		workspace_view = new SingleListView( workspace_m, new HandlerRenderer() );

		fields_m = new FieldsFrontModel();
		fields_view = new SingleListView( fields_m, new FieldRenderer() );
		
		methods_m = new MethodsFrontModel();
		methods_view = new SingleListView( methods_m, new MethodRenderer() );

		workspace_view.addGUIListener( fields_m );
		workspace_view.addGUIListener( methods_m );
		
		add( workspace_view );
		add( fields_view ); 
		add( methods_view );
	}
	
	public void selected( Object selection )
	{
		int len = workspace_m.getSize();
		
		for ( int i=0; i<len; ++i )
		{
			if ( workspace_m.getElementAt( i ).equals( selection ) )
			{
				workspace_view.setSelectedIndex( i );
				return;
			}
		}
		
		workspace_view.clearSelection();
//		workspace_view.selected(selection);
		
	}
	
	public void dispose()
	{
		workspace_view.removeGUIListener( fields_m );
		workspace_view.removeGUIListener( methods_m );
	}
	
	////	

	public void addWorkspaceListener( GUIListener listener )
	{
		workspace_view.addGUIListener( listener );
	}
	
	public void removeWorkspaceListener( GUIListener listener )
	{
		workspace_view.removeGUIListener( listener );
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
