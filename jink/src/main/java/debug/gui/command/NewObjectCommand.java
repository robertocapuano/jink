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

import com.sun.jdi.*;

import javax.swing.*;
import java.io.Serializable;
import java.awt.event.*;
import java.util.*;

import agents.*;

import debug.gate.GateManager;
import debug.exec.Reply;
import debug.bridge.*;

import debug.model.classobject.*;
import debug.model.object.*;
import debug.model.*;

import debug.gui.model.*;
import debug.gui.event.*;

import tools.*;

public class NewObjectCommand extends AbstractAction implements DebugOff, GUIListener
{
	protected GateManager gateMgr;
	protected Handler classobject_h;
	protected Method constructor;
	
	public NewObjectCommand( GateManager _gateMgr )
	{
		super( " New Object " );
		gateMgr = _gateMgr;
		classobject_h = null;
		constructor = null;
		setEnabled( false );
	}
	
	//// inter-gui communication :)
	public void selected( Object selection )
	{
/*		if (selection==null)
		{
			setEnabled( false );
			classobject_h = null;
			constructor = null;
			return;
		}
		else
			if (selection instanceof ClassObjectModel)
			{
				ClassObjectModel classobject_m = (ClassObjectModel) selection;
				classobject_h =classobject_m.getHandler();
			}
			else
			if (selection instanceof Method )
			{
				Method  method = (Method) selection;
				if (method.isConstructor())
					constructor = method;
				else
					constructor = null;
			}
			else
*/
		if ( selection!=null && selection instanceof MethodModel)
		{
			MethodModel inv = (MethodModel) selection;
			ObjectModel object_m = inv.getObject();
			Method method = inv.getMethod();
			
			if (!object_m.hasHandler() )
				DC.log( LEVEL, "(!object_m.hasHandler() )");
			else
			if (object_m instanceof ClassObjectModel && method.isConstructor() )
			{
				ClassObjectModel classobject_m = (ClassObjectModel) object_m;
				classobject_h = classobject_m.getHandler();
				constructor = method;
				setEnabled( true );
				return;
			}
		}

		classobject_h =null;
		constructor = null;
		setEnabled( false );

		if (selection!=null)
			DC.log( LEVEL, "" + selection + selection.getClass() );
			
	}
	
	////
	public void actionPerformed( ActionEvent e)
	{

		if (classobject_h==null)
		{
			JOptionPane.showMessageDialog(null,"no Class selected");
		}
		else
		if (constructor == null )
		{
			JOptionPane.showMessageDialog(null,"no constructor selected");
		}
		else
		{
			final String[] sig =  MethodModel.getSignature( constructor );
			final Serializable[] args = MethodInvocationDialog.getMethodParamether( sig );
			
			if (args==null) // utente ha fatto cancel
				return;
				
			Runnable r = new Runnable()
			{
				public void run()
				{
					try
					{
						Invocation invocation = new Invocation( classobject_h, sig, args );
						NewObject new_object = new NewObject( invocation );
						Reply reply = gateMgr.cross( new_object );
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



