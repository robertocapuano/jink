package debug.ti.graph;

import java.awt.*;

import javax.swing.border.*;
import javax.swing.*;
import java.text.*;
import java.awt.font.*;

import java.util.Map;

import org.jgraph.graph.*;

import debug.model.*;

import debug.ti.frontmodel.*;

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