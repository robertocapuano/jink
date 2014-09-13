package debug.ti.backmodel;

import java.util.*;

import debug.model.*;
import debug.gui.model.*;

import debug.model.*;
import debug.model.event.*;
import debug.model.thread.*;

import java.util.*;

public class TiBackModel
{
	ModelManager modelMgr;
	
	public TiBackModel( ModelManager _modelMgr )
	{
		modelMgr = _modelMgr;
		
//		
	}

	//// model controller
	protected ModelListener modelListener = new ModelListener()
	{
		public void threadNew( ThreadNewEvent threadNewEvent )
		{
/*			ThreadModel thread_m = threadNewEvent.getThreadModel();
			GraphCell cell = modelToCell( thread_m );
			if (cell==null)
				newThread( thread_m );
*/		}
		
		public void threadJump( ThreadJumpEvent threadJumpEvent )
		{
/*			DC.log( LEVEL, "" + ltime );

			try
			{
				ThreadModel thread_m = threadJumpEvent.getThreadModel();
				
//				ltime = Math.max( ltime, thread_m.timelineDepth() );
				
				int step_i = thread_m.timelineDepth() -1;
				JumpModel jump_m = thread_m.getJump( step_i );
				
				if (step_i==0)
				{
					firstStep(  jump_m );
				}
				else
				{
					int prev_i = step_i -1;
					JumpModel prev_m = thread_m.getJump( prev_i );
					DC.log(LEVEL, prev_m );
					DC.log( LEVEL, jump_m );
					doStep( jump_m, prev_m );
				}
			}
			catch( OperationException oe )
			{
				DC.log( oe );
			}
*/
		}
		
		public void threadStep( ThreadStepEvent threadStepEvent )
		{
		}
		
		public void threadEnd( ThreadEndEvent threadEndEvent )
		{
//			ThreadModel thread_m = threadEndEvent.getThreadModel();
		}
		
		public void vmLaunch( VMLaunchEvent vmLaunchEvent )
		{
//			modelToCell.clear();
		}
		
		public void vmQuit( VMQuitEvent vmQuitEvent )
		{
//			modelToCell.clear();
		}
		
		public void newHandler( NewHandlerEvent newHandlerEvent )
		{
/*			DetailModel handler_m = newHandlerEvent.getDetailModel();
			GraphCell cell = modelToCell( handler_m );
			if (cell==null)
				SwimmingLaneModel.this.newHandler( handler_m );
*/		}
	};
}
