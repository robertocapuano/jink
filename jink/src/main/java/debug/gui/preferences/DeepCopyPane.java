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
import debug.gui.Disposable;
import debug.model.*;

import tools.*;

public class DeepCopyPane extends JPanel implements DebugOn, Disposable
{
	private final static String DEEP_S = "Deep Copy";
	private final static int DEEP_I = 0;
	private final static String SHALLOW_S = "Shallow Copy";
	private final static int SHALLOW_I = 1;
	
	protected JComboBox combo;

	protected ModelManager modelMgr;
	protected boolean deepCopy;
	
	protected ActionListener selectionListener = new ActionListener()
	{
		public void actionPerformed( ActionEvent e)
		{
			String selection = (String) ((JComboBox)e.getSource()).getSelectedItem();
			boolean _deepCopy = selection.equals(DEEP_S);
			DC.log(LEVEL, "_deep:" + _deepCopy + " deep:" + deepCopy );
			if (deepCopy!=_deepCopy)
			{
				deepCopy = _deepCopy;
				DC.log(LEVEL, "deep:" + deepCopy );
				modelMgr.setDeepCopy( deepCopy );
			}
		}
	};
	
	protected ModellerListener modellerListener = new ModellerListener()
	{
		public void change( ModelManager modelMgr )
		{
			boolean _deepCopy = modelMgr.isDeepCopy();
			DC.log(LEVEL, "_deep:" + _deepCopy + " deep:" + deepCopy );
			if (deepCopy!=_deepCopy)
			{
				deepCopy = _deepCopy;
				DC.log(LEVEL, "deep:" + deepCopy );
				combo.setSelectedIndex( deepCopy ? DEEP_I : SHALLOW_I );
			}
		}
	};
	
	public DeepCopyPane( ModelManager _modelMgr  )
	{
		modelMgr = _modelMgr;

		setName( "DeepCopyPane" );
		setBorder( new TitledBorder( "DeepCopyPane" ) );
		
		String[] option = new String[] { DEEP_S, SHALLOW_S };
		combo = new JComboBox( option );
		
		deepCopy = modelMgr.isDeepCopy();
		combo.setSelectedIndex(  deepCopy ? DEEP_I : SHALLOW_I );

		combo.addActionListener( selectionListener );
		modelMgr.addModellerListener( modellerListener );
		
		add( combo );		
		combo.setEnabled( true );
	}
	
	public void dispose()
	{
		modelMgr = null;
	}
}

