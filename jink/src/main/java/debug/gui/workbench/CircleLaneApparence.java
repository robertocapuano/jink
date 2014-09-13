package debug.gui.workbench;

import javax.swing.*;

import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Color;

import java.util.*;

import org.jgraph.graph.*;

import debug.model.*;

import debug.gui.model.GraphApparence;

class CircleLaneApparence implements GraphApparence
{
	protected final int step_x = 240;
	protected final int step_y = 30;

	Map common( Map attr )
	{
		GraphConstants.setForeground( attr, Color.BLUE );
		GraphConstants.setOpaque( attr, true);
		GraphConstants.setBorder( attr, BorderFactory.createRaisedBevelBorder());
	
		return attr;
	}

	public Map present( DetailModel detail_m )
	{
		Map attr = GraphConstants.createMap();
		common( attr );
		Dimension size = new Dimension( step_x, step_y );
//		GraphConstants.setSize( attr, size );
		GraphConstants.setBounds( attr, new Rectangle( size ) );
		
		return attr;

/*		if (detail_m instanceof ClassLoaderModel)
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
*/
	}
	
/*
	Map present( ObjectModel object_m )
	{
	}
	
	Map present( ThreadModel thread_m )
	{
	}

	Map present( ClassObjectModel classobject_m )
	{
	}
	
	Map present( ClassLoader classloader_m )
	{
	}

	
	Map present( JumpModel jump_m)
	{
	}
*/	


	
}
