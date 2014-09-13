package debug.model.classloader;

import java.util.List;

import debug.model.object.ObjectState;
import debug.model.*;

public interface ClassLoaderState extends ObjectState
{
	List getDefinedClassNames() throws StateException;
	List getVisibleClassNames() throws StateException;
	
	List getDefinedClassModels() throws OperationException, StateException;
	List getVisibleClassModels() throws OperationException, StateException;
	
}
