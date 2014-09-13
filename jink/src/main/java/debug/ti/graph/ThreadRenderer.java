package debug.ti.graph;

import com.sun.jdi.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;

import org.jgraph.graph.*;

import debug.model.*;
import debug.model.thread.*;

import debug.ti.frontmodel.*;

import tools.*;

class ThreadRenderer extends CircleRenderer implements DebugOn
{
	protected final static Font font = new Font( "SansSerif", Font.BOLD, 10 );
	protected final static Color header_color = Color.white;
	protected final static Color info_color = Color.blue;

	protected int next_sector;

	public Color getBackground()
	{
		return Color.green;
	}
	

//	protected final static Color field_color = Color.white;

	public void paint( Graphics g )
	{
		super.paint(g);
		
		if ( super.isOpaque() )
		{
			Graphics2D g2 = (Graphics2D) g;

			ThreadView thread_v = (ThreadView) view;
			ThreadCell thread_c = (ThreadCell) thread_v.getCell();
			ThreadModel thread_m = (ThreadModel) thread_c.getUserObject();

			Map attr = thread_c.getAttributes();
						
			Rectangle innerRec = getInnerRect();

			g.setColor( Color.black );
			g.drawRect( innerRec.x, innerRec.y, innerRec.width, innerRec.height );
			
			{
				g.setColor( header_color );			
				next_sector = drawDesc( g2, font, thread_m.shortDescription(), innerRec.x, innerRec.y, innerRec.width ); 
			}
		}
	}

}