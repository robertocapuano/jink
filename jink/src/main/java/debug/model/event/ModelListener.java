package debug.model.event;

import java.util.EventListener;

import debug.model.thread.ThreadModel;
import debug.model.object.ObjectModel;

public interface ModelListener extends EventListener
{
	void threadNew( ThreadNewEvent threadForkEvent );
	void threadJump( ThreadJumpEvent threadJumpEvent ); 
	void threadStep( ThreadStepEvent threadStepEvent );
	void threadEnd( ThreadEndEvent threadJoinEvent );

	void vmLaunch( VMLaunchEvent vmLaunchEvent );
	void vmQuit( VMQuitEvent vmQuitEvent );
	
	void newHandler( NewHandlerEvent newHandlerEvent ); // qualunque cosa significhi questi evento	
}
