package debug.gui.abstractlistinspector;

import com.sun.jdi.*;
import javax.swing.*;
import java.awt.*;

import java.util.*;

import debug.model.object.FieldModel;
import debug.model.DetailModel;

import debug.gui.model.*;

public class FieldRenderer extends AbstractCellRenderer
{
	public FieldRenderer()
	{
		super();
	}
	
	protected String getLabel( Object value )
	{
		FieldModel mod = (FieldModel) value;
		String label = mod.toString();
		return label;
	}
}