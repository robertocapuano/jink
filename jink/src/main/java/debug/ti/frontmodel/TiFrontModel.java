package debug.ti.frontmodel;

import com.sun.jdi.*;

import java.util.*;
import java.awt.Rectangle;

import org.jgraph.JGraph;
import org.jgraph.graph.*;

import debug.model.event.*;
import debug.model.thread.*;
import debug.model.*;
import debug.model.object.*;

import debug.gui.Disposable;
import debug.gui.model.*;
import debug.gui.event.*;

import tools.*;

import debug.ti.backmodel.*;

/* Ogni cell ha una port che contiene gli oggetti che referenziano il relativo DetailModel,
** Per gli step, lo JumpModel è memorizzato nello edge, vi è una seconda porta (con userObject ThreadModel) per lo stream.
** Quando si fa uno step l'objectModel e la relativa porta si spostano in una nuova cella.
*/
public class TiFrontModel extends TiFactory implements Disposable
{
	protected ModelManager modelMgr;
	
	public final Map uidToCell = new HashMap(); // <UID> --> <DetailCell>/<CompoundCell>
	
	protected TiBackModel backModel;
	
	// timeModel
	protected TimeModel timeModel;
	protected int ltime = 0;	
	
	// visModel
	protected VisModel visModel;
	protected boolean showRefs;
	protected boolean showMonitors;

	protected CellWithTime uidToCell( DetailModel detail_m )
	{
		return uidToCell( detail_m.getUID() );
	}
	
	protected CellWithTime uidToCell( int uid )
	{
		Integer UID = new Integer( uid );
		
		if (uidToCell.containsKey( UID ) )
			return (CellWithTime) uidToCell.get( UID );
		return null;
	}

	protected void putModel( DetailModel detail_m, CellWithTime cell )
	{
		putUID( detail_m.getUID(), cell );
	}
	
	protected void putUID( int uid, CellWithTime cell )
	{
		Integer UID = new Integer( uid );		
		uidToCell.put( UID, cell );
	}

	protected void removeModel( DetailModel detail_m  )
	{
		removeUID( detail_m.getUID() );
	}
		
	protected void removeUID( int uid )
	{
		Integer UID = new Integer( uid );		
		uidToCell.remove( UID );
	}
	
	public TiFrontModel( ModelManager _modelMgr, TiBackModel _backModel, TimeModel _timeModel, VisModel _visModel )
	{
		super();
		
		modelMgr = _modelMgr;
		
		backModel = _backModel;
		timeModel = _timeModel;
		visModel = _visModel;
		
		if ( modelMgr!=null)
			modelMgr.addModelListener( modelListener );
		if ( timeModel != null )
			timeModel.addTimeListener( timeListener );
		if ( visModel != null )
		{
			visModel.addVisListener( visListener );
			showRefs = visModel.getReference();
			showMonitors = visModel.getMonitor();
		}
	}

	public void dispose()
	{
		modelMgr.removeModelListener( modelListener );
		modelMgr = null;
		timeModel.removeTimeListener( timeListener );
		timeModel = null;
		visModel.removeVisListener( visListener );
		visModel = null;
		uidToCell.clear();
	}

	//// Zona Object
	public ObjectCell newObjectModel( ObjectModel object_m )
	{
		ObjectCell object_c = newObjectCell( object_m );
		putModel( object_m, object_c );
		fire();
		return object_c;
	}
	/// end Object
	
	//// Zona Compound
	public void extractCompound( CompoundCell compoundCell )
	{
		super.extractCompoundCell( compoundCell );
		
		DetailCell[] details_c = compoundCell.getAllInnerCells();
		for ( int i=0; i<details_c.length; ++i )
		{
			DetailModel detail_m = (DetailModel) details_c[i].getUserObject();
			if (! details_c[i].isMemento() )
			{
				putModel( detail_m, details_c[i] ); 
			}
			else
			{
				removeModel( detail_m );
			}
			fire();
		}
		
	}

	public void newCompound( Object[] cells )
	{
		CompoundCell compoundCell = super.newCompoundCell( cells );
		DetailModel[] details_m = compoundCell.getInnerModels();
		
		for ( int i=0; i<details_m.length; ++i )
		{
			putModel( details_m[i], compoundCell );
			fire();
		}
	}
	//// end Compound
	
	//// Zona Thread
	
	public ThreadCell newThreadModel( ThreadModel thread_m )
	{
		ThreadCell thread_c = newThreadCell( thread_m );
		putModel( thread_m, thread_c );
		fire();
		return thread_c;
	}
	
	public DefaultEdge firstJumpModel( ThreadModel thread_m, JumpModel jump_m, ObjectModel object_m )
	{
		CellWithTime thread_c = (CellWithTime) uidToCell( thread_m );
		if ( thread_c == null )
		{
			thread_c = newThreadModel( thread_m );
			putModel( thread_m, thread_c );
		}
		
		CellWithTime cell = uidToCell( object_m );
		if (cell==null)
		{
			cell = newObjectModel( object_m );
			putModel( object_m, cell );
		}
			
		DefaultPort thread_p = getModelPort( thread_c, thread_m );
		DefaultPort object_p = getModelPort( cell, object_m );
		
		DefaultEdge jump_e = newJumpEdge( jump_m, thread_c, thread_p, cell, object_p );
		return jump_e;		
	}
	
	public DefaultEdge newJumpModel( ObjectModel prev_m, JumpModel jump_m, ObjectModel object_m )
	{
		ThreadModel thread_m = jump_m.getThread();
		
		CellWithTime prev_c = uidToCell( prev_m );
		if (prev_c==null)
		{
			prev_c = newObjectModel( prev_m );
			putModel( prev_m, prev_c );
		}
		
		DefaultPort prev_p = getModelPort( prev_c, prev_m );
	
		CellWithTime cell = uidToCell( object_m );
		if ( cell == null )
		{
			cell = newObjectModel( object_m );
			putModel( object_m, cell );
		}
		
		DefaultPort detail_p = getModelPort( cell, object_m );
		
		DefaultEdge jump_e = newJumpEdge( jump_m, prev_c, prev_p, cell, detail_p );
		return jump_e;
	}
	//// end zona Thread
	
//// model controller
	protected ModelListener modelListener = new ModelListener()
	{
		public void threadNew( ThreadNewEvent threadNewEvent )
		{
			ThreadModel thread_m = threadNewEvent.getThreadModel();
			GraphCell cell = uidToCell( thread_m );
			if (cell==null)
				newThreadModel( thread_m );
		}
		
		public void threadJump( ThreadJumpEvent threadJumpEvent )
		{
/*			DC.log( LEVEL, "" + ltime );

			try
			{
				ThreadModel thread_m = threadJumpEvent.getThreadModel();
				
//				ltime = Math.max( ltime, thread_m.timelineDepth() );
				
				int step_i = thread_m.timelineDepth() -1;
				JumpModel jump_m = thread_m.getJump( step_i );
				
				if (step_i==0)
				{
					firstStep(  jump_m );
				}
				else
				{
					int prev_i = step_i -1;
					JumpModel prev_m = thread_m.getJump( prev_i );
					DC.log(LEVEL, prev_m );
					DC.log( LEVEL, jump_m );
					doStep( jump_m, prev_m );
				}
			}
			catch( OperationException oe )
			{
				DC.log( oe );
			}
*/
		}
		
		public void threadStep( ThreadStepEvent threadStepEvent )
		{
		}
		
		public void threadEnd( ThreadEndEvent threadEndEvent )
		{
//			ThreadModel thread_m = threadEndEvent.getThreadModel();
		}
		
		public void vmLaunch( VMLaunchEvent vmLaunchEvent )
		{
			uidToCell.clear();
		}
		
		public void vmQuit( VMQuitEvent vmQuitEvent )
		{
			uidToCell.clear();
		}
		
		public void newHandler( NewHandlerEvent newHandlerEvent )
		{
/*			DetailModel handler_m = newHandlerEvent.getDetailModel();
			GraphCell cell = modelToCell( handler_m );
			if (cell==null)
				newObjectModel( (ObjectModel) handler_m );
*/
		}
	};
	
	public void setLTime( int new_ltime )
	{
		int _ltime = ltime;
		ltime = new_ltime;
		update( _ltime, ltime );
	}
	
	public void setShowRefs( boolean on )
	{
		showRefs = on;
//		update
	}
	
	public void setShowMonitors( boolean on )
	{
		showMonitors = on;
//		update
	}

	protected void update( int _ltime, int ltime )
	{
		if (ltime<_ltime)
		{
			flashback( _ltime, ltime ); // archi
			timeBump( ltime ); // celle
		}
		else
		if (ltime>_ltime)
		{
			flashforward( _ltime, ltime ); // archi
			timeBump( ltime ); // celle
		}
		else
		{
			// NO-OP
		}
	}
	
	private void timeBump( int ltime )
	{
		List uid_l = new LinkedList();
		Set cells_s = new HashSet( );
		
		Set entries = uidToCell.entrySet();
		for (Iterator i=entries.iterator(); i.hasNext(); )
		{
			Map.Entry entry = (Map.Entry) i.next();
			CellWithTime cell = (CellWithTime) entry.getValue();
			cell.timeBump( ltime );
			
			if ( cell.isMemento() )
			{
				Integer uid = (Integer) entry.getKey();
				uid_l.add( uid );
				cells_s.add( cell );
			}			
		}
		
		for ( Iterator i=uid_l.iterator(); i.hasNext(); )
		{
			Integer uid = (Integer) i.next();
			removeUID( uid.intValue() );
		}

		for ( Iterator i=cells_s.iterator(); i.hasNext(); )
		{
			DefaultGraphCell cell = (DefaultGraphCell) i.next();
			removeCell( cell );
		}
		
		fire();
	}
	
	private void flashback( int _ltime, int ltime )
	{
		BackEndModel repository = modelMgr.getBackEndModel();
		
		List threads_l = repository.getLiveThreads();
		// **todo: il thread morto resta nella lista dei vivi!
		threads_l.addAll( repository.getDeadThreads() );
		
		for ( Iterator i=threads_l.iterator(); i.hasNext(); )
		{
			ThreadModel thread_m = (ThreadModel) i.next();
			flashback( _ltime, ltime, thread_m );
		}	
	}

	// ltime < _ltime 
	// i valori da rimuovere sono da ltime a _ltime-1;
	private void flashback( int _ltime, int ltime, ThreadModel thread_m )
	{
		try
		{
			int depth = thread_m.timelineDepth();
			
			if ( depth==0 || thread_m.getJump( depth-1 ).getTime() < ltime || thread_m.getJump( 0 ).getTime() >= _ltime )
				// il thread è fuori dalla finestra temporale
				return;
	
			DC.log( LEVEL, thread_m );		
			List remove_l = new LinkedList();
	
			DC.log( LEVEL, ltime + " " + _ltime );
			
			int i;
			for ( i=0; i<depth && thread_m.getJump(i).getTime()<ltime; ++i );
			int first_jump = i;
			
			for ( i=first_jump+1; i<depth && thread_m.getJump(i).getTime()<_ltime; ++i );
			int last_jump = i-1;
			
			DC.log( LEVEL, "" + first_jump + " " + last_jump );

			for ( int j=first_jump; j<=last_jump; ++j )
			{
				JumpModel jump_m = thread_m.getJump( j );
				ObjectModel object_m = jump_m.getObject();
				DC.log( "" + object_m );
				CellWithTime cell = uidToCell( object_m );
				DefaultPort thread_p = getModelPort( cell, object_m );
				if (thread_p!=null)
				{
					DefaultEdge jump_e = getModelEdge( thread_p, jump_m );
					DC.log( "" + jump_e );
					if (jump_e!=null)
						remove_l.add( jump_e );
				}				
//				cell.timeBump( ltime );
//				object_c.setUserObject(  selectObject( jump_m.getObject(), ltime ) );
			}
			
			DC.log( LEVEL, "" + remove_l.size() );
			remove( remove_l.toArray() );
			
		}
		catch( OperationException oe )
		{
			DC.log( LEVEL, oe );
		}
	
	}
	
	private void flashforward( int _ltime, int ltime )
	{
		BackEndModel repository = modelMgr.getBackEndModel();
		
		List threads_l = repository.getLiveThreads();
		threads_l.addAll( repository.getDeadThreads() );
		
		for ( Iterator i=threads_l.iterator(); i.hasNext(); )
		{
			ThreadModel thread_m = (ThreadModel) i.next();
			DC.log( LEVEL, thread_m );
			flashforward( _ltime, ltime, thread_m );
		}
	}

	// i valori accettati sono da _ltime a ltime-1;
	// _ltime < ltime
	private void flashforward( int _ltime, int ltime, ThreadModel thread_m )
	{
		try
		{
			int depth = thread_m.timelineDepth();
			DC.log( LEVEL, "depth:"+depth );
			DC.log( LEVEL, "_ltime:"+_ltime + " ltime: " + ltime);
			int j;
			
			for ( j=0; j<depth; ++j )
			{
				int time_j = thread_m.getJump(j).getTime();
				DC.log( LEVEL, "" + time_j );
				if (time_j>=_ltime && time_j<ltime)
					break;
			}
		
			if (j==depth) return;
			
			DC.log( LEVEL, "j:"+j );
			if (j==0)
			{
				JumpModel jump_m = thread_m.getJump( 0 );
				ObjectModel live_m = jump_m.getObject();
				firstJumpModel( thread_m, jump_m, live_m );
				++j;
			}
		
			for ( int i=j; i<depth; ++i )
			{
				JumpModel jump_m = thread_m.getJump( i );
				int jump_t = jump_m.getTime();
				DC.log( LEVEL, "" + jump_t );
				if ( jump_t<ltime)
				{
					ObjectModel live_m = jump_m.getObject();
					ObjectModel prev_m = thread_m.getJump( i-1 ).getObject();
					newJumpModel( prev_m, jump_m, live_m );
				}
				else
					return;
			}
		}
		catch( OperationException oe )
		{
			DC.log( LEVEL, oe );
		}
	}
	
	
	protected TimeListener timeListener = new TimeListener()
	{
		// cambiato ltime
		public void leap( TimeEvent time_e )
		{
			DC.log( LEVEL, "" +ltime);
			setLTime( time_e.ltime );
		}
		
		// ltime=gtime
		public void now( TimeEvent time_e )
		{
			setLTime( time_e.ltime );
		}
		
		// cambiato gtime
		public void jump( TimeEvent time_e )
		{
		}
		
		// cambiato freeze
		public void freezed( TimeEvent time_e )
		{
		}
	};
	
	protected VisListener visListener = new VisListener()
	{
		public void monitor( boolean status )
		{
			showMonitors = status;
		}
		
		public void reference( boolean status )
		{
			if (showRefs!=status)
			{
				showRefs = status;
				
			}
		}
	};
	
	protected List listeners = new LinkedList();
	
	
	public void addFMListener( FMListener listener )
	{
		listeners.add( listener );
	}
	
	public void removeFMListener( FMListener listener )
	{
		listeners.remove( listener );
	}
	
	public void fire()
	{
		for ( Iterator i=listeners.iterator(); i.hasNext(); )
		{
			FMListener listener = (FMListener) i.next();
			listener.change();
		}
	}
	


/*
	protected CMListener coreListener = new CMListener()
	{
		public void change()
		{
		}
	};
*/	
}

