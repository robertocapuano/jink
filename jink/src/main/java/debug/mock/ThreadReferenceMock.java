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
package debug.mock;

import java.util.*;

import com.sun.jdi.*;

import org.pf.joi.Inspector;

public class ThreadReferenceMock extends ObjectReferenceMock implements ThreadReference
{
	String name;
	private ObjectReference contended;
	List stackFrames = new LinkedList();
	/**
	 ** crea un thread con StackFrame vuoto
	 ** Odd Objects che esiste in JPDA
	 */
	public ThreadReferenceMock(String _name, ReferenceType _type )
	{
		super( _type, new LinkedList() );
		name = _name;
	}
	
	public void setContendedMonitor( ObjectReference _contended )
	{
		contended = _contended;
	}
	
	public String toString()
	{
		String res;
		
		res = "<" + getClass() + ",";
		res += "name: "+ name + ",";
		res += "frames: " + stackFrames;
		res += ">";
		
		return res;
	}
	
	public ObjectReference currentContendedMonitor()
	{
		return contended;
	}
	
	public StackFrame frame( int index )
	{
		return (StackFrame) stackFrames.get(index);
	}
	
	public int frameCount()
	{
		return stackFrames.size();
	}
	
	public List frames()
	{
		return stackFrames;
	}
	
	public List frames( int start, int length ) throws IncompatibleThreadStateException
	{
		return null;
	}
	
	public boolean isAtBreakpoint()
	{
		return false;
	}
	
	public boolean isSuspended()
	{
		return false;
	
	}
	
	public String name()
	{
		return name;
	}
	
	
	public List ownedMonitors()
	{
		return new LinkedList();
	}

	public void popFrames( StackFrame frame )
	{
		stackFrames.remove( frame );
	}	
	
	public void pushFrame( StackFrame frame )
	{
		stackFrames.add( frame );
	}	


	public void suspend()
	{
		return;
	}
	
	public int suspendCount()
	{
		return 0;
	}
	
	public void resume() 
	{
		return;
	}
	
	public void interrupt() 
	{
	}

	public int status()
	{
		return 0;
	}
	
	public void stop(ObjectReference  throwable) throws InvalidTypeException
	{
		return;
	}

	public ThreadGroupReference threadGroup()
	{
		return null;
	}


	// inhitered from ObjectReference
	

	public boolean equals( Object  obj)
	{
		return super.equals( obj );
	}
	
	public int hashCode ()
	{
		return super.hashCode();
	
	}

	public static void main( String[] args )
	{
		List methods_l = new LinkedList();
		ReferenceType rt = new ReferenceTypeMock( "ThreadMock", new LinkedList(), methods_l );		

		ThreadReferenceMock threadReference = new ThreadReferenceMock( "myThread", rt );
		
		Map locals = new HashMap();
		LocalVariable lv = new LocalVariableMock( "a", IntegerTypeMock.shared);
		locals.put( lv, IntegerTypeMock.shared );

//		StackFrame frame = new StackFrameMock( null, null, locals );
		StackFrame frame = new StackFrameMock( threadReference, null, locals );

		threadReference.pushFrame( frame );
		
      	Inspector.inspect( threadReference );
		System.out.println( "" + threadReference );
	}
	
}

