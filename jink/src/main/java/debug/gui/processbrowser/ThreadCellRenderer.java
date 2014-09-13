package debug.gui.processbrowser;

import com.sun.jdi.Method;

import javax.swing.*;
import java.awt.*;

import debug.model.thread.JumpModel;
import debug.model.object.ObjectModel;
import debug.model.object.ObjectState;
import debug.model.classobject.ClassObjectModel;

import debug.gui.abstractlistinspector.AbstractCellRenderer;

class ThreadCellRenderer extends AbstractCellRenderer
{
	protected String getLabel( Object value )
	{
		JumpModel jump_m = (JumpModel) value;
		
		ObjectModel object_m = jump_m.getObject();
		ClassObjectModel class_m = (ClassObjectModel) object_m.getClassModel();
		Method method = jump_m.getMethod();
		String object_s = object_m.getStateName();
		String op = jump_m.isExitStep()  ? "return" : "invoke";
		int time = jump_m.getTime();
		
		String label = time + " " + op + " " + class_m.getName() + " " + method + " " + object_s;
		return label;
	}
	
}