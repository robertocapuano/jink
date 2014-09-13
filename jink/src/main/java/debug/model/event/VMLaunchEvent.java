package debug.model.event;

public class VMLaunchEvent extends ModelEvent
{
	public VMLaunchEvent(  )
	{
	}

	public void fire( ModelListener listener )
	{
		listener.vmLaunch( this );
	}
	
}
