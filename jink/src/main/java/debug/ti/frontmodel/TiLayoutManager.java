package debug.ti.frontmodel;

import javax.swing.border.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Container;
import java.awt.Graphics;

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

import debug.gui.*;

import tools.*;

import debug.ti.frontmodel.TiFrontModel;

public class TiLayoutManager implements DebugOff, Disposable
{
	protected final int step_w = 120;
	protected final int step_h = 120;

	protected DefaultGraphModel graphModel;
	
	TiLayoutManager( DefaultGraphModel _graphModel )
	{
		graphModel = _graphModel;
		graphModel.addGraphModelListener( frontModelListener );
	}
	
	public void dispose()
	{
		graphModel = null;
	}
	
	public void paint( Graphics g )
	{
		g.setColor( Color.black );
		g.drawOval( r, r, r, r );
	}
	
	GraphModelListener frontModelListener = new GraphModelListener()
	{	
		public void graphChanged( GraphModelEvent e )
		{
			if ( e.getChange().getInserted()!=null || e.getChange().getRemoved()!=null )
			// e.getChange().getInserted().length>0 )
				relayout( );
		}
	};
	

	private int bound_n = 1;
	private int counter = 0;
	private final int MAX_W = 1200;
	private final int MAX_H = 700;
	private int r = 320;
//	private int radius_x = 320;
//	private int radius_y = 200;
	
	protected Map layout( DefaultGraphCell cell )
	{
		if ( counter == bound_n )
		{	relayout();	}
		
		++counter;
		return layout0( cell, counter );
	}
	
	private Map layout0( DefaultGraphCell cell, int pos )
	{
		double phi = 2*Math.PI / bound_n;
		double angle = phi*pos;

//		int x = radius_x + (int)(Math.cos(angle) * radius_x);
//		int y = radius_y + (int)(Math.sin(angle) * radius_y);
		int x = r + (int) (Math.cos(angle) * r );
		int y = r + (int) (Math.sin(angle) * r );

		Map new_attr = GraphConstants.createMap();
		Dimension size = layout1( cell );
		GraphConstants.setBounds( new_attr, new Rectangle( x, y, size.width, size.height ));
		return new_attr;
	}

	private Dimension layout1( DefaultGraphCell cell )
	{
		return new Dimension( step_w, step_h );
	}
	
	protected void relayout(  )
	{
		List cells = initCells();
		Dimension cells_dim = maxDimension( cells );

//		Vector edges = initEdges();
//		Dimension edges_dim = maxDimension( edges );
		
//		Dimension dim = new Dimension();
//		dim.width = Math.max( cells_dim.width, edges_dim.width );
//		dim.height = Math.max( cells_dim.height, edges_dim.height );
	
		Dimension max = cells_dim;
		DC.log( LEVEL, "max:" + max );
		
		int n = cells.size();
		counter = n;

		{
			double d = Math.log( n ) / Math.log(2);
			d = Math.floor( d );
			int i = (int) (d + 1);
			bound_n = (int) Math.pow( 2, i );
		}		
		
		int c = bound_n * Math.max( max.width, max.height );
		r = (int) (c / (2 * Math.PI));
		
		DC.log( LEVEL, "r_x:" + r );
		int j=0;
		
		Map attr_map = new HashMap();
		
		for ( Iterator i=cells.iterator(); i.hasNext(); ++j)
		{
			DefaultGraphCell cell = (DefaultGraphCell) i.next();
			Map attr = layout0( cell, j );
			attr_map.put( cell, attr );
		}
		graphModel.insert( null, attr_map, null, null, null );
	}

	protected Map layout( 	DefaultGraphCell cell1, 
							DefaultGraphCell cell2, 
							boolean downstream		 )
	{
		
		Map cell1_a = cell1.getAttributes();
		Rectangle cell1_r = GraphConstants.getBounds( cell1_a );
		Point port1_p = cell1_r.getLocation();
//		Point port1_p = new Point( 10, 10 );
		
		Map cell2_a = cell2.getAttributes();
		Rectangle cell2_r = GraphConstants.getBounds( cell2_a );
		Point port2_p = cell2_r.getLocation();
			
		int medio_x = (port1_p.x + port2_p.x) /2;
		int p3_x = (port1_p.x + medio_x ) /2;
		int p4_x = (port2_p.x + medio_x )/2;
		
		int medio_y = (port1_p.y + port2_p.y) /2;
		int p3_y = (port1_p.y + medio_y)/2;
		int p4_y = (port2_p.y + medio_y)/2;
		
//		int dist_h = Math.abs( port1_p - port2_p );
//		int one_third = dist_h/3;
		
		
		if (p3_x==p4_x)
		{
			int dist = Math.abs( p3_y - p4_y );
			if (downstream)
				p3_x = port1_p.x - dist/3;
			else
				p3_x = port1_p.x + dist/3;
			p4_x = p3_x;
		}
		
		if (p3_y==p4_y)
		{
			int dist = Math.abs( p3_x - p4_x );
			if (downstream)
				p3_y = port1_p.y - dist/3;
			else
				p3_y = port1_p.y + dist/3;
			p4_y = p3_y;
		}
		
		Point p3 = new Point( p3_x, p3_y );
		Point p4 = new Point( p4_x, p4_y );
		
		ArrayList points = new ArrayList();
		
		if (downstream)
		{
			points.add( port1_p );
			points.add( port2_p );
		}
		else
		{
			points.add( port2_p );
			points.add( port1_p );
		}
		
		points.add( p3 );
		points.add( p4 );

		DC.log( LEVEL, port1_p + " " + port2_p + " " + p3 + " " + p4 );
				
		// edge
		Map edge_a = GraphConstants.createMap();
		GraphConstants.setPoints( edge_a, points );
		return edge_a;
	}

	private Dimension maxDimension( List cells )
	{
		Dimension dim = new Dimension();
		
		for ( Iterator i=cells.iterator(); i.hasNext(); )
		{
			DefaultGraphCell cell = (DefaultGraphCell) i.next();
			
			Map attr = cell.getAttributes();
			Rectangle bounds = GraphConstants.getBounds( attr );

			if (bounds!=null)
			{
				dim.width = Math.max( dim.width, bounds.width );
				dim.height = Math.max( dim.height, bounds.height );
			}
		}
		
		return dim;
	}
	
    protected List initCells( )
    {
    	return initList( DefaultGraphCell.class, DefaultEdge.class  );
	}
   
   	protected List initEdges()
   	{
   		return initList( DefaultEdge.class, DefaultGraphCell.class );
   	}
   	 
	protected List initList( Class clazz, Class no_clazz )
	{
    	List list = new LinkedList();
	    	
    	Object[] roots = DefaultGraphModel.getRoots( graphModel );

		for ( int i=0; i<roots.length; ++i )
    	{
    		if (clazz.isInstance( roots[i] ) && !no_clazz.isInstance( roots[i] ) )
    			list.add( roots[i] );
    	}
    	  
    	return list;
    }


    
}


