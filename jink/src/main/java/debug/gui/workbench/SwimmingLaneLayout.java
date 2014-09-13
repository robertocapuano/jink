package debug.gui.workbench;

import javax.swing.border.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Point;

import java.util.*;

import org.jgraph.graph.GraphConstants;

import debug.model.*;
import debug.model.object.*;
import debug.model.thread.*;
import debug.model.classobject.*;
import debug.model.classloader.*;

import tools.*;

/**
 ** Definiamo n colonne: nella prima i classloaders e le classi, dalla seconda in poi i threads
 */
public class SwimmingLaneLayout implements DebugOn
{
	class Coordinate
	{
		int col;
		int row;
		
		Coordinate( int _col, int _row )
		{
			col = _col;
			row = _row;
		}
	}
	
	protected int col_zero = 0;
	
	protected final int step_x = 320;
	protected final int step_y = 60;
	
	protected HashMap threadToCol = new HashMap();
	
	SwimmingLaneLayout()
	{
	}
	
	Map common( Map attr )
	{
		GraphConstants.setForeground( attr, Color.BLUE );
		GraphConstants.setOpaque( attr, true);
		GraphConstants.setBorder( attr, BorderFactory.createRaisedBevelBorder());
	
		return attr;
	}
		
	Map layout( ObjectModel object_m )
	{
		Map attr = GraphConstants.createMap();
		
		common( attr );
		
				
		Rectangle rec = new Rectangle( step_x/10, col_zero*step_y, step_x-step_x/10, step_y/2 );
		GraphConstants.setBounds( attr, rec );
		
		col_zero++;
		
		return attr;
	}
	
	Map layout( ThreadModel thread_m )
	{
		Map attr = GraphConstants.createMap();
		
		common( attr );
		
		Coordinate c = (Coordinate) threadToCol.get( thread_m );
		if ( c==null)
		{
			c = new Coordinate( threadToCol.size() +1, 0 );
			threadToCol.put( thread_m, c );
		}
		
		Rectangle rec = new Rectangle( c.col*step_x + step_x/10, c.row*step_y, step_x - step_x/10, 3 * step_y/4 );
		GraphConstants.setBounds( attr, rec );
		
		c.row++;
		threadToCol.put( thread_m, c );
		return attr;
	}
	
	Map layout( ClassObjectModel classobject_m )
	{
		Map attr = GraphConstants.createMap();
		
		common( attr );
				
		Rectangle rec = new Rectangle( step_x/10, col_zero*step_y, step_x-step_x/10, step_y/2 );
		GraphConstants.setBounds( attr, rec );
		
		col_zero++;
		return attr;
	}
	
	Map layout( ClassLoader classloader_m )
	{
		Map attr = GraphConstants.createMap();
		
		common( attr );
				
		Rectangle rec = new Rectangle( step_x/10, col_zero*step_y, step_x-step_x/10, step_y/2 );
		GraphConstants.setBounds( attr, rec );
		
		col_zero++;
		return attr;
	}

	
	Map layout( JumpModel jump_m)
	{
		ThreadModel thread_m = jump_m.getThread();
		Map attr = GraphConstants.createMap();
		
		common( attr );
		
		Coordinate c = (Coordinate) threadToCol.get( thread_m );
		if ( c==null)
		{
			c = new Coordinate( threadToCol.size() +1, 0 );
			threadToCol.put( thread_m, c );
		}
		
		c.row =  jump_m.getTime()+1;
		Rectangle rec = new Rectangle( c.col*step_x + step_x/10, c.row*step_y, step_x-step_x/10, step_y/2 );
		GraphConstants.setBounds( attr, rec );
		threadToCol.put( thread_m, c );
		return attr;
	}
	
	Map layout( DetailModel detail_m )
	{
		if (detail_m instanceof ClassLoaderModel)
			return layout( (ClassLoaderModel) detail_m );
		else
		if (detail_m instanceof ClassObjectModel)
			return layout( (ClassObjectModel) detail_m );
		else
		if (detail_m instanceof ThreadModel)
			return layout( (ThreadModel) detail_m );
		else
		if (detail_m instanceof JumpModel)
		{
			return layout( (JumpModel) detail_m );
		}
		else
		if (detail_m instanceof ObjectModel)
			return layout( (ObjectModel) detail_m );
		else
		{
			DC.log( LEVEL, "unknown type:" + detail_m.getClass() );
			return GraphConstants.createMap();
		}
	}
		
}