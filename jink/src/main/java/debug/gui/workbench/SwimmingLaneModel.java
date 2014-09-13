package debug.gui.workbench;

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

/* Ogni cell ha una port che contiene gli oggetti che referenziano il relativo DetailModel,
** Per gli step, lo JumpModel è memorizzato nello edge, vi è una seconda porta (con userObject ThreadModel) per lo stream.
** Quando si fa uno step l'objectModel e la relativa porta si spostano in una nuova cella.
*/
public class SwimmingLaneModel extends WorkbenchModel implements DebugOff, Disposable
{
	protected final Map modelToCell = new HashMap(); // <DetailModel> --> <GraphCell>
	protected final ModelManager modelMgr;
	
	protected SwimmingLaneLayout layoutMgr = new SwimmingLaneLayout();
	
	// timeModel
	protected TimeModel timeModel;
	protected int ltime;	
	
	// visModel
	protected VisModel visModel;
	protected boolean reference;
	protected boolean monitor;
	
	public DefaultGraphCell modelToCell( DetailModel detail_m )
	{
		if (modelToCell.containsKey( detail_m ) )
			return (DefaultGraphCell) modelToCell.get( detail_m );
		return null;
	}

	protected DefaultGraphCell newCell( DetailModel detail_m, DefaultGraphCell cell, Map attr )
	{
		Map attributes = new HashMap();
		
		attributes.put( cell, attr );
		
		Object[] cells = new Object[] { cell };
		insert( cells,  attributes, null, null, null );
		
		modelToCell.put( detail_m, cell );
		return cell;
	}
	
	protected JumpCell newJump( JumpModel jump_m, Map attr )
	{
		JumpCell cell = new JumpCell( jump_m );
		DefaultPort port = new DefaultPort( jump_m.getThread() );
		cell.add( port );
		newCell( jump_m, cell, layoutMgr.layout( jump_m ));
		return cell;
	}
	
	protected DefaultGraphCell newThread( ThreadModel thread_m )
	{
		ThreadCell cell = new ThreadCell( thread_m );
		DefaultPort port = new DefaultPort( thread_m );
		cell.add( port );
		newCell( thread_m, cell, layoutMgr.layout( thread_m )  );
		return cell;
	}		
	
	protected DefaultGraphCell newHandler( DetailModel detail_m )
	{
		DefaultGraphCell cell = new DefaultGraphCell( detail_m );
		DefaultPort port = new DefaultPort( detail_m );
		cell.add( port );
		if (reference) initOuterRefs(port );
		newCell( detail_m, cell, layoutMgr.layout( detail_m ) );
		return cell;
	}
	
	protected void initOuterRefs( DefaultPort port )
	{
		DetailModel detail_m = (DetailModel) port.getUserObject();
		
		if (detail_m instanceof ObjectModel)
		{
			try
			{
				ObjectModel object_m = (ObjectModel) detail_m;
				Map fields = object_m.getFields();
				Set keys = fields.keySet();
				
				for (Iterator i=keys.iterator(); i.hasNext(); )
				{
					Field field = (Field) i.next();
					DetailModel value_m = (DetailModel) fields.get( field );
					if ( value_m.hasHandler() )
					{
						DefaultGraphCell value_c = modelToCell( value_m );
						DefaultPort value_p = getModelPort( value_c, value_m );
						newRef( value_m ,port, value_p );
					}
				}
			}
			catch ( ModelException me )
			{	DC.log( me );	}
		}				
	}
	
	protected void disposeOuterRefs( DefaultPort port )
	{
		Set edges = port.getEdges();
		Object[] array = edges.toArray();
		remove( array );
	}

	
	protected DefaultGraphCell firstStep( JumpModel jump_m )
	{
		ThreadModel thread_m = jump_m.getThread();
		DefaultGraphCell jump_c = newJump( jump_m, layoutMgr.layout( jump_m ) );
		DefaultGraphCell thread_c = modelToCell( thread_m );
		DefaultPort jump_p = getModelPort(  jump_c, thread_m );
		DefaultPort thread_p = getModelPort(  thread_c, thread_m );
		
		newEdge( jump_m, thread_p, jump_p );
		
		return jump_c;
	}
	
	
	protected DefaultGraphCell doStep( ThreadModel thread_m, JumpModel jump_m, JumpModel prev_m )
	{
		Map attributes = new HashMap();
		DefaultGraphCell prev_c = modelToCell( prev_m );
		
		DefaultGraphCell jump_c = newJump( jump_m, layoutMgr.layout( jump_m ) );

		DefaultPort prev_p = getModelPort( prev_c, thread_m );
		DefaultPort step_p = getModelPort( jump_c, thread_m );

		newEdge( jump_m, prev_p, step_p );
		
		return jump_c;
	}
	
	

/*	public void update( DetailModel detail_m )
	{
		GraphCell cell = (DefaultGraphCell) modelToCell( detail_m );
		Map cellAttr = GraphConstants.createMap();
		GraphConstants.setValue( cellAttr, detail_m );
		cell.changeAttributes( cellAttr );
	}
*/	

	//// end inserters	

	public void register( TimeModel _timeModel )
	{
		timeModel = _timeModel;
		timeModel.addTimeListener( timeListener );
		ltime = timeModel.getLTime();
	}
	
	public void register( VisModel _visModel )
	{
		visModel = _visModel;
		visModel.addVisListener( visListener );
		monitor = visModel.getMonitor();
		reference = visModel.getReference();
	}
	
	public SwimmingLaneModel( ModelManager _modelMgr )
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

	public void dispose()
	{
		modelMgr.removeModelListener( modelController );
		timeModel.removeTimeListener( timeListener );
		visModel.removeVisListener( visListener );
		modelToCell.clear();
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
			DC.log( LEVEL, "" + ltime );

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
				SwimmingLaneModel.this.newHandler( handler_m );
		}
	};


	protected void updateSteps( int _ltime )
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
				updateStepsAdd( thread_m, _ltime );
			else
				updateStepsRemove( thread_m, _ltime );
		}
		
		List hist_l = backEndModel.getDeadThreads();
		for ( Iterator i=hist_l.iterator(); i.hasNext(); )
		{
			ThreadModel thread_m = (ThreadModel) i.next() ;
			if (adding)	
				updateStepsAdd( thread_m, _ltime );
			else
				updateStepsRemove( thread_m, _ltime );
		}
		
	}			

	protected void updateStepsAdd( ThreadModel thread_m, int _ltime )
	{
		try
		{
			int depth = thread_m.timelineDepth();
			
			// i valori accettati sono da _ltime a ltime-1;
			JumpModel prev_m, cell_m;
//			DefaultGraphCell prev_c, cell_c;
//			DefaultPort prev_p, cell_p;

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
				cell_m = thread_m.getJump( 0 );
				firstStep( cell_m );
/*				
				cell_c = firstStep( cell_m );
				cell_p = getModelPort( cell_c, thread_m );

				DefaultGraphCell thread_c = modelToCell( thread_m );
				DefaultPort thread_p = getModelPort( thread_c, thread_m );

				newEdge( cell_m, thread_p, cell_p );
*/
				j++;
			}
			else
			{
				cell_m = thread_m.getJump( j-1 );
//				cell_c = modelToCell( cell_m );
//				cell_p = getModelPort( cell_c, thread_m );
			}
						
			for ( int i=j; i<depth; ++i )
			{
				JumpModel jump_m = thread_m.getJump( i );
				int jump_t = jump_m.getTime();
				if ( jump_t<ltime)
				{
					prev_m = cell_m;
					cell_m = thread_m.getJump( i );
					doStep( thread_m, cell_m, prev_m );
/*					prev_m = cell_m;
					prev_c = cell_c;
					prev_p = cell_p;
				
//					cell_c = modelToCell( cell_m );
					cell_p = getModelPort( cell_c, thread_m );
					
					if (getModelEdge( cell_p, cell_m ) == null )
						newEdge( cell_m, prev_p, cell_p );
*/
				}
				else
					return;
			}
			 
		}
		catch( OperationException oe )
		{
			DC.log( oe );
		}
	}

	protected void updateStepsRemove( ThreadModel thread_m, int _ltime )
	{
		try
		{
			List remove_l = new LinkedList();
			List edges = new LinkedList();
	
			// i valori accettati sono da ltime-1 a _ltime-1
			DC.log( LEVEL, _ltime + " " + ltime );
			for ( int j=thread_m.timelineDepth()-1; j>=0; --j )
			{
				JumpModel jump_m = thread_m.getJump( j );
				int jump_t = jump_m.getTime();
				if ( jump_t>=ltime)
				{
					DefaultGraphCell jump_c = modelToCell( jump_m );
					if (jump_c!=null)
					{
						remove_l.add( jump_c );
										
						DefaultPort jump_p = getModelPort( jump_c, thread_m );
						DC.log( LEVEL, jump_p );
						if (jump_p!=null)
						{
							DefaultEdge jump_e = getModelEdge( jump_p, jump_m );
							if (jump_e!=null)
							{
								remove_l.add( jump_e );
								remove_l.add( jump_p );
								DC.log( jump_e );
							}
						}
					}
				}
				else
				{
					break;
				}
			}

			DC.log( LEVEL, "" + remove_l.size() );
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
			updateSteps( _ltime );
		}
		
		// ltime=gtime
		public void now( TimeEvent time_e )
		{
			DC.log( LEVEL, "" + ltime );
			int _ltime = ltime;
			ltime = time_e.ltime;
			updateSteps( _ltime );
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
	
	
	private void updateRefs()
	{
		if (reference)
			updateRefsOn();
		else
			updateRefsOff();
	}
	
	private void updateRefsOn()
	{
		Set models = modelToCell.keySet();
		
		for (Iterator i=models.iterator(); i.hasNext(); )
		{
			DetailModel detail_m = (DetailModel) i.next();
			if ( (detail_m instanceof ObjectModel ) && !(detail_m instanceof ThreadModel) ) 
			{
				ObjectModel object_m = (ObjectModel) detail_m;
				DC.log( LEVEL, object_m );
				DefaultGraphCell cell = (DefaultGraphCell) modelToCell.get( object_m );
				DefaultPort port = getModelPort( cell, object_m );
				if (port!=null)
					initOuterRefs( port );
			}
		}
	}
	
	private void updateRefsOff()
	{
		Set models = modelToCell.keySet();
		
		for (Iterator i=models.iterator(); i.hasNext(); )
		{
			DetailModel detail_m = (DetailModel) i.next();
			if ( (detail_m instanceof ObjectModel ) && !(detail_m instanceof ThreadModel) ) 
			{
				ObjectModel object_m = (ObjectModel) detail_m;
				DC.log( LEVEL, object_m );
				DefaultGraphCell cell = (DefaultGraphCell) modelToCell.get( object_m );
				DefaultPort port = getModelPort(cell, object_m );
				if (port!=null)
					disposeOuterRefs( port );
			}
		}
	}
	
	
	protected VisListener visListener = new VisListener()
	{
		public void monitor( boolean status )
		{
			monitor = status;
		}
		
		public void reference( boolean status )
		{
			if (reference!=status)
			{
				reference = status;
				
				updateRefs();
			}
		}
	};
}

