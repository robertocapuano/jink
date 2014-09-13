package debug.gui.backendbrowser;

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

import debug.gui.model.*;

import tools.*;

public class BackEndBrowser extends JPanel implements Disposable
{
	public static final String VERSION = "0.1";
	
	protected JList model_view, dead_view;
	
	public BackEndBrowser(  FrontEndModel frontEndModel ) throws GUIException
	{
		super(new GridLayout( 2, 1, 5, 5 ) );
		setName( "BackEndModel" );
		setOpaque( true );
		
		BackEndListModel backEndListModel = frontEndModel.getBackEndListModel();
		 
		model_view = new JList( backEndListModel );
		dead_view = new JList( frontEndModel.getHistoryFrontModel() );

		add( new JScrollPane( model_view ) );
		add( new JScrollPane( dead_view ) ); 
	}
	
	public void dispose()
	{
		model_view = null;
		dead_view = null;
	}
	
}
