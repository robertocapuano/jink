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
package debug.model.object;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.monitor.*;
import debug.model.thread.*;

/**
 ** Dopo che per N passi un oggetto � nello stato Snapshot passa allo stato
 ** History in cui non ha pi� riferimenti a dati.
 **
 */
public class HistoryState implements ObjectState
{
	/**
	 ** MonitorModel
	 */
//	final MonitorModel monitorModel;
	
	protected  HistoryState( SnapshotState snapshotState )
	{
	}
	
	public int getTime()
	{
		return -1;
	}
	
	/**
	 ** Dopo History per adesso non c'� niente.
	 */ 
	public ObjectState transition()
	{
		return this;
	}

	public Value getWrappedValue() throws StateException
	{
		throw new StateException();
	}
	
	public DetailModel getField( Field field ) throws StateException
	{
		throw new StateException();
	}

	public void setField( Field field, DetailModel model ) throws StateException
	{
		throw new StateException();
	}

	public Map getFields() throws StateException
	{
		throw new StateException();
	}
	
	public MonitorModel getMonitorModel() throws StateException
	{
		throw new StateException();
	}
	
	public boolean isLocked() throws StateException
	{
		throw new StateException();
	}

	public List getRunners() throws StateException
	{
		throw new StateException();
	}

	public void enter( ThreadModel thread_m ) throws StateException
	{
		throw new StateException();
	}
	
	public void leave( ThreadModel thread_m ) throws StateException
	{
		throw new StateException();
	}

	//// Stato
	public boolean isLive()
	{
		return false;
	}
	
	public boolean isSnapshot()
	{
		return false;
	}
	
	public boolean isHistory()
	{
		return true;
	}
	
	public boolean isDead()
	{
		return false;
	}

	
	public String toString()
	{
		String res = "<" + getClass() + ": ";
		res += ">";
		
		return res;
	}

}
