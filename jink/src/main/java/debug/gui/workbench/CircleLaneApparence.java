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

import javax.swing.*;

import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Color;

import java.util.*;

import org.jgraph.graph.*;

import debug.model.*;

import debug.gui.model.GraphApparence;

class CircleLaneApparence implements GraphApparence
{
	protected final int step_x = 240;
	protected final int step_y = 30;

	Map common( Map attr )
	{
		GraphConstants.setForeground( attr, Color.BLUE );
		GraphConstants.setOpaque( attr, true);
		GraphConstants.setBorder( attr, BorderFactory.createRaisedBevelBorder());
	
		return attr;
	}

	public Map present( DetailModel detail_m )
	{
		Map attr = GraphConstants.createMap();
		common( attr );
		Dimension size = new Dimension( step_x, step_y );
//		GraphConstants.setSize( attr, size );
		GraphConstants.setBounds( attr, new Rectangle( size ) );
		
		return attr;

/*		if (detail_m instanceof ClassLoaderModel)
			return layout( (ClassLoaderModel) detail_m );
		else
		if (detail_m instanceof ClassObjectModel)
			return layout( (ClassObjectModel) detail_m );
		else
		if (detail_m instanceof ThreadModel)
			return layout( (ThreadModel) detail_m );
		else
		if (detail_m instanceof JumpModel)
		{
			return layout( (JumpModel) detail_m );
		}
		else
		if (detail_m instanceof ObjectModel)
			return layout( (ObjectModel) detail_m );
		else
		{
			DC.log( LEVEL, "unknown type:" + detail_m.getClass() );
			return GraphConstants.createMap();
		}
*/
	}
	
/*
	Map present( ObjectModel object_m )
	{
	}
	
	Map present( ThreadModel thread_m )
	{
	}

	Map present( ClassObjectModel classobject_m )
	{
	}
	
	Map present( ClassLoader classloader_m )
	{
	}

	
	Map present( JumpModel jump_m)
	{
	}
*/	


	
}
