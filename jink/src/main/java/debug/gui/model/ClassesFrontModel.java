package debug.gui.model;

import java.util.*;
import javax.swing.*;

import debug.model.event.*;
import debug.model.classloader.*;
import debug.model.classobject.*;
import debug.model.*;

import tools.*;
import debug.gui.*;
import debug.gui.event.*;

public class ClassesFrontModel extends AbstractListModel implements GUIListener, DebugOff, Disposable
{
	protected final ModelManager modelMgr;
	// core model
	protected final Vector classes = new Vector();
		
	protected ClassLoaderModel classLoaderModel;
	
	ClassesFrontModel( ModelManager _modelMgr )
	{
		modelMgr = _modelMgr;
		
		modelMgr.addModelListener( modelController );
		
		classLoaderModel = null;
	}
	
	public void dispose()
	{
		modelMgr.removeModelListener( modelController );
		classes.clear();
	}
	
	public void selected( Object selection )
	{	
		classLoaderModel = (ClassLoaderModel) selection;

		if ( classLoaderModel != null )
		{
			try
			{
				classes.clear();
				classes.addAll( classLoaderModel.getVisibleClassModels() );
//					classes.addAll( classLoaderModel.getVisibleClassNames() );
				DC.log( LEVEL, classes.size() + ":" + classes );
				fireContentsChanged( ClassesFrontModel.this, 0, classes.size() -1 );
			}
			catch( ModelException me ) { DC.log( me ); }
		}
		else
		{
			classes.clear();
			fireContentsChanged( ClassesFrontModel.this, 0, 0 );
		}
	}
	
	protected ModelListener modelController = new ModelListener()
	{
		public void threadNew( ThreadNewEvent threadNewEvent )
		{
		}
		
		public void threadJump( ThreadJumpEvent threadJumpEvent )
		{}
		
		public void threadStep( ThreadStepEvent threadStepEvent )
		{}
		
		public void threadEnd( ThreadEndEvent threadEndEvent )
		{}
		
		public void vmLaunch( VMLaunchEvent vmLaunchEvent )
		{
			classes.clear();
		}
		
		public void vmQuit( VMQuitEvent vmQuitEvent )
		{
			classes.clear();
			fireContentsChanged( ClassesFrontModel.this, 0, 0 );
		}
		
		public void newHandler( NewHandlerEvent newHandlerEvent )
		{
			DetailModel dm = newHandlerEvent.getDetailModel();
			
			if (dm instanceof ClassObjectModel)
			{
				ClassObjectModel co_m = (ClassObjectModel) dm;

				//
/*
				ClassLoaderModel cl_m = co_m.getClassLoaderModel();
				if (cl_m==classLoaderModel)
				{
					classes.add( co_m );
					fireIntervalAdded( ClassesFrontModel.this, classes.size() -1, classes.size() -1 );
				}
*/
				try
				{
					classes.clear();
					if (classLoaderModel==null)
					{
						DC.log(LEVEL, "(classLoaderModel==null)" );
						return;
					}
					else
					{
						classes.addAll( classLoaderModel.getVisibleClassModels() );
					}
					fireContentsChanged( ClassesFrontModel.this, 0, classes.size() -1 );
				}
				catch( ModelException me )
				{
					DC.log( me );
				}

			}
		
		
		}
	};
	
	public Object getElementAt( int index )
	{
		return classes.get( index );
	}
	
	public int getSize()
	{
		DC.log( false, "" + classes.size() );
		return classes.size();
	}
}
