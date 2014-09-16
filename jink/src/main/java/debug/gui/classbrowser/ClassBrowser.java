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
