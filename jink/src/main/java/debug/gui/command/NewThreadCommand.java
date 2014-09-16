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
import debug.bridge.*;
import debug.gui.event.*;

import debug.model.object.*;
import debug.model.classobject.*;
import debug.model.ModelException;

import tools.*;

public class NewThreadCommand extends AbstractAction implements DebugOff, GUIListener
{
	protected GateManager gateMgr;

	protected Handler object_h;
		
	public NewThreadCommand( GateManager _gateMgr )
	{
		super( " New Thread " );
		gateMgr = _gateMgr;
		object_h = null;
		setEnabled( false );
	}
	
	//// inter-gui communication :)
	public void selected( Object selection )
	{
		try
		{
			if (selection!=null && selection instanceof ObjectModel)
			{
				ObjectModel object_m = (ObjectModel) selection;
				DC.log( LEVEL, object_m );
				ClassObjectModel class_m = (ClassObjectModel)object_m.getClassModel();
				if ( object_m.isInstance() && class_m.isRunnable() )
				{
					setEnabled( true );
					object_h = object_m.getHandler();
					DC.log( LEVEL, object_h );
					return;
				}
			}
		}
		catch( ModelException me )
		{
			DC.log( me );
		}
		
		object_h = null;
		setEnabled( false );
		return;
	}
	
	////
	public void actionPerformed( ActionEvent e)
	{
		if (object_h==null)
		{
			JOptionPane.showMessageDialog(null,"no Object selected");
		}
		else
		{
			Runnable r = new Runnable()
			{
				public void run()
				{
					try
					{
						NewThread new_thread = new NewThread( object_h );
						Reply reply = gateMgr.cross( new_thread );
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



