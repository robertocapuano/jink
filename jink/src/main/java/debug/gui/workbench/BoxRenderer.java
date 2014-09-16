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
package debug.gui.workbench;

import java.awt.*;

import javax.swing.border.*;
import javax.swing.*;
import java.text.*;
import java.awt.font.*;

import java.util.Map;

import org.jgraph.graph.*;

import debug.model.*;

import tools.*;

import java.util.*;

abstract class BoxRenderer extends AbstractRenderer implements DebugOn
{
	protected int width, height;
	
	public void paint( Graphics g )
	{
		if ( super.isOpaque() )
		{
			Graphics2D g2 = (Graphics2D) g;
			DefaultGraphCell cell = (DefaultGraphCell) view.getCell();

			Map attr = cell.getAttributes();
						
			Color bg_color = getBackground();
			Rectangle bounds = GraphConstants.getBounds( attr );
			Border border = BorderFactory.createRaisedBevelBorder();			

			{
				g2.setColor( bg_color );
				setBorder( border );
				border.paintBorder( this, g2, 0,0, bounds.width, bounds.height );
				Insets insets = getInsets();
				width =  bounds.width - insets.right - insets.left;
				height =  bounds.height - insets.bottom - insets.top;
				
				g2.fillRect( insets.left, insets.top, width, height );
			}
			
		}
		
		super.paintSelectionBorder( g);
	}
}