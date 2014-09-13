package debug.gui.stepbrowser;

import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.GridLayout;

import debug.model.thread.ThreadModel;
import debug.model.ModelManager;

import debug.gui.*;

import debug.gui.abstractlistinspector.*;

import debug.gui.model.FrontEndModel;
import debug.gui.model.FieldsFrontModel;
import debug.gui.model.MethodsFrontModel;
import debug.gui.event.*;

import tools.*;

public class StepBrowser extends JPanel implements Disposable
{
	public static final String VERSION = "0.1";
	
	protected StepView step_view;
	
	public StepBrowser( ModelManager modelMgr ) throws GUIException
	{
		super( new GridLayout( 2, 1, 5, 5 ) );
		setName( "Step" );
		setOpaque( true );
		
		step_view = new StepView( modelMgr );
		add( step_view );
	}
	
	public void dispose()
	{
	}
	
}
