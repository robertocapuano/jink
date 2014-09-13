package debug.gui.workspacebrowser;

import javax.swing.*;
import java.awt.*;

import debug.gui.abstractlistinspector.AbstractCellRenderer;
import debug.model.DetailModel;

class HandlerRenderer extends AbstractCellRenderer
{
	HandlerRenderer()
	{
		super();
	}
	
	protected String getLabel( Object value )
	{
		DetailModel detail_m = (DetailModel) value;
		String label = detail_m.getHandler() + " " + detail_m.getUID() + " " + detail_m.getClass();
		return label;
	}
}