package debug.model.classloader;

import com.sun.jdi.*;
import java.util.*;

import debug.model.*;

import debug.model.object.ObjectState;
import debug.model.classobject.ClassObjectModel;

import tools.*;

public class HistoryState extends debug.model.object.HistoryState implements ClassLoaderState, DebugOn
{
	public HistoryState( SnapshotState snapshot )
	{
		super( snapshot );
	}

	public List getDefinedClassNames() throws StateException
	{
		throw new StateException();
	}

	public List getVisibleClassNames() throws StateException
	{
		throw new StateException();
	}
	
	public List getDefinedClassModels() throws StateException
	{
		throw new StateException();
	}
	
	public List getVisibleClassModels() throws StateException
	{
		throw new StateException();
	}
}