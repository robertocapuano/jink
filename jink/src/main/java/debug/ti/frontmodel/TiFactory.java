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

import java.awt.Point;
import java.awt.Rectangle;

import java.util.*;

import debug.model.thread.*;
import debug.model.object.*;
import debug.model.*;

import debug.gui.model.*;

import tools.*;

abstract class TiFactory extends TiModelBase implements DebugOff
{
	protected TiLayoutManager layoutMgr = new TiLayoutManager( this );
	
	private void initWithCommonValues( Map map )
	{
//		GraphConstants.setBorder(map, BorderFactory.createRaisedBevelBorder());
//		GraphConstants.setForeground(map, Color.white);
//		GraphConstants.setFont(map, GraphConstants.defaultFont.deriveFont(Font.BOLD, 12));
		GraphConstants.setOpaque(map, true);
	}

	//// Compound Start
	
	private CompoundCell newCompoundCell()
	{
		// cell
		CompoundCell compoundCell = new CompoundCell( (TiFrontModel) this, "compound" );
		
		Map attr = new HashMap();
		Map compound_a = GraphConstants.createMap();
		attr.put( compoundCell, compound_a );

		initWithCommonValues( compound_a );
		compound_a.putAll( layoutMgr.layout( compoundCell ) );
		
		insert( new Object[] { compoundCell }, attr, null, null, null );
		return compoundCell;
	}
	
/*	public CompoundCell newCompound( DetailCell[] childs )
	{
		CompoundCell compoundCell = newCompound();
			
		compoundCell.collapseCells( childs );	
		remove( childs );
		return compoundCell;
	}
*/
	public CompoundCell newCompoundCell( Object[] cells )
	{
		CompoundCell compoundCell = newCompoundCell();
		
		for ( int i=0; i<cells.length; i++ )
		{
			if ( cells[i] instanceof DetailCell )
			{
				compoundCell.collapseCell( (DetailCell) cells[i] );
			}
			else
			if ( cells[i] instanceof CompoundCell )
			{
				compoundCell.collapseCompound( (CompoundCell) cells[i] );
			}
			else
			{
				DC.log( LEVEL, cells[i] );
				continue;
			}

			remove( new Object[] { cells[i] } );
		}

		return compoundCell;
	}


	public DetailCell[] extractCompoundCell( CompoundCell compoundCell )
	{
		Map attributes = new HashMap();

		DetailCell[] details_c  = compoundCell.getInnerCells();
		
		for ( int i=0; i<details_c.length; ++i )
		{
			Map cell_a = GraphConstants.createMap( );
			attributes.put( details_c[i], cell_a );
			layoutMgr.layout( details_c[i] );
		}
		
		if (details_c.length>0)
		{
			insert( details_c, attributes, null, null, null );

			for ( int i=0; i<details_c.length; ++i )
			{
				DefaultPort[] details_p = compoundCell.extractCell( details_c[i] );
				for ( int j=0; j<details_p.length; ++j )
				{
					details_c[i].add( details_p[j] );
				}
			}
		}
				
		remove( new Object[] { compoundCell } );
		return details_c;
	}
	//// Compound End
	
	//// Object start

	public ObjectCell newObjectCell( ObjectModel object_m )
	{
		Map attributes = new HashMap();
		
		// cell
		ObjectCell cell = new ObjectCell( object_m );
		DefaultPort object_p = new DefaultPort( new Integer( object_m.getUID() ) );
		cell.add( object_p );

		Map cell_a = GraphConstants.createMap();
		attributes.put( cell, cell_a );
		
		initWithCommonValues( cell_a );
		cell_a.putAll( layoutMgr.layout( cell ) );
		insert( new Object[] { cell },  attributes, null, null, null );
		
		return cell;
	}
	
	//// Object end


	//// start JumpEdge


	protected ThreadCell newThreadCell( ThreadModel thread_m )
	{
		Map attributes = new HashMap();
		
		ThreadCell cell = new ThreadCell( thread_m );
		DefaultPort thread_p = new DefaultPort( new Integer( thread_m.getUID() ) );
		cell.add( thread_p );
		
		Map cell_a = GraphConstants.createMap();
		attributes.put( cell, cell_a );
		
		initWithCommonValues( cell_a );
		cell_a.putAll( layoutMgr.layout( cell ) );
		
		insert( new Object[] { cell }, attributes, null, null, null );
		
		return cell;
	}

	protected DefaultEdge newJumpEdge(	JumpModel jump_m, 
										DefaultGraphCell cell1, DefaultPort port1,
										DefaultGraphCell cell2, DefaultPort port2 )
	{
		// edge
		DefaultEdge jumpEdge = super.newEdge( jump_m, port1, port2 );
		Map attributes = new HashMap();
		Map edge_a = GraphConstants.createMap();
		attributes.put( jumpEdge, edge_a );
		
		GraphConstants.setLineStyle( edge_a, GraphConstants.STYLE_BEZIER );
		edge_a.putAll( layoutMgr.layout( cell1, cell2, jump_m.isEnterStep() ) );
		this.insert( new Object[] { jumpEdge },  attributes, null, null, null );
		return jumpEdge;
	}
	//// end JumpEdge
	
/*
	protected DefaultGraphCell newJumpEdge( JumpModel jump_m, Map attr )
	{
		DefaultGraphCell cell = newCell( jump_m, attr );
		DefaultPort port = new DefaultPort( jump_m.getThread() );
		cell.add( port );
		return cell;
	}

	protected DefaultEdge newRef( DetailModel detail_m, DefaultPort port1, DefaultPort port2 )
	{
		// edge
		Map attributes = new HashMap();
		DefaultEdge	edge = new DefaultEdge( detail_m );
		Map edge_a = GraphConstants.createMap();
		attributes.put( edge, edge_a );
		GraphConstants.setLineEnd( edge_a, GraphConstants.ARROW_TECHNICAL );
		GraphConstants.setDashPattern( edge_a, new float[] {2.5f} );
		GraphConstants.setEndFill( edge_a, true );
		
		ConnectionSet cs = new ConnectionSet( edge, port1, port2 );
		Object[] cells = new Object[] { edge };
		this.insert( cells,  attributes, cs, null, null );
		
		return edge;
	}


	protected DefaultGraphCell newCell( DetailModel detail_m )
	{
		return newCell( detail_m, layoutMgr.layout( detail_m )  );
	}
	
	protected DefaultGraphCell newCell( DetailModel detail_m, Map attr )
	{
		Map attributes = new HashMap();
		
		// cell
		DefaultGraphCell cell = new DefaultGraphCell( detail_m );

		attributes.put( cell, attr );
		
		Object[] cells = new Object[] { cell };
		insert( cells,  attributes, null, null, null );
		
//		modelToCell.put( detail_m, cell );
		return cell;
	}
	
	protected DefaultGraphCell newStep( JumpModel jump_m, Map attr )
	{
		DefaultGraphCell cell = newCell( jump_m, attr );
		DefaultPort port = new DefaultPort( jump_m.getThread() );
		cell.add( port );
		return cell;
	}
	
	protected DefaultGraphCell newThread( ThreadModel thread_m )
	{
		DefaultGraphCell cell = newCell( thread_m, layoutMgr.layout( thread_m )  );
		DefaultPort port = new DefaultPort( thread_m );
		cell.add( port );
		return cell;
	}		
	
	protected DefaultGraphCell newHandler( DetailModel detail_m )
	{
		DefaultGraphCell cell = newCell( detail_m, layoutMgr.layout( detail_m ) );
		DefaultPort port = new DefaultPort( detail_m );
		cell.add( port );
//		if (reference) initOuterRefs(port );
		return cell;
	}
	
	protected void initOuterRefs( DefaultPort port )
	{
		DetailModel detail_m = (DetailModel) port.getUserObject();
		
		if (detail_m instanceof ObjectModel)
		{
			try
			{
				ObjectModel object_m = (ObjectModel) detail_m;
				Map fields = object_m.getFields();
				Set keys = fields.keySet();
				
				for (Iterator i=keys.iterator(); i.hasNext(); )
				{
					Field field = (Field) i.next();
					DetailModel value_m = (DetailModel) fields.get( field );
					if ( value_m.hasHandler() )
					{
						DefaultGraphCell value_c = modelToCell( value_m );
						DefaultPort value_p = getModelPort( value_c, value_m );
						newRef( value_m ,port, value_p );
					}
				}
			}
			catch ( ModelException me )
			{	DC.log( me );	}
		}				
	}
	
	protected void disposeOuterRefs( DefaultPort port )
	{
		Set edges = port.getEdges();
		Object[] array = edges.toArray();
		remove( array );
	}

	
	protected DefaultGraphCell firstStep( JumpModel jump_m )
	{
		ThreadModel thread_m = jump_m.getThread();
		DefaultGraphCell jump_c = newStep( jump_m, layoutMgr.layout( jump_m ) );
		DefaultGraphCell thread_c = modelToCell( thread_m );
		DefaultPort jump_p = getModelPort(  jump_c, thread_m );
		DefaultPort thread_p = getModelPort(  thread_c, thread_m );
		
//		newEdge( jump_m, thread_p, jump_p );
		
		return jump_c;
	}
	
	
	
*/	

/*	public void update( DetailModel detail_m )
	{
		GraphCell cell = (DefaultGraphCell) modelToCell( detail_m );
		Map cellAttr = GraphConstants.createMap();
		GraphConstants.setValue( cellAttr, detail_m );
		cell.changeAttributes( cellAttr );
	}
*/	

	//// end inserters	
}