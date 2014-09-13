package debug.gui.workbench;

import org.jgraph.graph.*;

import debug.model.thread.*;

import tools.*;
import debug.model.*;

public class ThreadCell extends ObjectCell implements DebugOn
{
	public ThreadCell( ThreadModel thread_m )
	{
		super( thread_m );
	}

	public ThreadModel getThreadModel()
	{
		return (ThreadModel) getUserObject();
	}
	
	public void setThreadModel( ThreadModel thread_m )
	{
		setUserObject( thread_m );
	}
	
}