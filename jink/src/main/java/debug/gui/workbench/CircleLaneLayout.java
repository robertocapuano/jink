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
package debug.gui.workbench;

import javax.swing.border.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Container;

import java.util.*;

import org.jgraph.graph.GraphConstants;
import org.jgraph.event.*;
import org.jgraph.graph.*;
import org.jgraph.JGraph;

import debug.model.*;
import debug.model.object.*;
import debug.model.thread.*;
import debug.model.classobject.*;
import debug.model.classloader.*;

import tools.*;

/**
 ** Definiamo n colonne: nella prima i classloaders e le classi, dalla seconda in poi i threads
 */
class CircleLaneLayout implements GraphModelListener, DebugOn
{
	JGraph graph;
	
	CircleLaneLayout( JGraph _graph)
	{
		graph = _graph;
		graph.getModel().addGraphModelListener( this );
	}
	
	public void graphChanged( GraphModelEvent e )
	{
		if ( e.getChange().getInserted()!=null && e.getChange().getInserted().length>0 )
			layout( graph );
	}

	private int max_w, max_h;
	
	private Comparator comparator = new Comparator()
	{
		public int compare( Object o1, Object o2 )
		{
			if (o1 instanceof DefaultGraphCell && o2 instanceof DefaultGraphCell)
			{
				DefaultGraphCell cell1 = (DefaultGraphCell) o1;
				DefaultGraphCell cell2 = (DefaultGraphCell) o2;
				
				DetailModel d1 = (DetailModel) cell1.getUserObject();
				DetailModel d2 = (DetailModel) cell2.getUserObject();
				return d1.getUID() - d2.getUID();
			}
			else return -1;
		}
	};
	
	protected void layout( JGraph graph )
	{
		Set views = new TreeSet( comparator );

		fillVertex( views );
		int n = views.size();
		int radius_x = (int) (Math.min( n*(max_w) , 1200 )/Math.PI);
		int radius_y = (int) (Math.min( n*(max_h), 700 )/Math.PI);
	
		double phi = 2*Math.PI / n;
				
		int i=0;
		for ( Iterator j=views.iterator(); j.hasNext(); ++i )
		{
			CellView view = (CellView) j.next();
			double angle = phi*i;

			int x = radius_x + (int)(Math.cos(angle) * radius_x);
			int y = radius_y + (int)(Math.sin(angle) * radius_y);
			
			setLocation( view, x, y );
		}
	}
	
	protected static void setLocation( CellView cellView, int x, int y )
	{
		Rectangle r = cellView.getBounds();

		Map attr = GraphConstants.createMap( );
		r.x = x;
		r.y = y;
		GraphConstants.setBounds( attr, r );

		cellView.setAttributes( attr );
	}
	
    protected void fillVertex( Set views )
    {
    	CellMapper cm = graph.getGraphLayoutCache();
    	Object[] cells = graph.getRoots();
		
    	for ( int i=0; i<cells.length; ++i )
    	{
    		if (cells[i] instanceof DefaultGraphCell)
    		{
//    			DC.log( LEVEL, "" + cells[i] );
    			GraphCell cell = (GraphCell) cells[i];
//    			VertexView view = (VertexView) cells[i];
    			CellView view = cm.getMapping( cell, true );
    			if ( (view!=null) && (view instanceof VertexView) )
    			{
    				views.add( view );
    				Rectangle b = view.getBounds();
					if (b!=null)
					{
						max_w = Math.max( b.width, max_w );
						max_h = Math.max( b.height, max_h );
					}
				}
			}
    	}
    }		
}


