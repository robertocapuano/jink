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
package debug.ti.browser;

import javax.swing.*;

import java.util.Map;
import java.util.List;

import java.io.Serializable;

import debug.ti.frontmodel.*;
import org.jgraph.graph.*;
import org.jgraph.event.*;

import java.util.*;

public class UIDToCellModel extends AbstractListModel
	implements GraphModelListener, FMListener
{
	Map map;
	
	List list;
	
    public UIDToCellModel(TiFrontModel frontModel)
    {
    	map = frontModel.uidToCell;
		list = new ArrayList();
		frontModel.addGraphModelListener( this );
  		frontModel.addFMListener( this );
        reload();
    }

	private void reload()
	{
		Set set = map.entrySet();
		list.clear();
		list.addAll( set );
		
		if ( list.size()==0)
			fireContentsChanged( this, 0, 0 );
		else		
			fireContentsChanged( this, 0, list.size()-1 );
	}
		
	public void graphChanged(GraphModelEvent e)
	{
		reload();
	}
	
	public void change()
	{
		reload();
	}
	

	// Start AbstractListModel
	public Object getElementAt(int index )
	{
		Map.Entry entry = (Map.Entry) list.get(index);

		if ( entry==null)
			return "";
		else			
			return entry.getKey().toString() + "->" + entry.getValue();
	}

	public int getSize( )
	{
		return list.size();
	}

}
