package debug.gui.event;

public interface TimeListener
{
	// cambiato ltime
	void leap( TimeEvent time_e );
	// ltime=gtime
	void now( TimeEvent time_e );
	// cambiato gtime
	void jump( TimeEvent time_e );
	// cambiato freeze
	void freezed( TimeEvent time_e );
}
	
	
	