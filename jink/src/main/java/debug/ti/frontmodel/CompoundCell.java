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
package debug.ti.frontmodel;

import org.jgraph.graph.*;

import debug.model.*;

import java.util.*;

/**
 ** 
 */
 

public class CompoundCell extends CellWithTime
{
	protected TiFrontModel frontModel;
	
	protected Map cellsToPort = new HashMap(); // DetailCell --> DefaulPort[]
	
	public CompoundCell( TiFrontModel _frontModel, Object userObject )
	{
		super( userObject );
		frontModel = _frontModel;
	}

/*	public CompoundCell( CompoundCell compoundCell )
	{
		super( "compound" );
	}
*/
	
	public DetailCell[] getInnerCells()
	{
		Set innerCells = cellsToPort.keySet();
		List inner_l = new LinkedList();
		
		for ( Iterator i=innerCells.iterator(); i.hasNext(); )
		{
			DetailCell cell = (DetailCell) i.next();
			if ( ! cell.isMemento() )
			{
				inner_l.add( cell );
			}
		}
		
		return (DetailCell[]) inner_l.toArray( new DetailCell[0] );
	}
	
	public DetailCell[] getAllInnerCells()
	{
		Set innerCells = cellsToPort.keySet();
		return (DetailCell[]) innerCells.toArray( new DetailCell[0] );
	}
	
	public DetailModel[] getInnerModels()
	{
		DetailCell[] details_c = getAllInnerCells();
		DetailModel[] details_m = new DetailModel[ details_c.length ];
		
		for ( int j=0; j<details_c.length; ++j )
		{
			details_m[j] = (DetailModel) details_c[j].getUserObject();
		}
		
		return details_m;
	}
	
	protected void collapseCompound( CompoundCell that )
	{
		Collection c = that.cellsToPort.values();
		cellsToPort.putAll( that.cellsToPort );
		
		for ( Iterator i=c.iterator(); i.hasNext(); )
		{
			DefaultPort[] ports = (DefaultPort[]) i.next();
			for ( int j=0; j<ports.length; ++j )
			{
				add( ports[j] );
			}
		}

		that.cellsToPort.clear();
	}
			
	protected DetailModel collapseCell( DetailCell detailCell )
	{
		Vector ports = new Vector();
		
		int childs_i = detailCell.getChildCount( );

		for ( int i=0; i<childs_i; ++i )
		{
			Object child = detailCell.getChildAt( i );

			if (  child instanceof DefaultPort )
			{
				DefaultPort detailPort = (DefaultPort) child;
				ports.add( detailPort );
			}
		}

		for ( Iterator i=ports.iterator(); i.hasNext(); )
		{
			DefaultPort detailPort = (DefaultPort) i.next();
			add( detailPort );
		}

		cellsToPort.put( detailCell, ports.toArray( new DefaultPort[0] ) );
		return (DetailModel) detailCell.getUserObject();
	}
	
	protected DefaultPort[] extractCell( DetailCell detailCell )
	{
		DefaultPort[] ports = (DefaultPort[]) cellsToPort.get( detailCell );
		cellsToPort.remove( detailCell );
		return ports;
	}
	
	protected void removeInnerCells()
	{
		cellsToPort.clear();
	}
	
	//// Time
	public void timeBump( int ltime )
	{
		memento = true;
		DetailCell[] cells = getAllInnerCells();
		
		for ( int i=0; i<cells.length; ++i )
		{
			cells[i].timeBump( ltime );
			if ( ! cells[i].isMemento() )
				memento = false;
//			if ( cells[i].isMemento() )
//			{
//				cellsToPort.remove( cells[i] );
//			}
				
		}
	}
	
	

}

