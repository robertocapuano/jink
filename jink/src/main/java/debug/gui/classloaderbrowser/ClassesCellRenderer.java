package debug.gui.classloaderbrowser;

import javax.swing.*;
import java.awt.*;

import debug.model.classobject.ClassObjectModel;
import debug.gui.abstractlistinspector.AbstractCellRenderer;

class ClassesCellRenderer extends AbstractCellRenderer
{
	ClassesCellRenderer()
	{
		super();
	}
	
	protected String getLabel( Object value )
	{
		ClassObjectModel classobject_m = (ClassObjectModel) value;
		
		String label = classobject_m.getName();
		return label;
	}
}