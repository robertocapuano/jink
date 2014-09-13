package debug.gui.command;

import javax.swing.*;
import java.io.File;
import java.awt.event.*;

import agents.*;

import debug.gate.GateManager;
import debug.exec.Reply;
import debug.bridge.BridgeException;
import debug.gui.*;

import tools.*;

import java.util.*;
import debug.gui.event.*;

public class NewClassLoaderCommand extends AbstractAction implements GUIEventsSource, DebugOff
{
	protected GateManager gateMgr;

	public NewClassLoaderCommand( GateManager _gateMgr )
	{
		super( " New ClassLoader " );
		gateMgr = _gateMgr;
	}

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
		String pwd = System.getProperty( "user.dir" );
		JFileChooser fileChooser = new JFileChooser( pwd );
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		int returnVal = fileChooser.showOpenDialog( null );
	 
	 	if ( returnVal == JFileChooser.APPROVE_OPTION )
	 	{
	 		File file = fileChooser.getSelectedFile();
		 	final String path = file.getPath();
		 	
//		JOptionPane.showInputDialog( null, "Path:"	
			Runnable r = new Runnable()
			{
				public void run()
				{
					try
					{
						NewClassLoader ncl1 = new NewClassLoader( path );
						Reply r1 = gateMgr.cross( ncl1 );
						
						DC.log( LEVEL, r1 );
						
						SwingUtilities.invokeLater( new Runnable() 
							{ public void run() { fire( path ); } } 
						);
					}
					catch( BridgeException be )
					{
						DC.log(be );
					}
				}
			};
			
			new Thread( r ).start();
		}
	}
		
}