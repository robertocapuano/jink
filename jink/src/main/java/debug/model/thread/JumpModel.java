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
package debug.model.thread;

import com.sun.jdi.*;
import java.util.*;

import debug.model.*;
import debug.model.object.ObjectModel;
import debug.model.object.MethodModel;
import debug.model.classobject.ClassObjectModel;

import tools.*;

/**
 ** Nel Thread vi e il concetto di history, viene fatto lo snapshot (shallow copy) degli oggetti
 ** solo per N stati precedenti a quello attuale.
 **
 */
public class JumpModel extends DetailModel implements DebugOff
{
	// Instance fields
	protected final boolean exit;
	protected final ThreadModel thread;
	
	/**
	 ** Coppia oggetto metodo.
	 */
	protected int time;
	protected MethodModel invocation;
	
	public JumpModel( ThreadModel _thread, ObjectModel _object, Method _method, boolean _exit )
	{
		super( _object );
		thread = _thread;
		invocation = new MethodModel( _object, _method );
		exit = _exit;
		time = _object.getTime();
		BackEndModel.getShared().incGTime();
	}
	
	public void snapshot() throws StateException, OperationException
	{
		ObjectModel object_m = invocation.getObject().snapshot();
		invocation = new MethodModel( object_m, invocation.getMethod() );
	}
	
	public Value getWrappedValue() throws StateException, OperationException
	{
		return invocation.getObject().getWrappedValue();
	}
	
	public void transition() throws StateException, OperationException
	{
		invocation.getObject().transition();
	}

	public int getTime()
	{
		return time;
	}
	
	public boolean isEnterStep()
	{
		return !exit;
	}

	public boolean isExitStep()
	{
		return exit;
	}
	
	public ThreadModel getThread()
	{
		return thread;
	}
	
	public ObjectModel getObject()
	{
		return invocation.getObject();
	}
	
	public Method getMethod()
	{
		return invocation.getMethod();
	}
	
	public String shortDescription()
	{
		String desc = "time: " + getTime() + " ";
		desc += isExitStep()  ? "return " : "invoke ";
//		desc += invocation.getObject().shortDescription() + " ";
		desc += invocation.getMethod() + " ";
		return desc;
	}
	
	public String longDescription()
	{
		String desc = "time: " + getTime() + " ";
		desc += isExitStep()  ? "ret " : "inv ";
		desc += invocation.getObject().shortDescription() + " ";
		desc += invocation.getMethod() + " ";
		return desc;
	}
	
	public String toString()
	{
		return shortDescription();
/*		String res = Stringer.extractClassName( getClass().getName() );// + " " + hashCode();
		res += "time: " + getTime() + ",";
		res += isExitStep()  ? "ret," : "inv,";
		res += "object:" + invocation.getObject();
		res += "method:" + invocation.getMethod();
		
		return res;
*/
	}
}
