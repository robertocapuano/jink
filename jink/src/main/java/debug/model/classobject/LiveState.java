package debug.model.classobject;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.primitive.*;
import debug.model.monitor.*;

import debug.model.object.ObjectState;
import debug.model.classloader.ClassLoaderModel;

import tools.*;

/**
 ** ObjectReference relativo al "this" del thread
 ** Da questo dovrebbe essere possibile analizzare gli altri oggetti.
 */
public class LiveState extends debug.model.object.LiveState implements ClassState, DebugOn
{
	LiveState( ClassObjectReference _classReference  ) throws OperationException
	{
		super( _classReference  );
	}
	
	protected Field[] initFieldDescriptors( ReferenceType referenceType ) throws OperationException
	{
		try
		{
			// **todo: controllare che sia allFields() il metodo giusto qui
			List in_fields_l = referenceType.allFields();
			List out_fields_l = new LinkedList();
			
			for ( Iterator it=in_fields_l.iterator(); it.hasNext(); )
			{
				Field field = (Field) it.next();
				if ( field.isStatic() )
					out_fields_l.add( field );
			}
			
			Field[] fields_a = (Field[]) out_fields_l.toArray( new Field[0] );
			return fields_a;
		}
		catch (IllegalArgumentException iae )
		{
			throw new OperationException( iae );
		}
	}

	/**
	 ** go to Snapshot
	 */
	public ObjectState transition() throws StateException, OperationException
	{
		SnapshotState next_state = new SnapshotState( this );
		return next_state;
	}

	public boolean isRunnable() throws OperationException
	{
		try
		{
			ClassObjectReference classObjectRef = (ClassObjectReference) objectReference;
			ReferenceType refType = (ReferenceType) classObjectRef.reflectedType();
			if (refType instanceof ClassType)
			{
				ClassType classType = (ClassType) refType;
				
				List interfaces = classType.interfaces();
				
				for ( Iterator i=interfaces.iterator(); i.hasNext(); )
				{
					InterfaceType iType = (InterfaceType) i.next();
					if (iType.name().equals( "java.lang.Runnable" ) )
					{
						return true;
					}
				}
			}

			return false;
		}
		catch( Exception e )
		{
			throw new OperationException( e );
		}		
	}

}
