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

import com.sun.jdi.*;

import java.util.*;

public class MethodMock implements Method
{
	Type returnType;
	List argTypes;
	String name;
	
	public MethodMock( String _name, Type _returnType, List _argTypes )
	{
		name = _name;
		returnType = _returnType;
		argTypes = _argTypes;
	}

	public final List allLineLocations()
	{
		return new LinkedList();
	}	

	public List allLineLocations(String stratumID,
							 String sourceName)
	{
		return new LinkedList();
	}
 	
 	
	public final List locationsOfLine(int lineNumber)
	{
		return new LinkedList();
	} 

    public List locationsOfLine(String stratumID,
                                String sourceName,
                                int lineNumber)
	{
		return new LinkedList();
	}


	public String returnTypeName()
	{
		return returnType.name();
	}   
 
 
	private String returnSignature()
	{
		return returnType.signature();
	}

    public Type returnType()
    {
    	return returnType;
    }
  	
    public Type findType(String signature)
    {
    	return null;
    }

    public List argumentTypeNames()
    {
    	return argTypes;
    }

    public List argumentSignatures()
    {
    	List sigs = new LinkedList();
    	
    	for ( Iterator i=argTypes.iterator(); i.hasNext(); )
    	{
    		sigs.add( ((Type)i.next()).signature() );
    	}

		return sigs;
	}
	
    Type argumentType(int index)
    {
    	return (Type) argTypes.get( index );
    }
    
    public List argumentTypes()
    {
    	return argTypes;
    }

    public int compareTo(Object object)
    {
    	return 0;
    }

    public boolean isAbstract()
    {
		return false;
	}

    public boolean isSynchronized() {
    	return false;
    }

    public boolean isNative() {
    	return false;
    }

    public boolean isConstructor() {
    	return false;
    }

    public boolean isStaticInitializer() {
    	return false;
    }
                                
    public boolean isObsolete() {
    	return false;
    }


    public String name() {
        return name;     
    }

    public String signature()
    {
        String sig =  returnType.signature() + '(';

        List args  = argumentSignatures();

        for (Iterator i=args.iterator(); i.hasNext(); )
        {
        	sig += (String) i.next();
        }
        
        sig += ')';
        
        return sig;
    }
    
    public int modifiers()
    {
        return 0;
    }
    
    public ReferenceType declaringType()
    {
        return null;
    }

    public boolean isStatic() {
    	return false;
    }

    public boolean isFinal() {
    	return false;
    }

    public boolean isPrivate() {
		return false;
    }

    public boolean isPackagePrivate() {
    	return false;
    }

    public boolean isProtected() {
   		return false;
    }

    public boolean isPublic() {
    	return true;
    }

    public boolean isSynthetic() {
    	return false;
    }

    long ref() {
        return hashCode();
    }

    boolean isModifierSet(int compareBits) {
    	return false;
    }

	public VirtualMachine virtualMachine()
	{
		return null;
	}

	public Location location () 
	{
		return null;
	}

    public Location locationOfCodeIndex(long codeIndex)
    {
    	return null;
    }
	
	public List variables () 
	{
		return new LinkedList();
	}
	
	public List variablesByName( String name )
	{
		return new LinkedList();
	}

	public List arguments () 
	{
		return new LinkedList();
	}
	
	public byte[] bytecodes ()
	{
		return new byte[0];
	}

	public String toString()
	{
		return  name +signature();
	}		
	
	public static void main( String[] args )
	{
		Method methodMock = new MethodMock( "f", IntegerTypeMock.shared, new LinkedList() );
		System.out.println( "" + methodMock );
	}	
}

