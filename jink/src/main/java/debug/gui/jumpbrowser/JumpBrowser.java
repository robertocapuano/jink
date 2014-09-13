package debug.gui.jumpbrowser;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.GridLayout;

import debug.model.thread.ThreadModel;
import debug.model.ModelManager;

import debug.gui.*;

import debug.gui.abstractlistinspector.*;

import debug.gui.model.FrontEndModel;
import debug.gui.model.FieldsFrontModel;
import debug.gui.model.MethodsFrontModel;
import debug.gui.event.*;

import tools.*;

public class JumpBrowser extends JPanel implements GUIListener, Disposable
{
	public static final String VERSION = "0.2";
	
	protected FieldsFrontModel fields_m;
	protected MethodsFrontModel methods_m;
	protected SingleListView  fields_view, methods_view;
	protected JumpView jump_view;
	
	public JumpBrowser( ) throws GUIException
	{
		super(new GridLayout( 3, 1, 5, 5 ) );
		setName( "Jump" );
		setOpaque( true );
		
		jump_view = new JumpView( );

		fields_m = new FieldsFrontModel();
		fields_view = new SingleListView( fields_m, new FieldRenderer() );
		
		methods_m = new MethodsFrontModel();
		methods_view = new SingleListView( methods_m, new MethodRenderer() );

		jump_view.addGUIListener( fields_m );
		jump_view.addGUIListener( methods_m );
		
		add( jump_view );
		add( fields_view ); 
		add( methods_view ); 
	}
	
	public void selected( Object selection )
	{
		jump_view.selected( selection );
	}
	
	public void dispose()
	{
		jump_view.removeGUIListener( fields_m );
		jump_view.removeGUIListener( methods_m );
	}
	
	////	

	public void addJumpListener( GUIListener listener )
	{
		jump_view.addGUIListener( listener );
	}
	
	public void removeJumpListener( GUIListener listener )
	{
		jump_view.removeGUIListener( listener );
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
