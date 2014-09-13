package debug.gui.preferences;

import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

import java.util.*;

import debug.runtime.*;

import debug.gui.*;
import debug.gui.event.*;
import debug.gui.model.*;

import tools.*;

public class RunPane extends JPanel implements Disposable, DebugOn
{
	protected RuntimeManager runtime;
	protected final JComboBox combo;
	
	protected boolean reqPause;
	
	private final static String JUMP_STRING = "Jump mode";
	private final static int JUMP_INDEX = 0;
	private final static String RUN_STRING = "Run mode";
	private final static int RUN_INDEX = 1;
	
	protected ActionListener selectionListener = new ActionListener()
	{
		public void actionPerformed( ActionEvent e)
		{
			String selection = (String) ((JComboBox)e.getSource()).getSelectedItem();
			
			boolean _reqPause = selection.equals( JUMP_STRING );
			if (reqPause!=_reqPause)
			{
				reqPause = _reqPause;
				runtime.setReqPause( reqPause );
			}
		}
	};

	protected RuntimeListener runtimeListener = new RuntimeListener()
	{
		public void change( RuntimeManager runtime )
		{
			boolean _reqPause =  runtime.reqPause();
			if (_reqPause!=reqPause)
			{
				reqPause = _reqPause;
				combo.setSelectedIndex( reqPause ? JUMP_INDEX : RUN_INDEX );
			}
		}
	};
	
	public RunPane( RuntimeManager _runtime )
	{
		runtime = _runtime;
		
		setName( "RunPane" );
		setBorder( new TitledBorder( "RunPane" ) );
		
		String[] option = new String[] { JUMP_STRING, RUN_STRING };
		combo = new JComboBox( option );
		reqPause = runtime.reqPause();
		
		combo.setSelectedIndex(  reqPause ? JUMP_INDEX : RUN_INDEX );

		combo.addActionListener( selectionListener );
		runtime.addRuntimeListener( runtimeListener );
		
		add( combo );		
		combo.setEnabled( true );
	}
	
	public void dispose()
	{
		runtime.removeRuntimeListener( runtimeListener );
	}
}

