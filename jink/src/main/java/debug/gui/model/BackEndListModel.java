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
