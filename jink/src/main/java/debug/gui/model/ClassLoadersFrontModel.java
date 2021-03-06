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
