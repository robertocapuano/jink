package debug.gui.workbench;

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
	
}