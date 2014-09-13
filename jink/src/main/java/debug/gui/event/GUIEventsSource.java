package debug.gui.event;

public interface GUIEventsSource
{
	void addGUIListener( GUIListener listener );
	void removeGUIListener( GUIListener listener );
}
