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
import debug.model.*;

import tools.*;
import debug.gui.*;
import debug.gui.event.*;

class ClassPathsModel extends AbstractListModel implements DebugOff, Disposable, GUIListener
{
	protected final Vector classpaths_v; // <ClassPathModel>
	
	ClassPathsModel( )
	{
		classpaths_v = new Vector();
	}
	
	public void dispose()
	{
		classpaths_v.clear();
	}
	
	
	public void selected( Object selection )
	{
		String classpath_s = (String) selection;
		ClassPathModel classpath_o = new ClassPathModel( classpath_s );
		classpaths_v.add( classpath_o );
		fireIntervalAdded( this, classpaths_v.size()-1, classpaths_v.size()-1 );
	}
	
	public Object getElementAt( int index )
	{
		return classpaths_v.get( index );
	}
	
	public int getSize()
	{
		return classpaths_v.size();
	}
}
