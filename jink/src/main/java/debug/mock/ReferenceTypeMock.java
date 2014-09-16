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

public class ReferenceTypeMock implements ReferenceType
{
	List fields;
	List methods;
	String name;
	ClassObjectReferenceMock classObject;
	
	public ReferenceTypeMock( String _name, List _fields, List _methods )
	{
		name = _name;
		fields = _fields;
		methods = _methods;
		classObject = new ClassObjectReferenceMock( this, _fields );
	}
	
	public String name ()
	{
		return name;
	}

	public List allLineLocations()
	{
		System.out.println("allLineLocations");
		return null;
	}
	
	public List availableStrata()
	{
		System.out.println("availableStrata");
		return null;
	}
	
	public ClassLoaderReference classLoader()
	{
		return null;
	}

	public String sourceName()
	{ 	
		return null;
	}

	public List sourceNames( String stratum )
	{ 	
		return null;
	}
	
	
	
	public List sourcePaths(String  stratum) 
	{ 	
		return null;
	}
	
	public String sourceDebugExtension()
	{ 	
		return null;
	}

	public boolean isStatic()
	{ 	
		return false;
	}
	
	public boolean isAbstract()
	{ 	
		return false;
	}
	
	public boolean isPrepared() 
	{ 	
		return false;
	}

	public boolean isFinal() 
	{ 	
		return false;
	}
	
	public boolean isVerified()
	{ 	
		return false;
	}

	public boolean isInitialized()
	{
		return false;
	}
		
	public boolean failedToInitialize ()
	{
		return false;
	}
	
	public List fields()
	{
		return fields;
	}

	public List allFields()
	{
		return fields;
	}

	public List visibleFields ()
	{
		return fields;
	}
	
	public Field fieldByName( String fieldName ) 
	{
		return null;
	}	
	
	public List methods()
	{
		return methods;
	}	
	
	
	public List visibleMethods() 
	{
		return methods;
	}	
	
	public List allMethods ()
	{
		return methods;
	}	
	
	public List methodsByName (String  name) 
	{
		return new ArrayList();
	}	
	
	public List methodsByName (String  name, String  signature)
	{
		return new LinkedList();
	}	
	
	public List nestedTypes () 
	{
		return new ArrayList();
	}	
	
	public Value getValue (Field  field)
	{
		return null;
	}	
	
	public Map getValues (List  fields) 
	{
		return new HashMap();
	}	
	
	public ClassObjectReference classObject ()
	{
		return classObject;
	}	
	
	public List allLineLocations (String  stratum, String  sourceName)
	{
		return new ArrayList();
	}	
	
	public List locationsOfLine (int lineNumber)
	{
		return new ArrayList();
	}	
	
	public List locationsOfLine (String  stratum, String  sourceName, int lineNumber) 
	{
		return new ArrayList();
	}	
	
	public String defaultStratum ()
	{
		return null;
	}
	
	public boolean equals (Object  obj)
	{
		return super.equals( obj );
	}	
	
	public int hashCode () 
	{
		return super.hashCode();
	}	


	public int modifiers () 	
	{
		return 0;
	}
	
	
	public boolean isPrivate()
	{
		return false;
	}
	
	public boolean isPackagePrivate()
	{
		return false;
	}
	
	
	
	public boolean isProtected()
	{
		return false;
	}
	
	public boolean isPublic() 
	{
		return false;
	}
	
	public int compareTo (Object  o) 
	{
		return 0;
	}
	
	public VirtualMachine virtualMachine() 
	{
		return null;
	}


	public String toString () 	
	{
		return "<" + getClass() + ":" + fields + ">";
	}
	
	public String signature ()
	{
		return "";
	}

	public static void main( String[] args )
	{
		Field[] fields = new Field[] { new FieldMock("a"), new FieldMock( "b") };
		List fields_l = Arrays.asList( fields );
		List methods_l = new LinkedList();
		ReferenceType rt = new ReferenceTypeMock( "MockType", fields_l, methods_l );		
		
		System.out.println( "" + rt );
	}
	
}