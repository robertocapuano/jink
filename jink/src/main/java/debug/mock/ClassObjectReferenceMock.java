package debug.mock;

import java.util.*;

import com.sun.jdi.*;

public class ClassObjectReferenceMock extends ObjectReferenceMock implements ClassObjectReference
{
	public ClassObjectReferenceMock( ReferenceType type, List values_l )
	{
		super( type, values_l );
	}
	
	public ReferenceType reflectedType()
	{
		return referenceType;
	}
}
