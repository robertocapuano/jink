package debug.model.classobject;

import debug.model.object.ObjectState;

import debug.model.OperationException;
import debug.model.StateException;

//import debug.model.classloader.ClassLoaderModel;

interface ClassState extends ObjectState
{
//	ClassLoaderModel getClassLoaderModel() throws StateException;
	public boolean isRunnable() throws OperationException, StateException;
	
}
