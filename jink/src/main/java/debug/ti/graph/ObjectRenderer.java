package debug.ti.graph;

import com.sun.jdi.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.*;

import org.jgraph.graph.*;
import org.jgraph.JGraph;

import debug.model.*;
import debug.model.object.*;

import debug.ti.frontmodel.*;

import tools.*;

class ObjectRenderer extends DetailRenderer implements DebugOn
{
	private final static Font font = new Font( "SansSerif", Font.BOLD, 10 );
	private final static Color info_color = Color.green;
	private final static Color field_color = Color.white;

//	protected SwirlingThread swirlingThread = new Sw;
	
	public Color getBackground()
	{
		return Color.blue.darker();
	}
	
	ObjectRenderer()
	{
//		swirlingThread = new SwirlingThread( null, this );
	}
	
	public void paint( Graphics g )
	{
		super.paint(g);

		if ( super.isOpaque() )
		{
			Graphics2D g2 = (Graphics2D) g;
	
			ObjectView object_v = (ObjectView) view;
			ObjectCell object_c = (ObjectCell) object_v.getCell();
			ObjectModel object_m = object_c.getObjectModel();
		
			Map attr = object_c.getAttributes();
						
			Insets insets = getInsets();
			
			{
				int dx = insets.left;
				int dy = next_sector;
				
				// sezione info
				{
					g.setColor( info_color );
					dy = drawDesc( g2, font, object_m.isInstance() ? "instance" : "class", insets.left, dy, width );
					if ( ! object_c.isMemento() )
						dy = drawDesc( g2, font, "time:" + object_m.getTime(), insets.left, dy, width );
					else
						dy = drawDesc( g2, font, "undefined", insets.left, dy, width );
				}
									
				try
				{
					g.setColor( field_color );
					Map fields_values = object_m.getFields();
					Set fields = fields_values.keySet();
					
					for ( Iterator i=fields.iterator(); i.hasNext(); )
					{
						Field field = (Field) i.next();
						DetailModel value_m = (DetailModel) fields_values.get( field );
						dy = drawDesc( g2, font, FieldModel.shortDescription( field, value_m ), insets.left, dy, width ); 
					}
				}
				catch( ModelException me )
				{
					DC.log( me );
				}
				
				
/*				{
					dy = swirlingThread.paint( g2, object_m, dx, dy, width );
				}
*/				
				next_sector = dy;
			}
		}
	}
	
	JGraph getGraph()
	{
		return graph;
	}

}