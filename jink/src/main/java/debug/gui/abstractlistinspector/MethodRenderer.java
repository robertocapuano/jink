package debug.gui.abstractlistinspector;

import com.sun.jdi.*;
import javax.swing.*;
import java.awt.*;

import debug.gui.model.*;
import debug.model.DetailModel;
import debug.model.object.MethodModel;

public class MethodRenderer extends AbstractCellRenderer
{
	public MethodRenderer()
	{
		super();
	}
	
	protected String getLabel( Object value )
	{
		MethodModel method_m = (MethodModel) value;
		return method_m.toString();
	}
}