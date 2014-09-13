package debug.gui.processbrowser;

import javax.swing.*;
import java.awt.*;

import debug.model.thread.ThreadModel;
import debug.gui.abstractlistinspector.AbstractCellRenderer;

class ProcessCellRenderer extends AbstractCellRenderer
{
	ProcessCellRenderer()
	{
		super();
	}
	
	protected String getLabel( Object value )
	{
		ThreadModel thread_m = (ThreadModel) value;
		
		String label = thread_m.getName();
		return label;
	}
}