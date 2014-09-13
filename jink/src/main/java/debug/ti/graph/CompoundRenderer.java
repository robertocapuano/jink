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