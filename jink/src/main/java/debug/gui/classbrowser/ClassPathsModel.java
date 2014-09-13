package debug.gui.classbrowser;

import java.util.*;
import javax.swing.*;

import debug.model.event.*;
import debug.model.classloader.*;
import debug.model.*;

import tools.*;
import debug.gui.*;
import debug.gui.event.*;

class ClassPathsModel extends AbstractListModel implements DebugOff, Disposable, GUIListener
{
	protected final Vector classpaths_v; // <ClassPathModel>
	
	ClassPathsModel( )
	{
		classpaths_v = new Vector();
	}
	
	public void dispose()
	{
		classpaths_v.clear();
	}
	
	
	public void selected( Object selection )
	{
		String classpath_s = (String) selection;
		ClassPathModel classpath_o = new ClassPathModel( classpath_s );
		classpaths_v.add( classpath_o );
		fireIntervalAdded( this, classpaths_v.size()-1, classpaths_v.size()-1 );
	}
	
	public Object getElementAt( int index )
	{
		return classpaths_v.get( index );
	}
	
	public int getSize()
	{
		return classpaths_v.size();
	}
}
