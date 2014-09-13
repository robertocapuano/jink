package debug.model.event;

import debug.model.thread.ThreadModel;

public class ThreadEndEvent extends ModelEvent
{
	protected final ThreadModel threadModel;
	
	public ThreadEndEvent( ThreadModel _threadModel )
	{
		threadModel = _threadModel;
	}
	
	public ThreadModel getThreadModel()
	{
		return threadModel;
	}
	
	public void fire( ModelListener listener )
	{
		listener.threadEnd( this );
	}
	public String toString()
	{
		return threadModel.toString();
	}	

	
}
