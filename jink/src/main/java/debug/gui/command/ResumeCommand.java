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
import java.awt.event.*;

import java.util.*;

import debug.runtime.*;

import debug.gui.*;
import debug.gui.event.*;

import tools.*;

public class ResumeCommand extends AbstractAction implements GUIEventsSource, DebugOff, Disposable
{
	RuntimeManager runtime;
	
	public ResumeCommand( RuntimeManager _runtime )
	{
		super( " Resume " );
		runtime = _runtime;
		runtime.addRuntimeListener( runtimeListener );
	}

	public void dispose()
	{
		runtime.removeRuntimeListener( runtimeListener );
		runtime = null;
	}
	
	protected RuntimeListener runtimeListener = new RuntimeListener()
	{
		public void change( RuntimeManager runtime )
		{
			ResumeCommand.this.setEnabled( runtime.isSuspended() );
		}
	};
	
	
	////
	protected LinkedList listeners = new LinkedList();

	public void addGUIListener( GUIListener listener )
	{
		listeners.add( listener );
	}
	
	public void removeGUIListener( GUIListener listener )
	{
		listeners.remove( listener );
	}
	
	public void fire( Object selection )
	{
		for ( Iterator i=listeners.iterator(); i.hasNext(); )
		{
			GUIListener listener = (GUIListener) i.next();
			listener.selected( selection );
		}
	}
	////
	
	public void actionPerformed( ActionEvent e)
	{
		try
		{ runtime.jump(); }
		catch( debug.runtime.RuntimeException re ) { DC.log( re ); }
	}
		
}