package debug.gui.abstractlistinspector;

import javax.swing.*;

class ImmutableLSM extends DefaultListSelectionModel
{
	int immutable_index;
	ImmutableLSM( int index )
	{
		super();
		immutable_index = index;
	}
	
	public void addSelectionIndex( int index1, int index2 )
	{
	}
	
	
	public void removeSelectionInterval( int index1, int index2 )
	{
	}
	

	
}
