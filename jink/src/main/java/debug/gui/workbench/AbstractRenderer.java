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

abstract class AbstractRenderer extends VertexRenderer implements DebugOn
{
	AbstractRenderer(  )
	{
		super( false );
	}
	
	/**
	 ** Il colore comunica informazioni sull'oggetto.
	 */
	abstract public Color getBackground();
	
	protected int drawDesc( Graphics2D g2, Font font, String desc_s, int dx, int dy, int width )
	{
		AttributedString desc_as = new AttributedString( desc_s ); //, attr );
		AttributedCharacterIterator desc_i = desc_as.getIterator();
		final int text_start = desc_i.getBeginIndex();
		final int text_end = desc_i.getEndIndex();
		desc_as.addAttribute( TextAttribute.FONT, font, text_start, text_end );

		LineBreakMeasurer lineMeasurer = new LineBreakMeasurer( desc_i, g2.getFontRenderContext() );
		lineMeasurer.setPosition( text_start );
		final float width_f = (float) width;
		float pos_y = dy;
		
		while (lineMeasurer.getPosition()<text_end)
		{
			TextLayout layout = lineMeasurer.nextLayout( width_f );
			pos_y += layout.getAscent();
			float pos_x = dx;
			layout.draw( g2, pos_x, pos_y );
			pos_y += layout.getDescent() + layout.getLeading();
		}
				
		return (int) pos_y;
	}

}