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

import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

import java.util.*;

import debug.gui.event.*;
import debug.gui.model.VisModel;
import debug.gui.Disposable;

import tools.*;


public class ReferencePane extends JPanel implements DebugOff, Disposable
{
	private final static String REF_OFF_S = "Reference Off";
	private final static int REF_OFF_I = 0;
	private final static String REF_ON_S = "Reference On";
	private final static int REF_ON_I = 1;
	
	protected VisModel visModel;
	
	public ReferencePane( VisModel _visModel )
	{
		visModel = _visModel;
		setName( "ReferencePane" );
		setBorder( new TitledBorder( "ReferencePane" ) );
		
		String[] option = new String[] { REF_OFF_S, REF_ON_S };
		JComboBox combo = new JComboBox( option );
		combo.setSelectedIndex( visModel.getReference() ? REF_ON_I : REF_ON_I );
		combo.addActionListener( selectionListener );
		add( combo );		
		combo.setEnabled( true );
		
		visModel.addVisListener( visListener );
	}
	
	public void dispose()
	{
		visModel = null;
	}

	protected VisListener visListener = new VisListener()
	{
		public void monitor( boolean status )
		{
			DC.log( LEVEL, "monitor:" + status );
		}
		
			// reference
		public void reference( boolean status )
		{
			DC.log( LEVEL, "ref:"+ status );
		}
	};

	ActionListener selectionListener = new ActionListener()
	{
		public void actionPerformed( ActionEvent e)
		{
			String selection = (String) ((JComboBox)e.getSource()).getSelectedItem();
			
			visModel.setReference( selection.equals(REF_ON_S) );
		}
	};
	

}

