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

public class FieldsFrontModel extends AbstractListModel implements GUIListener, DebugOff, Disposable 
{
	protected final static Vector NO_FIELDS = new Vector(0);
	protected Vector fields_v = NO_FIELDS; 
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
			
			try
			{
				Map fields_m = object_m.getFields();
				Set fields_s = fields_m.entrySet();
				fields_v = new Vector( fields_s );
				fireContentsChanged( FieldsFrontModel.this, 0, fields_v.size()-1 );
			}
			catch(Exception oe )
			{
				DC.log( oe );
				fields_v = NO_FIELDS;
			}
		}
		else
		{
			object_m = null;
			fields_v = NO_FIELDS;
			fireIntervalRemoved( FieldsFrontModel.this, 0, 0 );
		}
	}

	public FieldsFrontModel(  )
	{
	}
		
	public void dispose()
	{
		fields_v = NO_FIELDS;
	}
		
	// Start AbstractListModel
	public Object getElementAt(int index )
	{
		Map.Entry entry = (Map.Entry) fields_v.get(index);
		Field field = (Field) entry.getKey();
		DetailModel value_m = (DetailModel) entry.getValue();
		
		return new FieldModel( object_m, field, value_m );
	}

	public int getSize( )
	{
		return fields_v.size();
	}
	
} 

