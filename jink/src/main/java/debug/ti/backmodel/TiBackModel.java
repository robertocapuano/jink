/* 
 * Copyright (c) 2014, Roberto Capuano <roberto@2think.it>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
