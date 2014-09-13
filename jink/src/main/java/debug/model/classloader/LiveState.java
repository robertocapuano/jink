package debug.model.classloader;

import com.sun.jdi.*;
import java.util.*;

import debug.model.*;

import debug.model.object.ObjectState;
import debug.model.classobject.ClassObjectModel;

import tools.*;

public class LiveState extends debug.model.object.LiveState implements ClassLoaderState, DebugOn
{
	protected LiveState( ClassLoaderReference clr ) throws OperationException
	{
		super( clr );
	}

	public ObjectState transition() throws StateException, OperationException
	{
		return new SnapshotState( this );
	}
	
	public List getDefinedClassNames()
	{
		LinkedList classnames_l = new LinkedList();
		
		ClassLoaderReference clRef = (ClassLoaderReference) objectReference;
		
		List classes = clRef.definedClasses();
		
		for ( Iterator i=classes.iterator(); i.hasNext(); )
		{
			ReferenceType refType = (ReferenceType) i.next();
			String classname_s = refType.name();
			
			classnames_l.add( classname_s );
		}
		
		return classnames_l;
	}
	
	public List getVisibleClassNames()
	{
		LinkedList classnames_l = new LinkedList();
		ClassLoaderReference clRef = (ClassLoaderReference) objectReference;
		List classes = clRef.visibleClasses();
		
		for ( Iterator i=classes.iterator(); i.hasNext(); )
		{
			ReferenceType refType = (ReferenceType) i.next();
			String classname_s = refType.name();
			classnames_l.add( classname_s );
		}
		
		return classnames_l;
	}
	
	public List getDefinedClassModels() throws OperationException
	{
		BackEndModel backEndModel = BackEndModel.getShared();
		LinkedList classmodels_l = new LinkedList();
		
		ClassLoaderReference clRef = (ClassLoaderReference) objectReference;
		
		List classes = clRef.definedClasses();
		
		for ( Iterator i=classes.iterator(); i.hasNext(); )
		{
			ReferenceType refType = (ReferenceType) i.next();
			ClassObjectModel classModel = (ClassObjectModel) backEndModel.referenceToModel( refType.classObject() );
			classmodels_l.add( classModel );
		}
		
		return classmodels_l;
	}
	
	public List getVisibleClassModels() throws OperationException
	{
		try
		{
			BackEndModel backEndModel = BackEndModel.getShared();
			LinkedList classmodels_l = new LinkedList();
			
			ClassLoaderReference clRef = (ClassLoaderReference) objectReference;
			
			List classes = clRef.visibleClasses();
			
			for ( Iterator i=classes.iterator(); i.hasNext(); )
			{
				ReferenceType refType = (ReferenceType) i.next();
				ClassObjectModel classModel = (ClassObjectModel) backEndModel.referenceToModel( refType.classObject() );
				classmodels_l.add( classModel );
			}
			
			return classmodels_l;
		}
		catch( ClassNotPreparedException cnpe )
		{
			DC.log( cnpe );
			throw new OperationException( cnpe );
		}
	}
	
}
/*
class SnapshotState extends ClassLoaderState
{
	final int uid;
	
	Value getWrappedValue() throws StateException
	{
		throw new StateException();
	}
	
	int getUID()
	{
		return 0;
	}


}

class HistoryState extends ClassLoaderState
{
	public Value getWrappedValue() throws StateException
	{
		throw new StateException();
	}	

	int getUID()
	{
		return 0;
	}

}
*/