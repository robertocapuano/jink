package debug.ti.frontmodel;

import org.jgraph.graph.*;

import debug.model.*;

import java.util.*;

/**
 ** 
 */
 

public class CompoundCell extends CellWithTime
{
	protected TiFrontModel frontModel;
	
	protected Map cellsToPort = new HashMap(); // DetailCell --> DefaulPort[]
	
	public CompoundCell( TiFrontModel _frontModel, Object userObject )
	{
		super( userObject );
		frontModel = _frontModel;
	}

/*	public CompoundCell( CompoundCell compoundCell )
	{
		super( "compound" );
	}
*/
	
	public DetailCell[] getInnerCells()
	{
		Set innerCells = cellsToPort.keySet();
		List inner_l = new LinkedList();
		
		for ( Iterator i=innerCells.iterator(); i.hasNext(); )
		{
			DetailCell cell = (DetailCell) i.next();
			if ( ! cell.isMemento() )
			{
				inner_l.add( cell );
			}
		}
		
		return (DetailCell[]) inner_l.toArray( new DetailCell[0] );
	}
	
	public DetailCell[] getAllInnerCells()
	{
		Set innerCells = cellsToPort.keySet();
		return (DetailCell[]) innerCells.toArray( new DetailCell[0] );
	}
	
	public DetailModel[] getInnerModels()
	{
		DetailCell[] details_c = getAllInnerCells();
		DetailModel[] details_m = new DetailModel[ details_c.length ];
		
		for ( int j=0; j<details_c.length; ++j )
		{
			details_m[j] = (DetailModel) details_c[j].getUserObject();
		}
		
		return details_m;
	}
	
	protected void collapseCompound( CompoundCell that )
	{
		Collection c = that.cellsToPort.values();
		cellsToPort.putAll( that.cellsToPort );
		
		for ( Iterator i=c.iterator(); i.hasNext(); )
		{
			DefaultPort[] ports = (DefaultPort[]) i.next();
			for ( int j=0; j<ports.length; ++j )
			{
				add( ports[j] );
			}
		}

		that.cellsToPort.clear();
	}
			
	protected DetailModel collapseCell( DetailCell detailCell )
	{
		Vector ports = new Vector();
		
		int childs_i = detailCell.getChildCount( );

		for ( int i=0; i<childs_i; ++i )
		{
			Object child = detailCell.getChildAt( i );

			if (  child instanceof DefaultPort )
			{
				DefaultPort detailPort = (DefaultPort) child;
				ports.add( detailPort );
			}
		}

		for ( Iterator i=ports.iterator(); i.hasNext(); )
		{
			DefaultPort detailPort = (DefaultPort) i.next();
			add( detailPort );
		}

		cellsToPort.put( detailCell, ports.toArray( new DefaultPort[0] ) );
		return (DetailModel) detailCell.getUserObject();
	}
	
	protected DefaultPort[] extractCell( DetailCell detailCell )
	{
		DefaultPort[] ports = (DefaultPort[]) cellsToPort.get( detailCell );
		cellsToPort.remove( detailCell );
		return ports;
	}
	
	protected void removeInnerCells()
	{
		cellsToPort.clear();
	}
	
	//// Time
	public void timeBump( int ltime )
	{
		memento = true;
		DetailCell[] cells = getAllInnerCells();
		
		for ( int i=0; i<cells.length; ++i )
		{
			cells[i].timeBump( ltime );
			if ( ! cells[i].isMemento() )
				memento = false;
//			if ( cells[i].isMemento() )
//			{
//				cellsToPort.remove( cells[i] );
//			}
				
		}
	}
	
	

}

