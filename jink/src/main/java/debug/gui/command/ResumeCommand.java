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