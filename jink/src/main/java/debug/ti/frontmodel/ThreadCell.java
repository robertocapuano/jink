package debug.ti.frontmodel;

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
	
	public void timeBump( int ltime )
	{
		ThreadModel thread_m = getThreadModel();
		
		if (thread_m.timelineDepth()>0)
		{
			try
			{	setMemento( ! ( thread_m.getJump( 0 ).getTime()<=ltime  ) ); }
			catch( OperationException oe )
			{	DC.log( LEVEL, oe ); }
		}
	}
}