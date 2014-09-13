package debug.model.event;

public class VMQuitEvent extends ModelEvent
{
	public VMQuitEvent(  )
	{
	}

	public void fire( ModelListener listener )
	{
		listener.vmQuit( this );
	}
	
}
