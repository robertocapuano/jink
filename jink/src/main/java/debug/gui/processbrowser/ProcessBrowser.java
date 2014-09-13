package debug.gui.processbrowser;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.GridLayout;

import debug.model.thread.ThreadModel;
import debug.model.ModelManager;

import debug.gui.model.FrontEndModel;
import debug.gui.model.ProcessFrontModel;
import debug.gui.model.HistoryFrontModel;
import debug.gui.model.ThreadFrontModel;

import debug.gui.abstractlistinspector.AbstractListInspector;
import debug.gui.*;

import debug.gui.abstractlistinspector.SingleListView;
import debug.gui.event.*;

import tools.*;

public class ProcessBrowser extends JPanel implements Disposable, GUIListener //, GUIEventsSource
{
	public static final String VERSION = "0.5";
	
	protected ProcessFrontModel process_m;
	protected HistoryFrontModel history_m;
	protected ThreadFrontModel thread_m;
	protected SingleListView process_view, history_view, thread_view;
	
	public ProcessBrowser( FrontEndModel frontEndModel ) throws GUIException
	{
		super( new GridLayout( 2, 1, 5, 5 )  );
		setName( "Process" );
		process_m = frontEndModel.getProcessFrontModel();
		process_view = new SingleListView( process_m, new ProcessCellRenderer() );

		history_m = frontEndModel.getHistoryFrontModel();
		history_view = new SingleListView( history_m, new ProcessCellRenderer() );
		
		thread_m = frontEndModel.getThreadFrontModel();
		process_view.addGUIListener( thread_m );
		
		thread_view = new SingleListView( thread_m, new ThreadCellRenderer() );

		JTabbedPane process_pane = new JTabbedPane(JTabbedPane.BOTTOM);
//		process_pane.setPreferredSize( bounds.getSize() );

		process_pane.add( "Live Threads", process_view ); //, BorderLayout.CENTER );
		process_pane.add( "Dead Threads", history_view ); //, BorderLayout.CENTER );

		add( process_pane );
		add( thread_view );
	}
	
/*

// nessuno lo usa per adesso...
	public void addGUIListener( GUIListener guiListener )
	{
		thread_view.addGUIListener( guiListener );
	}
	
	public void removeGUIListener( GUIListener guiListener )
	{
		thread_view.removeGUIListener( guiListener );
	}

*/
	public void selected( Object selection )
	{
		int len = process_m.getSize();
		
		for ( int i=0; i<len; ++i )
		{
			if ( process_m.getElementAt( i ).equals( selection ) )
			{
				process_view.setSelectedIndex( i );
			}
		}
	}
	
	public void dispose()
	{
		process_view.removeGUIListener( thread_m );
		history_view.removeGUIListener( thread_m );
	}
	

}
