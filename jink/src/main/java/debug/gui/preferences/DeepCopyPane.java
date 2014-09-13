package debug.gui.preferences;

import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

import java.util.*;

import debug.gui.event.*;
import debug.gui.Disposable;
import debug.model.*;

import tools.*;

public class DeepCopyPane extends JPanel implements DebugOn, Disposable
{
	private final static String DEEP_S = "Deep Copy";
	private final static int DEEP_I = 0;
	private final static String SHALLOW_S = "Shallow Copy";
	private final static int SHALLOW_I = 1;
	
	protected JComboBox combo;

	protected ModelManager modelMgr;
	protected boolean deepCopy;
	
	protected ActionListener selectionListener = new ActionListener()
	{
		public void actionPerformed( ActionEvent e)
		{
			String selection = (String) ((JComboBox)e.getSource()).getSelectedItem();
			boolean _deepCopy = selection.equals(DEEP_S);
			DC.log(LEVEL, "_deep:" + _deepCopy + " deep:" + deepCopy );
			if (deepCopy!=_deepCopy)
			{
				deepCopy = _deepCopy;
				DC.log(LEVEL, "deep:" + deepCopy );
				modelMgr.setDeepCopy( deepCopy );
			}
		}
	};
	
	protected ModellerListener modellerListener = new ModellerListener()
	{
		public void change( ModelManager modelMgr )
		{
			boolean _deepCopy = modelMgr.isDeepCopy();
			DC.log(LEVEL, "_deep:" + _deepCopy + " deep:" + deepCopy );
			if (deepCopy!=_deepCopy)
			{
				deepCopy = _deepCopy;
				DC.log(LEVEL, "deep:" + deepCopy );
				combo.setSelectedIndex( deepCopy ? DEEP_I : SHALLOW_I );
			}
		}
	};
	
	public DeepCopyPane( ModelManager _modelMgr  )
	{
		modelMgr = _modelMgr;

		setName( "DeepCopyPane" );
		setBorder( new TitledBorder( "DeepCopyPane" ) );
		
		String[] option = new String[] { DEEP_S, SHALLOW_S };
		combo = new JComboBox( option );
		
		deepCopy = modelMgr.isDeepCopy();
		combo.setSelectedIndex(  deepCopy ? DEEP_I : SHALLOW_I );

		combo.addActionListener( selectionListener );
		modelMgr.addModellerListener( modellerListener );
		
		add( combo );		
		combo.setEnabled( true );
	}
	
	public void dispose()
	{
		modelMgr = null;
	}
}

