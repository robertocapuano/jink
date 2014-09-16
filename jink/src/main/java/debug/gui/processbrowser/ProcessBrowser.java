/* 
 * Copyright (c) 2014, Roberto Capuano <roberto@2think.it>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
