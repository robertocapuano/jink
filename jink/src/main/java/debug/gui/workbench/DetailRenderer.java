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

abstract class DetailRenderer extends BoxRenderer implements DebugOn
{
	private final static Font font = new Font( "SansSerif", Font.BOLD, 12 );
	private final static Color header_color = Color.yellow;

	protected int next_sector;
	
	public void paint( Graphics g )
	{
		super.paint( g );

		if ( super.isOpaque() )
		{
			Graphics2D g2 = (Graphics2D) g;
			
			// **todo: passare ad DetailCell etc
			CellView detail_v = (CellView) view;
			DefaultGraphCell detail_c = (DefaultGraphCell) detail_v.getCell();
			DetailModel detail_m = (DetailModel) detail_c.getUserObject();

			{
				g.setFont( font );				
				FontMetrics fontMetrics = g.getFontMetrics();
				g.setColor( header_color );
				
				Insets insets = getInsets();		
				int dx = insets.left;
				int dy = insets.top;
				
				dy = drawDesc( g2, font, detail_m.shortDescription(), dx, dy, width ); 
				
				next_sector = dy;
			}
		}
	}

}
