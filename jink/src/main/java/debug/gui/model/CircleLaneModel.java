package debug.gui.model;

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

import debug.gui.workbench.WorkbenchModel;

import tools.*;

/* Ogni cell ha una port che contiene gli oggetti che referenziano il relativo DetailModel,
** Per gli step, lo JumpModel è memorizzato nello edge, vi è una seconda porta (con userObject ThreadModel) per lo stream.
** Quando si fa uno step l'objectModel e la relativa porta si spostano in una nuova cella.
*/
public class CircleLaneModel extends WorkbenchModel implements DebugOff, Disposable
{
	protected final Map modelToCell = new HashMap(); // <DetailModel> --> <GraphCell>
	protected final ModelManager modelMgr;

	protected TimeModel timeModel;
	int ltime = 0;
		
	protected GraphApparence apparenceMgr;
	    
	public DefaultGraphCell modelToCell( DetailModel detail_m )
	{
		if (modelToCell.containsKey( detail_m ) )
			return (DefaultGraphCell) modelToCell.get( detail_m );
		return null;
	}

	public void register( TimeModel _timeModel )
	{
		timeModel = _timeModel;
		timeModel.addTimeListener( timeListener );
		ltime = timeModel.getLTime();
	}
	
	public void attach( GraphApparence _apparenceMgr )
	{
		apparenceMgr = _apparenceMgr;
	}
	
	CircleLaneModel( ModelManager _modelMgr)
	{
		super();
		
		modelMgr = _modelMgr;
		modelMgr.addModelListener( modelController );

		BackEndModel backEndModel = modelMgr.getBackEndModel();

		List ws_l = backEndModel.getWorkspaceModels();
		for ( Iterator i=ws_l.iterator(); i.hasNext(); )
		{
			DetailModel dmodel = (DetailModel) i.next() ;
			newHandler( dmodel );
		}

		List live_l = backEndModel.getLiveThreads();
		for ( Iterator i=live_l.iterator(); i.hasNext(); )
		{
			ThreadModel thread_m = (ThreadModel) i.next() ;
			newThread( thread_m );
		}
		
		List hist_l = backEndModel.getDeadThreads();
		for ( Iterator i=hist_l.iterator(); i.hasNext(); )
		{
			ThreadModel thread_m = (ThreadModel) i.next() ;
			newThread( thread_m );
		}
	}

	protected DefaultGraphCell newCell( DetailModel detail_m, Map attr )
	{
		Map attributes = new HashMap();
		
		// cell
		DefaultGraphCell cell = new DefaultGraphCell( detail_m );

		attributes.put( cell, attr );
		
		Object[] cells = new Object[] { cell };
		insert( cells,  attributes, null, null, null );
		
		modelToCell.put( detail_m, cell );
		return cell;
	}
	
	protected DefaultGraphCell newThread( ThreadModel thread_m )
	{
		DefaultGraphCell cell = newCell( thread_m, apparenceMgr.present( thread_m )  );
		DefaultPort port = new DefaultPort( thread_m );
		cell.add( port );
		return cell;
	}		
	
	protected DefaultGraphCell newHandler( DetailModel detail_m )
	{
		DefaultGraphCell cell = newCell( detail_m, apparenceMgr.present( detail_m ) );
		DefaultPort port = new DefaultPort( detail_m );
		cell.add( port );
		return cell;
	}
	
	protected DefaultGraphCell newObject( ThreadModel thread_m, ObjectModel object_m )
	{
		DefaultGraphCell cell = newCell( object_m, apparenceMgr.present(object_m ) );
		DefaultPort obj_p = new DefaultPort( object_m );
		cell.add( obj_p );
		DefaultPort thr_p = new DefaultPort( thread_m );
		cell.add( thr_p );
		return cell;
	}
	
	protected DefaultGraphCell firstStep( JumpModel jump_m )
	{
		try
		{
			ThreadModel thread_m = jump_m.getThread();
	//		if (jump_m.getThread()!=thread_m) DC.log( LEVEL, "" + jump_m.getThread() );
			Map attributes = new HashMap();
	
			DefaultGraphCell thread_c = modelToCell( thread_m );
			DefaultPort thread_p = getModelPort( thread_c, thread_m );
			
			ObjectModel live_m = jump_m.getObject().getLiveModel();
			DefaultGraphCell live_c = modelToCell( live_m );
			if (live_c==null)
				live_c = newObject( thread_m, live_m );
				
			DefaultPort live_p = getModelPort( live_c, thread_m );
			if (live_p==null)
			{
				live_p = new DefaultPort( thread_m );
				live_c.add( live_p );
			}
			
	//		DefaultEdge	edge = GraphUtilities.newEdge( this, jump_m, thread_p, live_p );
	//		return edge;
		}
		catch( OperationException oe )
		{
			DC.log( LEVEL, oe );
		}
	
		return null;
	}
	
	
	protected Edge doStep( JumpModel jump_m, JumpModel prev_m )
	{
		try
		{
			ThreadModel thread_m = jump_m.getThread();
	//		if (jump_m.getThread()!=thread_m) DC.log( LEVEL, "" + jump_m.getThread() );
			Map attributes = new HashMap();
			ObjectModel prev_live_m = prev_m.getObject().getLiveModel();
			DefaultGraphCell prev_live_c = modelToCell( prev_live_m );
			if (prev_live_c==null)
			{
				prev_live_c = newObject( thread_m, prev_live_m );
			}
			
			ObjectModel step_live_m = jump_m.getObject().getLiveModel();
			DefaultGraphCell step_live_c = modelToCell( step_live_m );
			if (step_live_c==null)
			{
				step_live_c = newObject( thread_m, step_live_m );
			}
	
			DefaultPort prev_live_p = getModelPort(prev_live_c, thread_m );
			if (prev_live_p==null)
			{
				prev_live_p = new DefaultPort( thread_m );
				prev_live_c.add( prev_live_p );
			}
			
			DefaultPort step_live_p = getModelPort( step_live_c, thread_m );
			if (step_live_p==null)
			{
				step_live_p = new DefaultPort( thread_m );
				step_live_c.add( step_live_p );
			}
			
			// **todo: un hack per evitare la cacofonia del disegno
			if (step_live_p == prev_live_p) return null;
			// edge
	//		DefaultEdge	edge = GraphUtilities.newEdge( this, jump_m, prev_live_p, step_live_p );
	//		return edge;
		}
		catch( OperationException oe )
		{
			DC.log( LEVEL, oe );
		}
	
		return null;
	}
	
	
	public void dispose()
	{
		modelMgr.removeModelListener( modelController );
		modelToCell.clear();
		timeModel.removeTimeListener( timeListener );
	}

	protected ModelListener modelController = new ModelListener()
	{
		public void threadNew( ThreadNewEvent threadNewEvent )
		{
			ThreadModel thread_m = threadNewEvent.getThreadModel();
			GraphCell cell = modelToCell( thread_m );
			if (cell==null)
				newThread( thread_m );
		}
		
		public void threadJump( ThreadJumpEvent threadJumpEvent )
		{
			try
			{
				ThreadModel thread_m = threadJumpEvent.getThreadModel();
				
				int step_i = thread_m.timelineDepth() -1;
				JumpModel jump_m = thread_m.getJump( step_i );
				
				if (step_i==0)
				{
					firstStep( jump_m );
				}
				else
				{
					int prev_i = step_i -1;
					JumpModel prev_m = thread_m.getJump( prev_i );

					doStep( jump_m, prev_m );
				}
			}
			catch( OperationException oe )
			{
				DC.log( oe );
			}
		}
		
		public void threadStep( ThreadStepEvent threadStepEvent )
		{
		}
		
		public void threadEnd( ThreadEndEvent threadEndEvent )
		{
			ThreadModel thread_m = threadEndEvent.getThreadModel();
		}
		
		public void vmLaunch( VMLaunchEvent vmLaunchEvent )
		{
			modelToCell.clear();
		}
		
		public void vmQuit( VMQuitEvent vmQuitEvent )
		{
			modelToCell.clear();
		}
		
		public void newHandler( NewHandlerEvent newHandlerEvent )
		{
			DetailModel handler_m = newHandlerEvent.getDetailModel();
			GraphCell cell = modelToCell( handler_m );
			if (cell==null)
				CircleLaneModel.this.newHandler( handler_m );
		}
	};


	protected void update( int _ltime )
	{
		if (ltime==_ltime)
			return;

		DC.log( LEVEL, "" );
			
		boolean adding = (ltime>_ltime);
		
		BackEndModel backEndModel = modelMgr.getBackEndModel();

		List live_l = backEndModel.getLiveThreads();
		for ( Iterator i=live_l.iterator(); i.hasNext(); )
		{
			ThreadModel thread_m = (ThreadModel) i.next() ;
			if (adding)	
				update_add( thread_m, _ltime );
			else
				update_remove( thread_m, _ltime );
		}
		
		List hist_l = backEndModel.getDeadThreads();
		for ( Iterator i=hist_l.iterator(); i.hasNext(); )
		{
			ThreadModel thread_m = (ThreadModel) i.next() ;
			if (adding)	
				update_add( thread_m, _ltime );
			else
				update_remove( thread_m, _ltime );
		}
		
	}			

	// i valori accettati sono da _ltime a ltime-1;
	// _ltime < ltime
	protected void update_add( ThreadModel thread_m, int _ltime )
	{
		try
		{
			int depth = thread_m.timelineDepth();
			
			// i valori accettati sono da _ltime a ltime;
			DetailModel prev_m, cell_m;
			DefaultGraphCell prev_c, cell_c;
			DefaultPort prev_p, cell_p;

			int j;
			int time_j = 0;
		
			for ( j=0; j<depth; j++ )
			{
				time_j = thread_m.getJump(j).getTime();
				if (time_j>=_ltime && time_j<ltime)
					break;
			}
			
			if (j==depth) return;

			if (j==0)
			{
				DefaultGraphCell thread_c = modelToCell( thread_m );
				DefaultPort thread_p = getModelPort( thread_c, thread_m );
				cell_m = thread_m;
				cell_c = thread_c;
				cell_p = thread_p;
			}
			else
			{
				cell_m = thread_m.getJump( j-1 ).getObject().getLiveModel();
				cell_c = modelToCell( cell_m );
				cell_p = getModelPort( cell_c, thread_m );
			}
						
			for ( int i=j; i<depth; ++i )
			{
				JumpModel jump_m = thread_m.getJump( i );
				int jump_t = jump_m.getTime();
				if ( jump_t<ltime)
				{
					prev_c = cell_c;
					prev_p = cell_p;
				
					cell_m = thread_m.getJump( i ).getObject().getLiveModel();
					cell_c = modelToCell( cell_m );
					cell_p = getModelPort( cell_c, thread_m );
					
					if ( getModelEdge( cell_p, jump_m ) == null )
					{
						if (prev_p!=cell_p && prev_p!=null && cell_p!=null)
							newEdge( jump_m, prev_p, cell_p );
					}
				}
//				else
//					return;
			}
		}
		catch( OperationException oe )
		{
			DC.log( oe );
		}
	}

	protected void update_remove( ThreadModel thread_m, int _ltime )
	{
		try
		{
			List remove_l = new LinkedList();
	
			// i valori accettati sono da ltime a _ltime
			DC.log( LEVEL, _ltime + " " + ltime );
			for ( int j=thread_m.timelineDepth()-1; j>=0; --j )
			{
				JumpModel jump_m = thread_m.getJump( j );
				int jump_t = jump_m.getTime();
				if ( jump_t>=ltime)
				{
					DetailModel live_m = jump_m.getObject().getLiveModel();
					DefaultGraphCell live_c = modelToCell( live_m );
					DefaultPort live_p = getModelPort( live_c, thread_m );
					DefaultEdge jump_e = getModelEdge( live_p, jump_m );
					if (jump_e!=null)
					{
						remove_l.add( jump_e );
						DC.log( jump_e );
					}
				}
				else
					break;
			}

			DC.log( LEVEL, "" + remove_l.size() );
			if (remove_l.size()>0)
				remove( remove_l.toArray( ) );
		}
		catch( OperationException oe )
		{
			DC.log( oe );
		}
	}

	protected TimeListener timeListener = new TimeListener()
	{
		// cambiato ltime
		public void leap( TimeEvent time_e )
		{
			DC.log( LEVEL, "" +ltime);
			int _ltime = ltime;
			ltime = time_e.ltime;
			update( _ltime );
		}
		
		// ltime=gtime
		public void now( TimeEvent time_e )
		{
			DC.log( LEVEL, "" + ltime );
			int _ltime = ltime;
			ltime = time_e.ltime;
			update( _ltime );
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

}

