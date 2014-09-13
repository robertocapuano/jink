package debug.gui.model;

import com.sun.jdi.*;
import java.util.*;
import javax.swing.AbstractListModel;

import debug.model.*;
import debug.model.object.*;
import debug.model.thread.JumpModel;

import debug.gui.*;
import debug.gui.event.*;

import tools.*;

public class MethodsFrontModel extends AbstractListModel implements GUIListener, DebugOn, Disposable
{
	protected final static Method[] NO_METHODS = new Method[0];
	protected Method[] methods = NO_METHODS;
	protected ObjectModel object_m = null;
	
	public void selected( Object selection )
	{
		
		if (selection != null)
		{
			if (selection instanceof JumpModel )
			{
				JumpModel jump_m = (JumpModel) selection;
				object_m = jump_m.getObject();
			}
			else
			if (selection instanceof ObjectModel)
			{
				object_m = (ObjectModel) selection;
			}
			else
			{
				object_m = null;
				DC.log( LEVEL, "" + selection );
				return;
			}

			methods = object_m.getMethodDescriptors();

			if (methods.length<=0)
				fireIntervalRemoved( MethodsFrontModel.this, 0, 0 );
			else
				fireContentsChanged( MethodsFrontModel.this, 0, methods.length-1 );
		}
		else
		{
			methods = NO_METHODS;
			fireIntervalRemoved( MethodsFrontModel.this, 0, 0 );
		}
		
	}

	public MethodsFrontModel(  )
	{
	}
		
	public void dispose()
	{
		methods = NO_METHODS;
	}
		
	// Start AbstractListModel
	public Object getElementAt(int index )
	{
		return new MethodModel( object_m, methods[index] );
	}

	public int getSize( )
	{
		return methods.length;
	}
	
} 

