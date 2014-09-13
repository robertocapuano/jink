package debug.model;

import com.sun.jdi.*;

import java.util.*;

import debug.model.event.*;
import debug.model.object.*;
import debug.model.thread.*;
import debug.model.string.*;
import debug.model.array.*;
import debug.model.classobject.*;
import debug.model.classloader.*;

import tools.*;
import debug.mock.*;


/*
 ** Il repository contiene tutti gli oggetti ed i threads usati nel programma debuggato.
 ** Alcuni di questi (sia oggetti che threads) avranno l'handler e sono stati istanziati dall'utente (oggetti workspace)
 ** Questi verranno visualizzati quando si sceglie di visualizzare solo gli oggetti workspace.
 ** Gli oggetti non workspace verranno visualizzati quando si sceglierà questo filtro.
 ** Infine i passati stati degli oggetti quando si sceglierà questa modalità.
 ** I Threads morti allo stesso modo.
 ** Questi filtri saranno implementati nell' Application Model (class BackEndModel)
 ** Questo, per la cronaca, è il Business Model
 **
 */
 
//// **TODO: correggere le race-condition: applicare qualche pattern per evitare troppe synchronized
public class BackEndModel implements DebugOn
{
	static protected final BackEndModel shared = new BackEndModel();

	public static BackEndModel getShared()
	{
		return shared;
	}

	private static int gtime = 0;
	
	public int getGTime()
	{
		return gtime;
	}
	
	public int incGTime()
	{
		return ++gtime;
	}
	
	// La mappa contiene soltanto oggetti live
	// i passati modelli sono presenti nel campo ObjectModel.past
	// e nelle timeline dei threads.
	protected Map referenceToModel = new HashMap();
	
	/**
	 ** dead threads repository
	 */
	protected List deadThreadRepository = new LinkedList();
	 

	public ArrayList getRefToModelList()
	{
		return new ArrayList( referenceToModel.entrySet() );
	}
	
	public synchronized DetailModel uidToModel( int uid )
	{
		return (DetailModel) referenceToModel.get( new Integer( uid ) );
	}
	
	/**
	 ** Restituisce il modello del riferimento
	 ** Per adesso solo ObjectModel, più avanti StringModel, ArrayModel, ClassModel, ClassLoaderModel.
	 */
	public synchronized DetailModel referenceToModel( ObjectReference reference ) throws OperationException
	{
		if ( reference == null)
		{
			DC.log( LEVEL, "objectReference==null"  );
			throw new OperationException( "objectReference==null" );
		}

		DetailModel model =  uidToModel( reference.hashCode() );

		// da qui factory method
		if ( model == null )
		{
			// l'ordine qui è significativo
			
			if ( reference instanceof StringReference )
			{
				StringReference sr = (StringReference) reference;
				model = new StringModel( sr );
			}
			else
			if ( reference instanceof ArrayReference )
			{
				ArrayReference ar = (ArrayReference) reference;
				model = new ArrayModel( ar );
			}
			if ( reference instanceof ThreadReference )
			{
				ThreadReference tr = (ThreadReference) reference;
				model = new ThreadModel( tr );
				DC.log( LEVEL, model );
			}
			else
			if ( reference instanceof ClassObjectReference )
			{
				try
				{
					ClassObjectReference cor = (ClassObjectReference) reference;
					model = new ClassObjectModel( cor );
				}
				catch( ClassNotPreparedException cnpe )
				{
					throw new OperationException( ((ClassObjectReference) reference).reflectedType().name(), cnpe );
				}
			}
			else
			if ( reference instanceof ClassLoaderReference )
			{
				ClassLoaderReference clr = (ClassLoaderReference) reference;
				model = new ClassLoaderModel( clr );
			}
			else
			{
				model = new ObjectModel( reference );
			}
			
			referenceToModel.put( new Integer( reference.hashCode() ), model );
		}
		
		return model;
	}
	
	public synchronized DetailModel removeReference( ObjectReference oRef ) throws OperationException
	{
		if (oRef == null)
			throw new OperationException( "objectReference==null" );
			
		DetailModel dModel = (DetailModel) referenceToModel.get( new Integer( oRef.hashCode() ) );
		if (dModel==null)
		{
			DC.log( LEVEL, "dModel==null" );
			throw new OperationException( "dModel==null" );
		}
		else
		{
			DC.log( LEVEL, referenceToModel.remove( new Integer( oRef.hashCode() ) ) );
			return dModel;
		}					
	}
			
	public synchronized ThreadModel killThread( ThreadReference tRef ) throws StateException, OperationException
	{
		DC.log(LEVEL, tRef );
		ThreadModel tModel = (ThreadModel) removeReference( tRef );
		tModel.transition();
		deadThreadRepository.add( tModel );
		return tModel;
	}

	/**
	 ** Restituisce solo gli oggetti del workspace, cioè creati dall'utente, hanno l'handler
	 */
	public synchronized List getWorkspaceModels()
	{
		List workspace = new LinkedList();
		Collection models = referenceToModel.values();
		
		for (Iterator it=models.iterator(); it.hasNext(); )
		{
			DetailModel dModel = (DetailModel) it.next();

			if (dModel.hasHandler() )
				workspace.add( dModel );
		}
		
		return workspace;
	}	

	public synchronized List getLiveThreads()
	{
		List lives = new LinkedList();
		Collection values = referenceToModel.values();
		
		for (Iterator it=values.iterator(); it.hasNext(); )
		{
			DetailModel dModel = (DetailModel) it.next();
			
			if (dModel instanceof ThreadModel)
			{
				lives.add( dModel );
			}
		}
	
		return lives;
	}

	public synchronized List getDeadThreads()
	{
		List list = new Vector( deadThreadRepository );
		return list;
	}
	
	public synchronized List getClassLoaders()
	{
		List cl_l = new LinkedList();
		Collection values = referenceToModel.values();
		
		for (Iterator it=values.iterator(); it.hasNext(); )
		{
			DetailModel dModel = (DetailModel) it.next();
			
			if (dModel instanceof ClassLoaderModel)
			{
				cl_l.add( dModel );
			}
		}
	
		return cl_l;
	}

	/**
	 ** Dovrebbe gestrire ObjectCollectedException
	 ** e rimuovere gli oggetti morti
	 */
	void referenceInvalid( ObjectReference reference ) throws OperationException
	{
		DetailModel model = referenceToModel( reference );
		removeReference( reference );
		
		try
		{
			if (model instanceof ObjectModel)
			{
				ObjectModel omodel = (ObjectModel) model;
				omodel.transitionToHistory();
			}// qui vanno gestiti un milione di Exception
			else
			if (model instanceof ThreadModel )
			{
				ThreadModel tmodel = (ThreadModel) model;
				tmodel.transition();
			}
		}
		catch( StateException se )
		{
			DC.log( se );
		}
		catch( OperationException oe )
		{
			DC.log( oe );
		}
		
	}
	
	public String toString()
	{
		String res = "<" + getClass() + ":";
		res += "rToM:" + referenceToModel + ",";
		res += "tRep:" + deadThreadRepository;
		res += ">";
		return res;
	}
	
	public static void main( String[] args ) throws Exception
	{
/*		ObjectReference mock_a, mock_b;

		ReferenceType mock_type_a, mock_type_b;
		
		{
			Field[] fields = new Field[] { new FieldMock("i") };
			Value[] values = new Value[] { new IntegerValueMock(71) };
			List fields_l = Arrays.asList( fields );
			List values_l = Arrays.asList( values );
			List methods_l = new LinkedList();
			
			mock_type_a = new ReferenceTypeMock( "MockType", fields_l, methods_l );		
			mock_a = new ObjectReferenceMock( mock_type_a, values_l );
		}

		{
			Field[] fields = new Field[] { new FieldMock("a"), new FieldMock( "b"), new FieldMock("c") };
			Value[] values = new Value[] { new IntegerValueMock(34), new IntegerValueMock(56), mock_a };
			List fields_l = Arrays.asList( fields );
			List values_l = Arrays.asList( values );
			List methods_l = new LinkedList();
			
			mock_type_b = new ReferenceTypeMock( "MockType", fields_l,methods_l );		
			mock_b = new ObjectReferenceMock( mock_type_b, values_l );
		}

		ObjectModel mockModel = (ObjectModel) BackEndModel.getShared().referenceToModel( mock_b );

//		ObjectModel mockModel = new ObjectModel( mock_b );
		ThreadReference tr = new ThreadReferenceMock( "Helo" );

		ThreadModel tm = (ThreadModel) BackEndModel.getShared().referenceToModel( tr );

//		BackEndModel.getShared().removeReference( mock_b );
//		BackEndModel.getShared().removeReference( tr );
//		tm.transition();
		
		System.out.println( "" + BackEndModel.getShared().getLiveThreads() );
*/
	}



		
}
