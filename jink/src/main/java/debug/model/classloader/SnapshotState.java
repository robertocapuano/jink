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
package debug.model.classloader;

import com.sun.jdi.*;
import java.util.*;

import debug.model.*;

import debug.model.object.ObjectState;
import debug.model.classobject.ClassObjectModel;

import tools.*;

public class SnapshotState extends debug.model.object.SnapshotState implements ClassLoaderState, DebugOn
{
	protected final List definedClassModels;
	protected final List definedClassNames;
	protected final List visibleClassModels;
	protected final List visibleClassNames;
	
	protected SnapshotState( LiveState live ) throws OperationException, StateException
	{
		super( live );
		
		definedClassModels = live.getDefinedClassModels();
		definedClassNames = live.getDefinedClassNames();
		visibleClassModels = live.getVisibleClassModels();
		visibleClassNames = live.getVisibleClassNames();
	}

	public ObjectState transition()
	{
		return new HistoryState( this );
	}

	public List getDefinedClassNames()
	{
		return definedClassNames;
	}

	public List getVisibleClassNames()
	{
		return visibleClassNames;
	}
	
	public List getDefinedClassModels() 
	{
		return definedClassModels;
	}
	
	public List getVisibleClassModels()
	{
		return visibleClassModels;
	}
}
