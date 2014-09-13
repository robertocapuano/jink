package debug.gui.classbrowser;

import javax.swing.*;
import java.awt.*;
import java.io.*;

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
		String label = (String) value;
		return label;
	}
}