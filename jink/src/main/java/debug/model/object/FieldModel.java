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

import debug.model.object.ObjectModel;
import debug.model.DetailModel;

public class FieldModel
{
	protected final ObjectModel object_m;
	protected final Field field;
	protected final DetailModel value;
	
	public FieldModel( ObjectModel _object_m, Field _field, DetailModel _value )
	{
		object_m = _object_m;
		field = _field;
		value = _value;
	}

	public String toString()
	{
		return longDescription();
	}

	public ObjectModel getObject()
	{
		return object_m;
	}
	
	public Field getField()
	{
		return field;
	}
	
	public DetailModel getValue()
	{
		return value;
	}

	public static String fieldShortDescription( Field field )
	{
		return field.toString();
	}
	
	public static String fieldlongDescription( Field field )
	{
		return field.toString();
	}
	
	
	public static String shortDescription( Field field, DetailModel value )
	{
		String desc = fieldShortDescription( field );
		desc += " ";
		desc += value.shortDescription();
		return desc;
	}
	
	public static String longDescription( Field field, DetailModel value )
	{
		String desc = fieldlongDescription( field );
		desc += " ";
		desc += value.longDescription();
		return desc;	
	}
	
	public String shortDescription()
	{
		return shortDescription( field, value );
	}
	
	public String longDescription()
	{
		return longDescription( field, value );
	}
}