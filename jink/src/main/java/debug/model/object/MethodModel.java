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