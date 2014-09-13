package debug.gui.classloaderbrowser;

import javax.swing.*;
import java.awt.*;

import debug.model.classloader.ClassLoaderModel;
import debug.gui.abstractlistinspector.AbstractCellRenderer;

class ClassLoadersCellRenderer extends AbstractCellRenderer
{
	ClassLoadersCellRenderer()
	{
		super();
	}
	
	protected String getLabel( Object value )
	{
		ClassLoaderModel classloader_m = (ClassLoaderModel) value;
		
		String label = classloader_m.getHandler() + " " + classloader_m.getUID();
		return label;
	}
}