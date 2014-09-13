package debug.gui.preferences;

import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

import java.util.*;

import debug.gui.event.*;
import debug.gui.model.VisModel;
import debug.gui.Disposable;

import tools.*;


public class ReferencePane extends JPanel implements DebugOff, Disposable
{
	private final static String REF_OFF_S = "Reference Off";
	private final static int REF_OFF_I = 0;
	private final static String REF_ON_S = "Reference On";
	private final static int REF_ON_I = 1;
	
	protected VisModel visModel;
	
	public ReferencePane( VisModel _visModel )
	{
		visModel = _visModel;
		setName( "ReferencePane" );
		setBorder( new TitledBorder( "ReferencePane" ) );
		
		String[] option = new String[] { REF_OFF_S, REF_ON_S };
		JComboBox combo = new JComboBox( option );
		combo.setSelectedIndex( visModel.getReference() ? REF_ON_I : REF_ON_I );
		combo.addActionListener( selectionListener );
		add( combo );		
		combo.setEnabled( true );
		
		visModel.addVisListener( visListener );
	}
	
	public void dispose()
	{
		visModel = null;
	}

	protected VisListener visListener = new VisListener()
	{
		public void monitor( boolean status )
		{
			DC.log( LEVEL, "monitor:" + status );
		}
		
			// reference
		public void reference( boolean status )
		{
			DC.log( LEVEL, "ref:"+ status );
		}
	};

	ActionListener selectionListener = new ActionListener()
	{
		public void actionPerformed( ActionEvent e)
		{
			String selection = (String) ((JComboBox)e.getSource()).getSelectedItem();
			
			visModel.setReference( selection.equals(REF_ON_S) );
		}
	};
	

}

