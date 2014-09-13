package debug.model.thread;

import com.sun.jdi.*;
import java.util.*;

import debug.model.*;
import debug.model.object.ObjectModel;
import debug.model.object.MethodModel;
import debug.model.classobject.ClassObjectModel;

import tools.*;

/**
 ** Nel Thread vi e il concetto di history, viene fatto lo snapshot (shallow copy) degli oggetti
 ** solo per N stati precedenti a quello attuale.
 **
 */
public class JumpModel extends DetailModel implements DebugOff
{
	// Instance fields
	protected final boolean exit;
	protected final ThreadModel thread;
	
	/**
	 ** Coppia oggetto metodo.
	 */
	protected int time;
	protected MethodModel invocation;
	
	public JumpModel( ThreadModel _thread, ObjectModel _object, Method _method, boolean _exit )
	{
		super( _object );
		thread = _thread;
		invocation = new MethodModel( _object, _method );
		exit = _exit;
		time = _object.getTime();
		BackEndModel.getShared().incGTime();
	}
	
	public void snapshot() throws StateException, OperationException
	{
		ObjectModel object_m = invocation.getObject().snapshot();
		invocation = new MethodModel( object_m, invocation.getMethod() );
	}
	
	public Value getWrappedValue() throws StateException, OperationException
	{
		return invocation.getObject().getWrappedValue();
	}
	
	public void transition() throws StateException, OperationException
	{
		invocation.getObject().transition();
	}

	public int getTime()
	{
		return time;
	}
	
	public boolean isEnterStep()
	{
		return !exit;
	}

	public boolean isExitStep()
	{
		return exit;
	}
	
	public ThreadModel getThread()
	{
		return thread;
	}
	
	public ObjectModel getObject()
	{
		return invocation.getObject();
	}
	
	public Method getMethod()
	{
		return invocation.getMethod();
	}
	
	public String shortDescription()
	{
		String desc = "time: " + getTime() + " ";
		desc += isExitStep()  ? "return " : "invoke ";
//		desc += invocation.getObject().shortDescription() + " ";
		desc += invocation.getMethod() + " ";
		return desc;
	}
	
	public String longDescription()
	{
		String desc = "time: " + getTime() + " ";
		desc += isExitStep()  ? "ret " : "inv ";
		desc += invocation.getObject().shortDescription() + " ";
		desc += invocation.getMethod() + " ";
		return desc;
	}
	
	public String toString()
	{
		return shortDescription();
/*		String res = Stringer.extractClassName( getClass().getName() );// + " " + hashCode();
		res += "time: " + getTime() + ",";
		res += isExitStep()  ? "ret," : "inv,";
		res += "object:" + invocation.getObject();
		res += "method:" + invocation.getMethod();
		
		return res;
*/
	}
}
