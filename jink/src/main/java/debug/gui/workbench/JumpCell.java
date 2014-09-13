package debug.gui.workbench;

import org.jgraph.graph.*;

import debug.model.thread.*;

public class JumpCell extends DetailCell
{
	
	public JumpCell( JumpModel jump_m )
	{
		super( jump_m );
	}
	
	public JumpModel getJumpModel()
	{
		return (JumpModel) getUserObject();
	}
	
	public void setJumpModel( JumpModel jump_m )
	{
		setUserObject( jump_m );
	}
	
	
}