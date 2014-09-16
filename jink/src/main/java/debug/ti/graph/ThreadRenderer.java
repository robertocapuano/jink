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