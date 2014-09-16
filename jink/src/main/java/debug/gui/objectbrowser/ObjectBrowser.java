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
