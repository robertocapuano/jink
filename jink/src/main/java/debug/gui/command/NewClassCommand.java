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
package debug.gui.command;

import javax.swing.*;
import java.io.File;
import java.awt.event.*;

import agents.*;

import debug.gate.GateManager;
import debug.exec.Reply;
import debug.bridge.BridgeException;

import debug.model.classobject.*;
import debug.gui.event.*;

import tools.*;

public class NewClassCommand extends AbstractAction implements DebugOff, GUIListener
{
	protected GateManager gateMgr;

	protected String fqcn;
		
	public NewClassCommand( GateManager _gateMgr )
	{
		super( " New Class " );
		gateMgr = _gateMgr;
		fqcn = null;
	}
	
	//// inter-gui communication :)
	public void selected( Object selection )
	{
		if (selection==null)
		{
			return;
		}
		else	
		if (selection instanceof String)
		{
			fqcn = (String) selection;
			DC.log( LEVEL, fqcn );
		}
	}
	
	////
	public void actionPerformed( ActionEvent e)
	{
		if ( ((e.getModifiers()&ActionEvent.ALT_MASK) != 0) || fqcn==null)
		{
			String class_s = JOptionPane.showInputDialog("Insert fqcn:", fqcn);
			if (class_s==null)	return;
			fqcn = class_s;
		}
	
		if (fqcn!=null)
		{
			Runnable r = new Runnable()
			{
				public void run()
				{
					try
					{
						NewClass new_class = new NewClass( fqcn );
						Reply reply = gateMgr.cross( new_class );
						DC.log( LEVEL, reply );
					}
					catch( BridgeException be )
					{
						DC.log( be );
					}
				}
			};
					
			new Thread( r ).start();
		}
	}
		
}



