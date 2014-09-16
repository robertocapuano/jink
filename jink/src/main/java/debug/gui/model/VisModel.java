/* 
 * Copyright (c) 2014, Roberto Capuano <roberto@2think.it>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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