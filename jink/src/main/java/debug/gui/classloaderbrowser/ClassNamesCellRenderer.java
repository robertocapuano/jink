package debug.gui.classloaderbrowser;

import javax.swing.*;
import java.awt.*;

import debug.model.classobject.ClassObjectModel;
import debug.gui.abstractlistinspector.AbstractCellRenderer;

import tools.*;

class ClassNamesCellRenderer extends AbstractCellRenderer implements DebugOff
{
	ClassNamesCellRenderer()
	{
		super();
	}
	
	protected String getLabel( Object value )
	{
		DC.log( LEVEL, value.getClass() );
		String classobject_s = (String) value;
		return classobject_s;
	}
}