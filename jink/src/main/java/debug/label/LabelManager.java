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
package debug.label;


import java.util.*;

/**
 ** Da uid ad una etichetta
 ** Ma anche da Compound ad una etichetta
 */
public class LabelManager
{
	public final static String VERSION = "0.1";
	
	private static LabelManager shared = new LabelManager();
	public static LabelManager getShared() { return shared; }
	
	////	
	
	private LabelManager()
	{}
	
	
	protected List repository = new LinkedList();
	
	public void putLabel( Label label )
	{
		repository.remove( label );
		repository.add( label );
		fire( label );
	}
	
	public List getLabels( )
	{
		return new LinkedList( repository );
	}
	
	//// Listeners
	protected List listeners = new LinkedList();
	
	public void addLabelListener( LabelListener labelListener )
	{
		listeners.add( labelListener );
	}
	
	public void removeLabelListener( LabelListener labelListener )
	{
		listeners.remove( labelListener );
	}
	
	protected void fire( Label label )
	{
		for ( Iterator i=listeners.iterator(); i.hasNext(); )
		{
			LabelListener listener = (LabelListener) i.next();
			listener.newLabel( label );	
		}
	}
	
		
}