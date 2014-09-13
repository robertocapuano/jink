package debug.gui.event;

public interface TimeEventsSource
{
	void addTimeListener( TimeListener listener );
	void removeTimeListener( TimeListener listener );
}
