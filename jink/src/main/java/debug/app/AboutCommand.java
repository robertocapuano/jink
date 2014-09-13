package debug.app;

import javax.swing.*;
import java.io.File;
import java.awt.event.*;

import debug.gui.*;

import tools.*;

import java.util.*;

public class AboutCommand extends AbstractAction 
{

	public AboutCommand( )
	{
		super( " About " );
	}

	public void actionPerformed( ActionEvent e )
	{
		AboutBox.getShared().show();
	}
		
}