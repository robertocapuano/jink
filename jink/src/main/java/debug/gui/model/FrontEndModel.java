package debug.gui.model;

import debug.model.*;
import debug.gui.*;
import debug.gui.event.*;
import debug.runtime.*;

import debug.gui.workbench.SwimmingLaneModel;

//import debug.ti.coremodel.TiBackModel;
//import debug.ti.graphmodel.TiFrontModel;

import tools.*;

public class FrontEndModel implements Disposable
{
	public final static String VERSION = "0.5";
	
	protected final ClassLoadersFrontModel classLoadersFrontModel;
	protected final ClassesFrontModel classesFrontModel;
	protected final ProcessFrontModel processFrontModel;
	protected final HistoryFrontModel historyFrontModel;
	protected final ThreadFrontModel threadFrontModel;
	protected final WorkspaceFrontModel workspaceFrontModel;
	
	protected final TimeModel timeModel;
	protected final VisModel visModel;
	
	protected final SwimmingLaneModel swimmingLaneModel;
	protected final CircleLaneModel circleLaneModel;
	
	protected final BackEndListModel backEndListModel;
	
//	protected final TiBackModel TiBackModel;
//	protected final TiFrontModel TiFrontModel;
	
	public ClassLoadersFrontModel 	getClassLoadersFrontModel() 	{ return classLoadersFrontModel; }
	public ClassesFrontModel 		getClassesFrontModel() 			{ return classesFrontModel; }
	public ProcessFrontModel 		getProcessFrontModel() 			{ return processFrontModel; }
	public HistoryFrontModel 		getHistoryFrontModel()			{ return historyFrontModel; }
	public ThreadFrontModel 		getThreadFrontModel() 			{ return threadFrontModel; }
	public WorkspaceFrontModel 		getWorkspaceFrontModel() 		{ return workspaceFrontModel; }
	public TimeModel				getTimeModel()					{ return timeModel; }

	public SwimmingLaneModel		getSwimmingLaneModel()			{ return swimmingLaneModel; }
	public CircleLaneModel 			getCircleLaneModel()			{ return circleLaneModel; }

	public VisModel					getVisModel()					{ return visModel; }

	public BackEndListModel			getBackEndListModel()			{ return backEndListModel; }	
//	public TiBackModel				getTiBackModel()				{ return TiBackModel; }
//	public TiFrontModel				getTiFrontModel()				{ return TiFrontModel; }
	
	public FrontEndModel( ModelManager modelMgr, RuntimeManager runtime )
	{
		// l'ordine di inizializzazione è rilevante.
		swimmingLaneModel = new SwimmingLaneModel( modelMgr );
		circleLaneModel = new CircleLaneModel( modelMgr );
		/* il timeModel è iniziliazzato in due passaggi per motivi di temporizzazione.
			swimmingLaneModel riceverà gli eventi ModelEvent prima di TimeModel.
			In questo modo gli eventi del modello vengono usati per aggiungere i vertici al grafo ed
			gli eventi del TimeModel per l'aggiunta degli archi.
			**todo: riscriverlo in italiano.
		*/
		timeModel = new TimeModel( modelMgr );
		visModel = new VisModel( runtime );
		
		classLoadersFrontModel = new ClassLoadersFrontModel( modelMgr );
		classesFrontModel = new ClassesFrontModel( modelMgr );
		
		processFrontModel = new ProcessFrontModel( modelMgr );
		historyFrontModel = new HistoryFrontModel( modelMgr );
		threadFrontModel = new ThreadFrontModel( modelMgr, timeModel );
		workspaceFrontModel = new WorkspaceFrontModel( modelMgr );
		
		swimmingLaneModel.register( timeModel );
		swimmingLaneModel.register( visModel );
		
		circleLaneModel.register( timeModel );
		
		backEndListModel = new BackEndListModel( modelMgr );
		//// ti
//		TiBackModel = new TiBackModel( modelMgr );
//		TiFrontModel = new TiFrontModel( TiBackModel, timeModel, visModel );
	}
	
	public void dispose()
	{
		classLoadersFrontModel.dispose();
		classesFrontModel.dispose();
		processFrontModel.dispose();
		historyFrontModel.dispose();
		threadFrontModel.dispose();
		workspaceFrontModel.dispose();
		
		swimmingLaneModel.dispose();
		circleLaneModel.dispose();
		timeModel.dispose();

		visModel.dispose();
		
//		TiBackModel.dispose();
//		TiFrontModel.dispose();
	}
}