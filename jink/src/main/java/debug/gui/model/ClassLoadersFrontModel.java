package debug.gui.model;

import java.util.*;
import javax.swing.*;

import debug.model.event.*;
import debug.model.classloader.*;
import debug.model.*;

import tools.*;
import debug.gui.Disposable;

public class ClassLoadersFrontModel extends AbstractListModel implements DebugOff, Disposable
{
	protected final ModelManager modelMgr;
	// core model
	protected final Vector classloaders;
		
	ClassLoadersFrontModel( ModelManager _modelMgr )
	{
		modelMgr = _modelMgr;
		modelMgr.addModelListener( modelController );
		
		BackEndModel backEndModel = modelMgr.getBackEndModel();
		List classloaders_l = backEndModel.getClassLoaders();
		classloaders = new Vector( classloaders_l );
		
	}
	
	public void dispose()
	{
		modelMgr.removeModelListener( modelController );
		classloaders.clear();
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
		{
		}
		
		public void vmLaunch( VMLaunchEvent vmLaunchEvent )
		{
			classloaders.clear();
		}
		
		public void vmQuit( VMQuitEvent vmQuitEvent )
		{
			classloaders.clear();
			fireContentsChanged( ClassLoadersFrontModel.this, 0, 0 );
		}
		
		public void newHandler( NewHandlerEvent newHandlerEvent )
		{	
			DetailModel dm = newHandlerEvent.getDetailModel();
			if (dm instanceof ClassLoaderModel)
			{
				ClassLoaderModel cl_m = (ClassLoaderModel) dm;
				classloaders.add( cl_m );

				fireIntervalAdded( ClassLoadersFrontModel.this, classloaders.size()-1, classloaders.size()-1 );
			}
		}
	};
	
	public Object getElementAt( int index )
	{
		return classloaders.get( index );
	}
	
	public int getSize()
	{
//		DC.log( LEVEL, "" + threads.size() );
		return classloaders.size();
	}
}
