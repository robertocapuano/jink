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

class ThreadView extends VertexView implements DebugOn
{
	public static ThreadRenderer renderer = new ThreadRenderer();
	
	ThreadView( Object user, TiGraph tiGraph, CellMapper cm )
	{
		super( user, tiGraph, cm );
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
