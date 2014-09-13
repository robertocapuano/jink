package debug.ti.frontmodel;

import org.jgraph.graph.DefaultGraphCell;

abstract class CellWithTime extends DefaultGraphCell
{
	/** La cella è indefinita
	*/
	boolean memento = false;

	public CellWithTime( Object userObject )
	{
		super( userObject );
	}
	
	abstract void timeBump( int time );
	
	public boolean isMemento()
	{
		return memento;
	}
	
	protected void setMemento( boolean _memento )
	{
		memento = _memento;
	}
	
}
