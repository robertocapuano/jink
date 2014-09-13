package debug.model.classloader;

import com.sun.jdi.*;
import java.util.*;

import debug.model.*;

import debug.model.object.ObjectState;
import debug.model.classobject.ClassObjectModel;

import tools.*;

public class SnapshotState extends debug.model.object.SnapshotState implements ClassLoaderState, DebugOn
{
	protected final List definedClassModels;
	protected final List definedClassNames;
	protected final List visibleClassModels;
	protected final List visibleClassNames;
	
	protected SnapshotState( LiveState live ) throws OperationException, StateException
	{
		super( live );
		
		definedClassModels = live.getDefinedClassModels();
		definedClassNames = live.getDefinedClassNames();
		visibleClassModels = live.getVisibleClassModels();
		visibleClassNames = live.getVisibleClassNames();
	}

	public ObjectState transition()
	{
		return new HistoryState( this );
	}

	public List getDefinedClassNames()
	{
		return definedClassNames;
	}

	public List getVisibleClassNames()
	{
		return visibleClassNames;
	}
	
	public List getDefinedClassModels() 
	{
		return definedClassModels;
	}
	
	public List getVisibleClassModels()
	{
		return visibleClassModels;
	}
}
