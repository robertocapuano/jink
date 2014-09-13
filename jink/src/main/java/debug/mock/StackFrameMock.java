package debug.mock;

import java.util.*;

import com.sun.jdi.*;

public class StackFrameMock implements StackFrame // implements StackFrame
{
	ThreadReference threadReference;
	ObjectReference self;
	// LocalVariable to Value
	Map varsToValues;// = new HashMap();

	public StackFrameMock( ThreadReference _threadReference, ObjectReference _self, Map _varsToValues)
	{
		threadReference = _threadReference;
		varsToValues = _varsToValues;
		self = _self;
	}

	public Location location ()
	{
		return null;
	}
	
	public ThreadReference thread () 
	{
		return threadReference;
	}

	public ObjectReference thisObject () 
	{
		return self;
	}

	public List visibleVariables() throws AbsentInformationException 	
	{
		return null;
	}

	public LocalVariable visibleVariableByName(String  name) throws AbsentInformationException
	{
		return null;
	}
	
	
	public Value getValue (LocalVariable  variable) 
	{
		return null;
	}
	
	public Map getValues (List  variables) 
	{
		return null;
	}
	
	public void setValue (LocalVariable  variable, Value  value) throws InvalidTypeException ,ClassNotLoadedException
	{
	}
	
	public VirtualMachine virtualMachine ()
	{
		return null;
	}
	
	public String toString() 
	{
		String res;
		
		res = "<"+ getClass() + ":";
		if (threadReference != null)
			res += "\tthreadReference: " + threadReference.hashCode() + ", ";
		if (self!= null)
			res += "\tself: " + self.hashCode() + ", ";
			
		res += "\nvarsToValues: " + varsToValues;
		res += ">";
		
		return res;
	}	
	
	public static void main( String[] args )
	{
		Map locals = new HashMap();
		LocalVariable lv = new LocalVariableMock("a",IntegerTypeMock.shared);
		locals.put( lv, IntegerTypeMock.shared );

		StackFrame frame = new StackFrameMock( null, null, locals );

		System.out.println( "" + frame );
	}
	
	
	
}