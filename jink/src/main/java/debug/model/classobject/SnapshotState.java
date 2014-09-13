package debug.model.classobject;

import com.sun.jdi.*;

import java.util.*;


import debug.model.*;
import debug.model.monitor.*;
import debug.model.classloader.*;


/**
 ** ObjectReference non più valido, quindi non interrogabile.
 ** Come suo surrogato abbiamo una shallow-copy.
 ** Rappresenta il this della precedente invocazione di dmetodo.
 */
public class SnapshotState extends debug.model.object.SnapshotState implements ClassState
{
	SnapshotState( LiveState liveState ) throws StateException, OperationException
	{
		super( liveState );
	}

	public boolean isRunnable() throws StateException
	{
		throw new StateException();
	}
}
