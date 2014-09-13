package debug.mock;

import com.sun.jdi.*;

public class LocalVariableMock implements LocalVariable
{
	String name;
	Type type;
	
	public LocalVariableMock( String _name, Type _type )
	{
		name = _name;
		type = _type;
	}
	
	public boolean equals (Object  obj)
	{
		return false;
	}
	
	public int hashCode() 
	{
		return super.hashCode();
	}
		
	public String name() 
	{
		return name;
	}
	
	public String typeName () 
	{
		return "";
	}
	
	public Type type() throws ClassNotLoadedException 
	{
		return type;
	}
	
	
	public String signature ()
	{
		return "";
	}
	
	public boolean isVisible (StackFrame  frame) 
	{
		return true;
	}
	
	public boolean isArgument () 
	{
		return false;
	}
	
	public int compareTo (Object  o) 
	{
		return 0;
	}


	public VirtualMachine virtualMachine () 
	{
		return null;
	}
	
	
	public String toString ()
	{
		String res;

		res = "<" + getClass() + ": ";
		res += "name:" + name + ",";
		res += "type:" + type + ">";
		return res;
	}
	
	public static void main( String[] args )
	{
		Type intType = IntegerTypeMock.shared;
		
		LocalVariable lv = new LocalVariableMock("a",intType);
		
		System.out.println( "" + lv );	
	}
}
