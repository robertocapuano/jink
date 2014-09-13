package debug.model.classloader;

import com.sun.jdi.*;
import java.util.List;
import java.io.Serializable;

import debug.model.*;

import debug.model.object.ObjectModel;
import debug.model.object.ObjectState;

import java.util.LinkedList;

public class ClassLoaderModel extends ObjectModel implements Serializable
{
	public ClassLoaderModel( ClassLoaderReference clr ) throws OperationException
	{
		super( clr.hashCode(),
				(ObjectModel) BackEndModel.getShared().referenceToModel( clr.referenceType().classObject() ),
				new LiveState( clr ),
				new LinkedList(),
				clr.referenceType().visibleMethods()
		);
	}

	public ClassLoaderModel( ClassLoaderModel that )
	{
		super( that );
	}
	
	public List getDefinedClassNames() throws StateException
	{
		return ((ClassLoaderState)state).getDefinedClassNames();
	}
	
	public List getVisibleClassNames() throws StateException
	{
		return ((ClassLoaderState)state).getVisibleClassNames();
	}
	
	public List getDefinedClassModels() throws OperationException, StateException
	{
		return ((ClassLoaderState)state).getDefinedClassModels();
	}
	
	public List getVisibleClassModels() throws OperationException, StateException
	{
		return ((ClassLoaderState)state).getVisibleClassModels();
	}
}



