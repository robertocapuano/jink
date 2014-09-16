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

import java.awt.FlowLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;

import debug.gui.event.*;
import debug.gui.model.VisModel;
import debug.gui.Disposable;

public class MonitorPane extends JPanel implements Disposable
{
	protected VisModel visModel;
		
	protected ActionListener selectionListener = new ActionListener()
	{
		public void actionPerformed( ActionEvent e)
		{
			String selection = (String) ((JComboBox)e.getSource()).getSelectedItem();
			
			visModel.setMonitor( selection.equals("Monitor On") );
		}
	};

	public MonitorPane( VisModel _visModel )
	{
		visModel = _visModel;
		setName( "MonitorPane" );
		setBorder( new TitledBorder( "MonitorPane" ) );
	
		String[] option = new String[] { "Monitor Off","Monitor On" };
		JComboBox combo = new JComboBox( option );
		combo.addActionListener( selectionListener );
		add( combo );
		combo.setEnabled( visModel.immutableMonitor() );
	}

	public void dispose()
	{
		visModel = null;
	}


}
