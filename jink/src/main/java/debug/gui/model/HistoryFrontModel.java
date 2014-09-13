package debug.gui.model;

import java.util.*;
import javax.swing.*;

import debug.model.ModelManager;
import debug.model.BackEndModel;
import debug.model.event.*;
import debug.model.thread.*;

import tools.*;
import debug.gui.Disposable;

public class HistoryFrontModel extends AbstractListModel implements DebugOff, Disposable
{
	// core model
	protected final Vector threads;

	protected final ModelManager modelMgr;
	
	HistoryFrontModel( ModelManager _modelMgr )
	{
		modelMgr = _modelMgr;
		modelMgr.addModelListener( modelController );
		
		BackEndModel backEndModel = modelMgr.getBackEndModel();
		List dead_threads = backEndModel.getDeadThreads();
		threads = new Vector( dead_threads );
	}
	
	public void dispose()
	{
		modelMgr.removeModelListener( modelController );
		threads.clear();
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
			DC.log( LEVEL, ""+ threadEndEvent );
			
			ThreadModel thread_m = threadEndEvent.getThreadModel();
			threads.add( thread_m );
			fireIntervalAdded( HistoryFrontModel.this, threads.size()-1, threads.size()-1 );
		}
		
		public void vmLaunch( VMLaunchEvent vmLaunchEvent )
		{
			threads.clear();
		}
		
		public void vmQuit( VMQuitEvent vmQuitEvent )
		{
			threads.clear();
			fireContentsChanged( HistoryFrontModel.this, 0, 0 );
		}
		
		public void newHandler( NewHandlerEvent newHandlerEvent )
		{}
	};
	
	public Object getElementAt( int index )
	{
		return threads.get( index );
	}
	
	public int getSize()
	{
		return threads.size();
	}
}
