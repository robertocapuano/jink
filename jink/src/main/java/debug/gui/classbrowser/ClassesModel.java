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
package debug.gui.classbrowser;

import java.util.*;
import javax.swing.*;

import debug.model.event.*;
import debug.model.classloader.*;
import debug.model.classobject.*;
import debug.model.*;

import tools.*;
import debug.gui.*;
import debug.gui.event.*;

class ClassesModel extends AbstractListModel implements GUIListener, DebugOn, Disposable
{
	protected final Vector classes = new Vector();
		
	protected ClassPathModel classPathModel;
	
	ClassesModel( )
	{
		classPathModel = null;
	}
	
	public void dispose()
	{
		classes.clear();
	}
	
	public void selected( Object selection )
	{
		classPathModel = (ClassPathModel) selection;

		if ( classPathModel != null )
		{
			classes.clear();
			classes.addAll( classPathModel.getClasses() );
//				DC.log( LEVEL, classes.size() + ":" + classes );
			fireContentsChanged( ClassesModel.this, 0, classes.size() -1 );
		}
		else
		{
			classes.clear();
			fireContentsChanged( ClassesModel.this, 0, 0 );
		}
	}
	
	;
	
	public Object getElementAt( int index )
	{
		return classes.get( index );
	}
	
	public int getSize()
	{
//		DC.log( false, "" + classes.size() );
		return classes.size();
	}
}
