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

import debug.model.object.ObjectModel;

/**
 ** Invocazione di Metodo su un oggetto
 */
public class MethodModel
{
	public static String[] getSignature( Method method )
	{
		List sig_l = method.argumentTypeNames();
		String[] sig_a = (String[]) sig_l.toArray( new String[0] );
		return sig_a;
	}
	
	public static String extractString( Method method )
	{
		String[] a = getSignature( method );
		StringBuffer res = new StringBuffer( method.name() );
		
		for (int i=0; i<a.length; ++i)
			res.append( a[i] );
			
		return res.toString();
	}
	
	//// Instance	
	ObjectModel object_m;
	Method method;
	
	public MethodModel( ObjectModel _object_m, Method _method )
	{
		object_m = _object_m;
		method = _method;
	}
	
	public ObjectModel getObject()
	{
		return object_m;
	}
	
	public Method getMethod()
	{
		return method;
	}
	
	public String shortDescription()
	{
		return shortDescription( method );
		
	}
	
	public String longDescription()
	{
		String desc = object_m.shortDescription();
		desc += longDescription( method );
		return desc;
	}
	
	public String toString()
	{
		return shortDescription();
	}

	public static String shortDescription( Method method )
	{
		return method.toString();
	}
	
	
	public static String longDescription( Method method )
	{
		return method.toString();
	}	
}