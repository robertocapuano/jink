package debug.ti.frontmodel;

import org.jgraph.graph.*;
import java.util.*;

import debug.model.*;
import debug.model.thread.*;

abstract class TiModelBase extends DefaultGraphModel
{	
	protected DefaultEdge newEdge( DetailModel detail_m, DefaultPort port1, DefaultPort port2 )
	{
		// edge
		Map attributes = new HashMap();
		DefaultEdge	edge = new DefaultEdge( detail_m );
		Map edge_a = GraphConstants.createMap();
		attributes.put( edge, edge_a );
		GraphConstants.setLineEnd( edge_a, GraphConstants.ARROW_CLASSIC );
		GraphConstants.setEndFill( edge_a, true );
		
		ConnectionSet cs = new ConnectionSet( edge, port1, port2 );
		Object[] cells = new Object[] { edge };
		this.insert( cells,  attributes, cs, null, null );
		
		return edge;
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

/*	DefaultPort initThreadPort( ThreadModel thread_m, DetailCell detail_c )
	{
		DefaultPort detail_p = getModelPort( detail_c, thread_m );
		if (detail_p==null)
		{
			detail_p = new DefaultPort( thread_m );
			detail_c.add( detail_p );
		}
		
		return detail_p;
	}
*/
	protected DefaultPort getModelPort( DefaultGraphCell jump_c, DetailModel detail_m )
	{
		int childs_i = this.getChildCount( jump_c );

		for ( int i=0; i<childs_i; ++i )
		{
			Object child = this.getChild( jump_c, i );

			if (  this.isPort( child ) )
			{
				DefaultPort port = (DefaultPort) child;
				Integer port_uid = (Integer) port.getUserObject();
				if (port_uid==null || port_uid.intValue()!=detail_m.getUID() )
					continue;
				else
					return port;
			}
		}
		
		return null;
	}

	protected DefaultEdge getModelEdge( DefaultPort port, DetailModel detail_m )
	{
		Set edges = port.getEdges();

		for ( Iterator i=edges.iterator(); i.hasNext(); )
		{
			DefaultEdge edge = (DefaultEdge) i.next();
			DetailModel edge_model = (DetailModel) edge.getUserObject();

			if (edge_model==null || edge_model!=detail_m)
				continue;
			else
				return edge;
		}
		
		return null;
	}	

	protected void removeCell( DefaultGraphCell cell )
	{
		int childs_i = this.getChildCount( cell );
		Object[] remove_a = new Object[childs_i+1];
		
		for ( int i=0; i<childs_i; ++i )
		{
			remove_a[i] = this.getChild( cell, i );
		}
		
		remove_a[childs_i] = cell;
		
		remove( remove_a );
	}
	
}
