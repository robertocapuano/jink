package debug.app;

import javax.swing.*;

import debug.model.*;
import debug.model.event.*;

import debug.gui.Disposable;

class GameOver implements Disposable
{
	ModelManager modelMgr;
	
	GameOver( ModelManager _modelMgr )
	{
		modelMgr = _modelMgr;
		modelMgr.addModelListener( modelController );
	}
	
	public void dispose()
	{
		modelMgr.removeModelListener( modelController );
		modelMgr = null;
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
		{}
		
		public void vmQuit( VMQuitEvent vmQuitEvent )
		{
			JOptionPane.showMessageDialog( null, "Debuggee application quitted", "GameOver",JOptionPane.INFORMATION_MESSAGE );
			System.exit(0);
		}
		
		public void newHandler( NewHandlerEvent newHandlerEvent )
		{}
	};
}
