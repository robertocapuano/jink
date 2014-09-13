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

class ThreadView extends VertexView implements DebugOn
{
	public static ThreadRenderer renderer = new ThreadRenderer();
	
	ThreadView( Object user, WBGraph wbGraph, CellMapper cm )
	{
		super( user, wbGraph, cm );
	}
	
	public Point getPerimeterPoint( Point source, Point p )
	{
		Rectangle r = getBounds();
		
		double x = r.getX();
		double y = r.getY();
		double a = (r.getWidth() + 1) / 2;
		double b = (r.getHeight() + 1) / 2;
		
		// x0,y0 - center of ellipse
		double x0 = x + a;
		double y0 = y + b;

		// x1, y1 - point
		double x1 = p.x;
		double y1 = p.y;
		
		// calculate straight line equation through point and ellipse center
		// y = d * x + h
		double dx = x1 - x0;
		double dy = y1 - y0;

		if (dx == 0)
		    return new Point((int) x0, (int) (y0 + b * dy / Math.abs(dy)));

		double d = dy / dx;
		double h = y0 - d * x0;
					
		// calculate intersection
		double e = a * a * d * d + b * b;
		double f = - 2 * x0 * e;
		double g = a * a * d * d * x0 * x0 + b * b * x0 * x0 - a * a * b * b;
		
		double det = Math.sqrt(f * f - 4 * e * g);
		
		// two solutions (perimeter points)
		double xout1 = (-f + det) / (2 * e);
		double xout2 = (-f - det) / (2 * e);
		double yout1 = d * xout1 + h;
		double yout2 = d * xout2 + h;
		
		double dist1 = Math.sqrt(Math.pow((xout1 - x1), 2)
								 + Math.pow((yout1 - y1), 2));
		double dist2 = Math.sqrt(Math.pow((xout2 - x1), 2)
								 + Math.pow((yout2 - y1), 2));
		
		// correct solution
		double xout, yout;
		
		if (dist1 < dist2) {
			xout = xout1;
			yout = yout1;
		} else {
			xout = xout2;
			yout = yout2;
		}
		
		return new Point((int)xout, (int)yout);
	}
	
	public CellViewRenderer getRenderer()
	{
		return renderer;
	}
}
