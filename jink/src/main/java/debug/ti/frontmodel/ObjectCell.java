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
package debug.ti.frontmodel;

import org.jgraph.graph.*;

import debug.model.object.*;

public class ObjectCell extends DetailCell
{
	
	public ObjectCell( ObjectModel _object_m )
	{
		super( _object_m );
	}
	
	public ObjectModel getObjectModel()
	{
		return (ObjectModel) getUserObject();
	}
	
	public void setObjectModel( ObjectModel object_m )
	{
		setUserObject( object_m );
	}
	
	//// Time
	public void timeBump( int ltime )
	{
		ObjectModel object_m = getObjectModel();
		ObjectModel time_m = object_m.getAliasAtTime( ltime );

		if (time_m==null)
		{
			setMemento( true );
			// **todo: si pu� evitare questo? con update refresh, etc..?
			setObjectModel( object_m );
		}
		else
		{
			setMemento( false );
			setObjectModel( time_m );
		}
	}
	
}