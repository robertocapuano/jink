package debug.gui.classbrowser;

import java.util.*;
import javax.swing.*;

import debug.model.event.*;
import debug.model.classloader.*;
import debug.model.classobject.*;
import debug.model.*;

import tools.*;
import debug.gui.*;
import debug.gui.event.*;

class ClassesModel extends AbstractListModel implements GUIListener, DebugOn, Disposable
{
	protected final Vector classes = new Vector();
		
	protected ClassPathModel classPathModel;
	
	ClassesModel( )
	{
		classPathModel = null;
	}
	
	public void dispose()
	{
		classes.clear();
	}
	
	public void selected( Object selection )
	{
		classPathModel = (ClassPathModel) selection;

		if ( classPathModel != null )
		{
			classes.clear();
			classes.addAll( classPathModel.getClasses() );
//				DC.log( LEVEL, classes.size() + ":" + classes );
			fireContentsChanged( ClassesModel.this, 0, classes.size() -1 );
		}
		else
		{
			classes.clear();
			fireContentsChanged( ClassesModel.this, 0, 0 );
		}
	}
	
	;
	
	public Object getElementAt( int index )
	{
		return classes.get( index );
	}
	
	public int getSize()
	{
//		DC.log( false, "" + classes.size() );
		return classes.size();
	}
}
