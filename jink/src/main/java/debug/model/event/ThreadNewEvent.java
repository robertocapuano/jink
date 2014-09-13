package debug.model.event;

import debug.model.thread.ThreadModel;

public class ThreadNewEvent extends ModelEvent
{
	protected final ThreadModel threadModel;
	
	public ThreadNewEvent( ThreadModel _threadModel )
	{
		threadModel = _threadModel;
	}
	
	public ThreadModel getThreadModel()
	{
		return threadModel;
	}
	
	public void fire( ModelListener listener )
	{
		listener.threadNew( this );
	}

	public String toString()
	{
		return threadModel.toString();
	}	
}
