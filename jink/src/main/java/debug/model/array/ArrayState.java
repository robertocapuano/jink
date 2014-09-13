package debug.model.array;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.monitor.*;
import debug.model.primitive.*;
import debug.model.object.ObjectState;

interface ArrayState extends ObjectState
{
	DetailModel getComponent( int index ) throws StateException, OperationException;
	void setComponent( int index, DetailModel component ) throws StateException, OperationException;
	DetailModel[] getComponents() throws StateException, OperationException;
	
//	int getLength() throws StateException;
}
