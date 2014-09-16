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
