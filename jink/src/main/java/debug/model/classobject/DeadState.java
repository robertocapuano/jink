package debug.model.classobject;

import com.sun.jdi.*;

import java.util.*;

import com.sun.jdi.*;
import debug.model.StateException;

/**
 ** Se non è più usato l'oggeto allora passa in questo stato.
 ** Onestamente non mi è chiaro l'uso di questo stato e le sue
 ** transizioni.
 */
 
abstract class DeadState extends debug.model.object.DeadState implements ClassState
{
	public boolean isRunnable() throws StateException
	{
		throw new StateException();
	}
}


