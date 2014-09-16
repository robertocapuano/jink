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

abstract class CircleRenderer extends AbstractRenderer implements DebugOn
{
	protected Rectangle getInnerRect()
	{
		Dimension size = getSize();
		
		double a = size.width/2;
		double b = size.height/2;
		double x0 = -a;
		double y0 = -b;
		
		double y = x0 / 3;
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