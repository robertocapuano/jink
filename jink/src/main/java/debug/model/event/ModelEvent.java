package debug.model.event;

public abstract class ModelEvent
{
	protected ModelEvent()
	{}
	
	public abstract void fire( ModelListener listener );
}
