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
package debug.gui.preferences;

import javax.swing.*;
import java.util.*;
import java.awt.event.*;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import java.util.*;

import debug.gui.*;
import debug.gui.event.*;
import debug.gui.model.*;
import debug.model.*;

public class PreferencesPanes 
{
	public static final String VERSION = "0.5";
	
	MonitorPane monitorPane;
	ReferencePane referencePane;
	TimePane timePane;
	RunPane runPane;
	DeepCopyPane deepPane;
	
	protected JFrame frame;
	
	public PreferencesPanes( JPanel[] panes )
	{
		
		frame = new JFrame("Preferences " + VERSION );
		frame.setDefaultLookAndFeelDecorated( true );
		frame.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE  );
		
		JPanel cp = (JPanel) frame.getContentPane();
		JTabbedPane tabbed = new JTabbedPane();
		for ( int i=0; i<panes.length; ++i )
		{
			tabbed.add( panes[i] );
		}

		cp.add( tabbed );
		
		Rectangle bounds = getBounds();
//		frame.setLocation( bounds.getLocation() );
		frame.setBounds( bounds );
//		frame.pack();
		frame.setVisible( true );
	}
	
	public void show()
	{
		frame.show();
	}
	
	public void hide()
	{
		frame.hide();
	}
	

	protected Rectangle getBounds()
	{
		int x = (int) ( 0 * GUILayout.step_x ) ;
		int y = (int) ( 7 * GUILayout.step_y );
		int width = 5*GUILayout.step_x;
		int height = 2*GUILayout.step_y;
		
		Rectangle r = new Rectangle( x, y, width, height );
		return r;
	}
	
}
