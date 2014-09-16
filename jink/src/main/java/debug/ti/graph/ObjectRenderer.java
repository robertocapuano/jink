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