package debug.model.event;

import com.sun.jdi.Location;

import debug.model.thread.ThreadModel;

public class ThreadStepEvent extends ModelEvent
{
	protected final ThreadModel threadModel;
	protected final Location location;
	
	public ThreadStepEvent( ThreadModel _threadModel, Location _loc )
	{
		threadModel = _threadModel;
		location = _loc;
	}
	
	public ThreadModel getThreadModel()
	{
		return threadModel;
	}
	
	public Location getLocation()
	{
		return location;
	}
	
	public void fire( ModelListener listener )
	{
		listener.threadStep( this );
	}

	
}
