package debug.gui.model;

import debug.model.*;
import debug.runtime.*;

import debug.gui.event.*;
import debug.gui.*;

import java.util.*;

public class VisModel implements Disposable
{
	final boolean canGetOwnedMonitorInfo, canGetMonitorInfo, canGetCurrentContendedMonitor;
	final boolean immutableMonitor;
	
	boolean monitor;
	boolean reference = true;
	
	VisModel( RuntimeManager runtime )
	{
		canGetOwnedMonitorInfo = runtime.canGetOwnedMonitorInfo();
		canGetMonitorInfo = runtime.canGetMonitorInfo();
		canGetCurrentContendedMonitor = runtime.canGetCurrentContendedMonitor();
		immutableMonitor = canGetOwnedMonitorInfo && canGetMonitorInfo && canGetCurrentContendedMonitor;
		monitor = immutableMonitor;
	}
	
	public void dispose()
	{
	}
	
	public boolean immutableMonitor()
	{
		return immutableMonitor;
	}
	
	public void setMonitor( boolean _value )
	{
		if (!immutableMonitor)
		{
			monitor = _value;
			fireMonitor();
		}
	}
	
	public boolean getMonitor()
	{
		return monitor;
	}
	
	public void setReference( boolean _value )
	{
		reference = _value;
		fireReference();
	}
	
	public boolean getReference()
	{
		return reference;
	}
	
	protected List listeners = new LinkedList();
	
	public void addVisListener( VisListener visListener )
	{
		listeners.add( visListener );
	}
	
	public void removeVisListener( VisListener visListener )
	{
		listeners.remove( visListener );
	}
	
	protected void fireMonitor()
	{
		for ( Iterator i=listeners.iterator(); i.hasNext(); )
		{
			VisListener vis = (VisListener) i.next();
			vis.monitor( monitor );
		}
	}
	
	protected void fireReference()
	{
		for ( Iterator i=listeners.iterator(); i.hasNext(); )
		{
			VisListener vis = (VisListener) i.next();
			vis.reference( reference );
		}
	}
}