package debug.ti.graph;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import javax.swing.*;
import javax.swing.border.*;

import org.jgraph.graph.*;

import debug.ti.frontmodel.*;

import debug.model.object.*;

import tools.*;

class CompoundView extends VertexView implements DebugOn
{
	public static CompoundRenderer renderer = new CompoundRenderer();
	
	CompoundView( Object user, TiGraph tiGraph, CellMapper cm )
	{
		super( user, tiGraph, cm );
	}
	
	public Point getPerimeterPoint( Point source, Point p )
	{
		return super.getPerimeterPoint( source, p );
	}
	
	public CellViewRenderer getRenderer()
	{
		return renderer;
	}
}
