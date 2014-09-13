package debug.gui.workbench;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;

import javax.swing.*;
import javax.swing.border.*;

import org.jgraph.graph.*;

import debug.model.object.*;

import tools.*;

class JumpView extends VertexView implements DebugOn
{
	public static JumpRenderer renderer = new JumpRenderer();
	
	JumpView( Object user, WBGraph wbGraph, CellMapper cm )
	{
		super( user, wbGraph, cm );
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
