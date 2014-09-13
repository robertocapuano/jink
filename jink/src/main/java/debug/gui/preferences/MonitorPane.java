package debug.gui.preferences;

import java.awt.FlowLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import java.util.*;

import debug.gui.event.*;
import debug.gui.model.VisModel;
import debug.gui.Disposable;

public class MonitorPane extends JPanel implements Disposable
{
	protected VisModel visModel;
		
	protected ActionListener selectionListener = new ActionListener()
	{
		public void actionPerformed( ActionEvent e)
		{
			String selection = (String) ((JComboBox)e.getSource()).getSelectedItem();
			
			visModel.setMonitor( selection.equals("Monitor On") );
		}
	};

	public MonitorPane( VisModel _visModel )
	{
		visModel = _visModel;
		setName( "MonitorPane" );
		setBorder( new TitledBorder( "MonitorPane" ) );
	
		String[] option = new String[] { "Monitor Off","Monitor On" };
		JComboBox combo = new JComboBox( option );
		combo.addActionListener( selectionListener );
		add( combo );
		combo.setEnabled( visModel.immutableMonitor() );
	}

	public void dispose()
	{
		visModel = null;
	}


}
