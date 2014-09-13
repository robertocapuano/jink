package debug.gui.workbench;

import com.sun.jdi.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;

import org.jgraph.graph.*;

import debug.model.*;
import debug.model.thread.*;

import tools.*;

abstract class CircleRenderer extends AbstractRenderer implements DebugOn
{
	protected Rectangle getInnerRect()
	{
		Dimension size = getSize();
		
		double a = size.width/2;
		double b = size.height/2;
		double x0 = -a;
		double y0 = -b;
		
		double y = x0 / 10;
		double x =  a * Math.sqrt( 1 - Math.pow((y-y0)/b, 2) ) + x0;
		
		double Dx = a+x;
		double Dy = b+y;
		
		int _x = (int) -x;
		int _y = (int) -y;
		int _w = (int) (2*Dx);
		int _h = (int) (2*Dy);
		
		return new Rectangle( _x, _y, _w, _h );
	}
	

//	protected final static Color field_color = Color.white;

	public void paint( Graphics g )
	{
		if ( super.isOpaque() )
		{
			Graphics2D g2 = (Graphics2D) g;

			final int bw = borderWidth;
			Dimension size = getSize();
			
			g.setColor( getBackground() );
			g.fillOval( bw - 1, bw - 1, size.width - bw, size.height - bw );
			
			if (bordercolor != null)
			{
				g.setColor(bordercolor);
				g2.setStroke( new BasicStroke(bw) );
				g.drawOval(bw - 1, bw - 1, size.width - bw, size.height - bw);
			}
			
			if (selected)
			{
				g2.setStroke(GraphConstants.SELECTION_STROKE);
				g.setColor( graph.getHighlightColor() );
				g.drawOval(bw - 1, bw - 1, size.width - bw, size.height - bw);
			}
			
		}
		
		super.paintSelectionBorder( g); 
	}

}