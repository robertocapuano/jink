package debug.app;

import javax.swing.*;
import java.io.File;
import java.awt.event.*;

import debug.gui.*;

import tools.*;

import java.util.*;

public class QuitCommand extends AbstractAction 
{

	public QuitCommand( )
	{
		super( " Quit " );
	}

	public void actionPerformed( ActionEvent e )
	{
		System.exit(0);
	}
		
}