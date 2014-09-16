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
import java.util.List;
import java.io.Serializable;

import debug.model.*;

import debug.model.object.ObjectModel;
import debug.model.object.ObjectState;

import java.util.LinkedList;

public class ClassLoaderModel extends ObjectModel implements Serializable
{
	public ClassLoaderModel( ClassLoaderReference clr ) throws OperationException
	{
		super( clr.hashCode(),
				(ObjectModel) BackEndModel.getShared().referenceToModel( clr.referenceType().classObject() ),
				new LiveState( clr ),
				new LinkedList(),
				clr.referenceType().visibleMethods()
		);
	}

	public ClassLoaderModel( ClassLoaderModel that )
	{
		super( that );
	}
	
	public List getDefinedClassNames() throws StateException
	{
		return ((ClassLoaderState)state).getDefinedClassNames();
	}
	
	public List getVisibleClassNames() throws StateException
	{
		return ((ClassLoaderState)state).getVisibleClassNames();
	}
	
	public List getDefinedClassModels() throws OperationException, StateException
	{
		return ((ClassLoaderState)state).getDefinedClassModels();
	}
	
	public List getVisibleClassModels() throws OperationException, StateException
	{
		return ((ClassLoaderState)state).getVisibleClassModels();
	}
}



