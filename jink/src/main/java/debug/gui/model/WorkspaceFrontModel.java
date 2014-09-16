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

import tools.*;
import debug.gui.Disposable;
import debug.model.DetailModel;

public class WorkspaceFrontModel extends AbstractListModel implements DebugOff, Disposable
{
	// core model
	protected final List workspace; // <DetailModel>

	protected final ModelManager modelMgr;
	protected final FieldsFrontModel fields_m;
	protected final MethodsFrontModel methods_m;
	
	WorkspaceFrontModel( ModelManager _modelMgr )
	{
		modelMgr = _modelMgr;
		modelMgr.addModelListener( modelController );
		
		BackEndModel backEndModel = modelMgr.getBackEndModel();
		workspace = backEndModel.getWorkspaceModels();
		
		fields_m = new FieldsFrontModel();
		methods_m = new MethodsFrontModel();
	}
	
	public void dispose()
	{
		modelMgr.removeModelListener( modelController );
		workspace.clear();
	}
	
	
	public FieldsFrontModel getFieldsFrontModel()
	{
		return fields_m;
	}
	
	public MethodsFrontModel getMethodsFrontModel()
	{
		return methods_m;
	}
	
	protected ModelListener modelController = new ModelListener()
	{
		public void threadNew( ThreadNewEvent threadNewEvent )
		{
			DC.log(LEVEL, threadNewEvent );
		}
		
		public void threadJump( ThreadJumpEvent threadJumpEvent )
		{}
		
		public void threadStep( ThreadStepEvent threadStepEvent )
		{}
		
		public void threadEnd( ThreadEndEvent threadEndEvent )
		{}
		
		public void vmLaunch( VMLaunchEvent vmLaunchEvent )
		{
			workspace.clear();
		}
		
		public void vmQuit( VMQuitEvent vmQuitEvent )
		{
			workspace.clear();
			fireContentsChanged( WorkspaceFrontModel.this, 0, 0 );
		}
		
		public void newHandler( NewHandlerEvent newHandlerEvent )
		{
			DetailModel dModel = newHandlerEvent.getDetailModel();
			if (workspace.contains( dModel) )
				return;
			workspace.add( dModel );
			int index = workspace.size()-1;
			fireIntervalAdded( WorkspaceFrontModel.this, index, index );
		}
	};
	
	public Object getElementAt( int index )
	{
		return workspace.get( index );
	}
	
	public int getSize()
	{
		return workspace.size();
	}
}
