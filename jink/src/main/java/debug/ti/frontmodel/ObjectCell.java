package debug.ti.frontmodel;

import org.jgraph.graph.*;

import debug.model.object.*;

public class ObjectCell extends DetailCell
{
	
	public ObjectCell( ObjectModel _object_m )
	{
		super( _object_m );
	}
	
	public ObjectModel getObjectModel()
	{
		return (ObjectModel) getUserObject();
	}
	
	public void setObjectModel( ObjectModel object_m )
	{
		setUserObject( object_m );
	}
	
	//// Time
	public void timeBump( int ltime )
	{
		ObjectModel object_m = getObjectModel();
		ObjectModel time_m = object_m.getAliasAtTime( ltime );

		if (time_m==null)
		{
			setMemento( true );
			// **todo: si pu˜ evitare questo? con update refresh, etc..?
			setObjectModel( object_m );
		}
		else
		{
			setMemento( false );
			setObjectModel( time_m );
		}
	}
	
}