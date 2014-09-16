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
package debug.model.array;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.monitor.*;
import debug.model.primitive.*;
import debug.model.object.ObjectState;

class SnapshotState extends debug.model.object.SnapshotState implements ArrayState
{
	protected final DetailModel[] components;

	SnapshotState( LiveState live ) throws OperationException, StateException
	{
		super( live );
		
		components = live.getSnapComponents();
	}

/*	public int getLength()
	{
		return components.length;
	}
*/	
	public ObjectState transition() //throws StateException //, OperationException
	{
		return new HistoryState( this );
	}
	
	public DetailModel getComponent( int index ) //throws OperationException
	{
		return components[index];
	}
	
	public void setComponent( int index, DetailModel model ) throws StateException
	{
		throw new StateException();
	}
	
	// Doppia conversione di components: prima da lista a vettore poi di nuovo in lista
	public DetailModel[] getComponents()
	{
		return components;
	}
	
	public DetailModel[] getSnapComponents() 
	{
		return components;
	}

	public String toString()
	{
		String res;
		res = "<" + super.toString();
		res += " components:"+ components + ",";
		res += ">";
		
		return res;
	}

}