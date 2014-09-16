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

import tools.*;

import org.jgraph.JGraph;
import org.jgraph.graph.*;

import debug.ti.frontmodel.*;

public class TiGraph extends JGraph implements DebugOff
{
	protected TiFrontModel frontModel;
	public TiGraph( TiFrontModel _frontModel )
	{
		super( _frontModel );

		frontModel = _frontModel;

		setSelectNewCells(false);
		setEditable(false);
		setCloneable( false );
		setDropEnabled( false );
		setConnectable( false );
		setDisconnectable( false );
	}
	
	public void updateUI()
	{
		setUI( new TiGraphUI( ) );
		invalidate();
	}
	
	protected VertexView createVertexView( Object v, CellMapper cm )
	{
		if (v instanceof ThreadCell)
		{
			DC.log( LEVEL, v );
			return new ThreadView( v, this, cm );
		}
		else
		if (v instanceof ObjectCell)
		{
			DC.log( LEVEL, v );
			return new ObjectView( v, this, cm );
		}
		else
		if (v instanceof CompoundCell)
		{
			DC.log( LEVEL, v );
			return new CompoundView( v, this, cm );
		}
		else
		{
			return super.createVertexView( v, cm );
		}
	}
	
	
}
