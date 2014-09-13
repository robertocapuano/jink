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