package debug.mock;

import java.util.*;

import com.sun.jdi.*;


public class IntegerTypeMock implements IntegerType
{
	public final static IntegerTypeMock shared = new IntegerTypeMock();

	public String signature() 
	{
		return "i";
	}
	
	public String name() 
	{
		return "int";
	}
	
	public VirtualMachine virtualMachine() 
	{
		return null;
	}
	
	public String toString() 
	{
		return getClass() + ": " + name();
	}
}

