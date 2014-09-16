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
import debug.model.object.*;

import debug.ti.frontmodel.*;

import tools.*;

class CompoundRenderer extends BoxRenderer implements DebugOn
{
	private final static Font font = new Font( "SansSerif", Font.BOLD, 12 );
	private final static Color info_color = Color.green;
	private final static Color inner_color = Color.white;

	public Color getBackground()
	{
		return Color.blue;
	}
	
	public void paint( Graphics g )
	{
		super.paint(g);

		if ( super.isOpaque() )
		{
			Graphics2D g2 = (Graphics2D) g;
			CompoundView compound_v = (CompoundView) view;
			CompoundCell compound_c = (CompoundCell) compound_v.getCell();

			Insets insets = getInsets();

			{							
				int dx = insets.left;
				int dy = insets.top;
				
				g.setColor( inner_color );
				DetailCell[] collapsed = compound_c.getInnerCells();
				DC.log( "" + collapsed );

				for ( int i=0; i<collapsed.length; ++i)
				{
					DetailCell detail_c = collapsed[i];
					DetailModel detail_m = (DetailModel) detail_c.getUserObject();
					dy = drawDesc( g2, font, detail_m.shortDescription(  ), insets.left, dy, width );
				}
				
			}
		}
	}

}