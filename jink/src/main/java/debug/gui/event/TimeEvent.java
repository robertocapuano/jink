package debug.gui.event;

public class TimeEvent
{
	public final int gtime;
	public final int ltime;
	public final boolean freeze;
	public final boolean continuum;
	
	public TimeEvent( 	int _gtime,
						int _ltime,
						boolean _freeze,
						boolean _continuum )
	{
		gtime = _gtime;
		ltime = _ltime;
		freeze = _freeze;
		continuum = _continuum;
	}
	
	public String toString()
	{
		return "gtime:" + gtime + " ltime:" + ltime + " freeze:" + freeze + " continuum:" + continuum;
	}
}

