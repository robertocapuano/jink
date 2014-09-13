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



