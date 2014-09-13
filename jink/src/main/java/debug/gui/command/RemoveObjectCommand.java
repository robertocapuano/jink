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

import tools.*;

public class RemoveObjectCommand extends AbstractAction implements DebugOff, GUIListener
{
	protected GateManager gateMgr;

	protected Handler object_h;
		
	public RemoveObjectCommand( GateManager _gateMgr )
	{
		super( " Remove Object " );
		gateMgr = _gateMgr;
		setEnabled( false );
	}
	
	//// inter-gui communication :)
	public void selected( Object selection )
	{
		if (selection==null)
		{
			setEnabled( false );
			object_h = null;
			return;
		}
		else	
		if (selection instanceof ObjectModel)
		{
			setEnabled( true );
			ObjectModel object_m = (ObjectModel) selection;
			object_h = object_m.getHandler();
			DC.log( LEVEL, object_h );
		}
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
						RemoveObject remove_object = new RemoveObject( object_h );
						Reply reply = gateMgr.cross( remove_object );
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



