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
