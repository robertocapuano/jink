package debug.gui.workbench;

import tools.*;

import org.jgraph.JGraph;
import org.jgraph.graph.*;

public class WBGraph extends JGraph implements DebugOff
{
	protected WorkbenchModel wbModel;
	
	public WBGraph( WorkbenchModel _wbModel )
	{
		super( _wbModel );

		_wbModel = wbModel;

		setSelectNewCells(false);
		setEditable(false);
		setCloneable( false );
		setDropEnabled( false );
		setConnectable( false );
		setDisconnectable( false );
	}
	
	public void updateUI()
	{
		setUI( new WBGraphUI( ) );
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
		if (v instanceof JumpCell)
		{
			DC.log( LEVEL, v );
			return new JumpView( v, this, cm );
		}
		else
		{
			return super.createVertexView( v, cm );
		}
	}
	
	
}
