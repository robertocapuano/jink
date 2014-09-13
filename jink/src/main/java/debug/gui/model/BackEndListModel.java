package debug.gui.model;

import java.util.*;
import javax.swing.*;

import debug.model.ModelManager;
import debug.model.BackEndModel;
import debug.model.event.*;
import debug.model.thread.*;
import debug.model.*;

import tools.*;
import debug.gui.Disposable;

public class BackEndListModel extends AbstractListModel implements DebugOff, Disposable
{
	protected ModelManager modelMgr;
	protected BackEndModel backEndModel;
	
	BackEndListModel( ModelManager _modelMgr )
	{
		modelMgr = _modelMgr;
		modelMgr.addModelListener( modelController );
		
		backEndModel = modelMgr.getBackEndModel();
		list = backEndModel.getRefToModelList();
	}
	
	public void dispose()
	{
		modelMgr.removeModelListener( modelController );
		modelMgr = null;
		backEndModel = null;
		list = null;
	}
	
	// core model
	protected ArrayList list;
	
	protected ModelListener modelController = new ModelListener()
	{
		public void threadNew( ThreadNewEvent threadNewEvent )
		{
			update();
		}
		
		public void threadJump( ThreadJumpEvent threadJumpEvent )
		{
			update();
		}
		
		public void threadStep( ThreadStepEvent threadStepEvent )
		{
			update();
		}
		
		public void threadEnd( ThreadEndEvent threadEndEvent )
		{
			update();
		}
		
		public void vmLaunch( VMLaunchEvent vmLaunchEvent )
		{
			update();
		}
		
		public void vmQuit( VMQuitEvent vmQuitEvent )
		{
			update();
		}
		
		public void newHandler( NewHandlerEvent newHandlerEvent )
		{
			update();
		}
		
		private void update()
		{
			list = backEndModel.getRefToModelList();
			fireContentsChanged( BackEndListModel.this, 0, list.size() );
		}
	};
	
	public Object getElementAt( int index )
	{
		Map.Entry entry = (Map.Entry) list.get( index );
		Object key = entry.getKey();
		DetailModel detail_m = (DetailModel) entry.getValue();
		
		return key + "->" + detail_m.longDescription();
	}
	
	public int getSize()
	{
		return list.size();
	}
}
