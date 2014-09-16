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

import com.sun.jdi.*;
import java.util.*;
import javax.swing.AbstractListModel;

import debug.model.*;
import debug.model.object.*;
import debug.model.thread.JumpModel;

import debug.gui.*;
import debug.gui.event.*;

import tools.*;

public class FieldsFrontModel extends AbstractListModel implements GUIListener, DebugOff, Disposable 
{
	protected final static Vector NO_FIELDS = new Vector(0);
	protected Vector fields_v = NO_FIELDS; 
	protected ObjectModel object_m = null;
	
	public void selected( Object selection )
	{	
		if (selection != null)
		{
			if (selection instanceof JumpModel )
			{
				JumpModel jump_m = (JumpModel) selection;
				object_m = jump_m.getObject();
			}
			else
			if (selection instanceof ObjectModel)
			{
				object_m = (ObjectModel) selection;
			}
			else
			{
				object_m = null;
				DC.log( LEVEL, "" + selection );
				return;
			}
			
			try
			{
				Map fields_m = object_m.getFields();
				Set fields_s = fields_m.entrySet();
				fields_v = new Vector( fields_s );
				fireContentsChanged( FieldsFrontModel.this, 0, fields_v.size()-1 );
			}
			catch(Exception oe )
			{
				DC.log( oe );
				fields_v = NO_FIELDS;
			}
		}
		else
		{
			object_m = null;
			fields_v = NO_FIELDS;
			fireIntervalRemoved( FieldsFrontModel.this, 0, 0 );
		}
	}

	public FieldsFrontModel(  )
	{
	}
		
	public void dispose()
	{
		fields_v = NO_FIELDS;
	}
		
	// Start AbstractListModel
	public Object getElementAt(int index )
	{
		Map.Entry entry = (Map.Entry) fields_v.get(index);
		Field field = (Field) entry.getKey();
		DetailModel value_m = (DetailModel) entry.getValue();
		
		return new FieldModel( object_m, field, value_m );
	}

	public int getSize( )
	{
		return fields_v.size();
	}
	
} 

