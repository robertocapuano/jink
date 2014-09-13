package debug.gui.classbrowser;

import javax.swing.*;
import java.awt.*;

import debug.gui.abstractlistinspector.AbstractCellRenderer;

class ClassPathsCellRenderer extends AbstractCellRenderer
{
	ClassPathsCellRenderer()
	{
		super();
	}
	
	protected String getLabel( Object value )
	{
		ClassPathModel classpath_m = (ClassPathModel) value;
		
		String label = classpath_m.getClassPathString();
		return label;
	}
	
	
	
}