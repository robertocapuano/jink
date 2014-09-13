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
