package debug.app;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.io.Serializable;

import debug.event.*;
import debug.exec.*;
import debug.gate.GateManager;
import debug.runtime.RuntimeManager;

import debug.bridge.*;
import debug.agentsmgr.*;

import debug.model.*;
import debug.model.ModelManager;
import debug.model.thread.ThreadModel;
import debug.model.object.ObjectModel;
import debug.model.classobject.ClassObjectModel;
import debug.model.classloader.ClassLoaderModel;
import debug.model.thread.JumpModel;

import debug.gui.model.*;
import debug.gui.classloaderbrowser.*;
import debug.gui.processbrowser.*;
import debug.gui.command.*;
import debug.gui.classbrowser.*;
import debug.gui.workspacebrowser.*;
import debug.gui.workbench.*;
import debug.gui.jumpbrowser.*;
import debug.gui.visor.*;
import debug.gui.objectbrowser.*;
import debug.gui.preferences.*;
import debug.gui.stepbrowser.*;
import debug.gui.backendbrowser.*;

import debug.gui.event.*;
import debug.gui.*;
import debug.gui.util.*;
import tools.*;

import agents.*;

import debug.ti.graph.TiGraph;
import debug.ti.view.TiView;
import debug.ti.browser.TiBrowser;
import debug.ti.backmodel.TiBackModel;
import debug.ti.frontmodel.TiFrontModel;

class AppInit extends Task implements DebugOn
{
	AppInit()
	{
		lengthOfTask = 21;
	}
	
	public void go()
	{
		final SwingWorker worker = new SwingWorker()
		{
			public Object construct()
			{
				return new App();
			}
		};
		worker.start();
	}
	
   int getLengthOfTask() {
        return lengthOfTask;
    }

    void stop() {
        current = lengthOfTask;
    }

    boolean done() {
        if (current >= lengthOfTask)
            return true;
        else
            return false;
    }

    String getMessage() {
        return status;
    }
    
	class App implements DebugOn
	{
		protected RuntimeManager runtime;
		protected GateManager gate;
		
		protected ModelManager modelMgr;

		protected DebuggeeConsole debuggeeConsole;
		protected CommandToolBar commandToolBar;

		protected AgentsManager agents;
		protected FrontEndModel frontEndModel;
		protected ClassLoaderBrowser classLoaderBrowser;
		protected ProcessBrowser processBrowser;
		protected ClassBrowser classBrowser;
		protected WorkspaceBrowser workspaceBrowser;
		protected StepBrowser stepBrowser;
		protected BackEndBrowser backEndBrowser;
		
		protected Workbench workbench;
		protected Visor visor;
		protected JumpBrowser jumpBrowser;
		protected ObjectBrowser objectBrowser;
		protected PreferencesPanes preferencesPanes;
		
		App()
		{
			try
			{
				current = 1;
				
				status = "runtime init";
				runtime = new RuntimeManager( "-classpath build:libs", "debug.exec.ExecManager", "" );// "debug.bridge.server.BridgeServer", "" );
				current += 1;

				status = "gate init";
				gate = new GateManager();
				current += 1;
				
				status = "init DebuggeeConsole";
				debuggeeConsole = new DebuggeeConsole();
				current += 1;

				status = "attach DebuggeeConsole";
				runtime.attach( debuggeeConsole );
				current += 1;
		
				DC.log( "canGetCurrentContendedMonitor: " + runtime.canGetCurrentContendedMonitor() );
				DC.log( "canGetMonitorInfo: " + runtime.canGetMonitorInfo() );
				DC.log( "canGetOwnedMonitorInfo: " + runtime.canGetOwnedMonitorInfo() );
		
				status = "init ModelManager";
				modelMgr = new ModelManager( runtime );
				current += 1;

				status = "init AgentsManager";
				agents = new AgentsManager( runtime, modelMgr );
				current += 1;

				frontEndModel = new FrontEndModel( modelMgr, runtime );

				status = "init WorkspaceBrowser";
				workspaceBrowser = new WorkspaceBrowser( frontEndModel );
				current += 1;

				status = "init ClassLoaderBrowser";
				classLoaderBrowser = new ClassLoaderBrowser( frontEndModel );
				current += 1;
				
				status = "init ProcessBrowser";
				processBrowser = new ProcessBrowser( frontEndModel );
				current += 1;
				
				status = "init ClassBrowser";
				classBrowser = new ClassBrowser( );
				current += 1;

				status = "init JumpBrowser";
				jumpBrowser = new JumpBrowser( );
				current += 1;
				
				status = "init ObjectBrowser";
				objectBrowser = new ObjectBrowser( );
				current += 1;

				status = "init StepBrowser";
				stepBrowser = new StepBrowser( modelMgr );
				current +=1;

				status = "init BackEndBrowser";
				backEndBrowser = new BackEndBrowser( frontEndModel );
				current +=1;

/*
				status = "init MethodBrowser";
				methodBrowser = new MethodBrowser(  );
				current +=1;
*/				

				status = "init Workbench";
				SwimmingLaneView swimmingLaneView = new SwimmingLaneView( frontEndModel );
				CircleLaneView circleLaneView = new CircleLaneView( frontEndModel );

				// **todo: Queste due righe di codice vanno spostate.
				final TiBackModel tiBackModel = new TiBackModel( modelMgr );
				final TiFrontModel tiFrontModel = new TiFrontModel( modelMgr, tiBackModel, frontEndModel.getTimeModel(), frontEndModel.getVisModel() );
				final TiGraph tiGraph = new TiGraph( tiFrontModel );
				TiView tiView = new TiView( tiGraph );
				TiBrowser tiBrowser = new TiBrowser( tiGraph, tiBackModel, tiFrontModel );

				workbench = new Workbench( new JComponent[] { swimmingLaneView, circleLaneView, tiView } );
				current += 1;
				
				status = "init Visor";
				visor = new Visor(new JPanel[] { classBrowser, stepBrowser, tiBrowser, backEndBrowser } );
				visor.add( workspaceBrowser, ClassObjectModel.class );
				visor.add( classLoaderBrowser, ClassLoaderModel.class );
				visor.add( processBrowser, ThreadModel.class );
				visor.add( jumpBrowser, JumpModel.class );
				visor.add( objectBrowser, ObjectModel.class );
//				visor.add( tiBrowser,  null );
//				visor.add( methodBrowser, InvocationSelection.class );
				visor.show();
				current +=1;

				workbench.addGUIListener( visor ); // per selezionare il tab
				workbench.addGUIListener( workspaceBrowser );
				workbench.addGUIListener( processBrowser );
				workbench.addGUIListener( classLoaderBrowser );
				workbench.addGUIListener( jumpBrowser );
				workbench.addGUIListener( objectBrowser );

				status = "init CommandToolBar";
				commandToolBar = new CommandToolBar( gate );
				current += 1;

				NewClassLoaderCommand newClassLoaderCommand = new NewClassLoaderCommand( gate );
				commandToolBar.add( newClassLoaderCommand );
				newClassLoaderCommand.addGUIListener( classBrowser );
				
				NewClassCommand newClassCommand = new NewClassCommand( gate );
				commandToolBar.add( newClassCommand );		
				classBrowser.addGUIListener( newClassCommand );

				NewObjectCommand newObjectCommand = new NewObjectCommand( gate );
				commandToolBar.add( newObjectCommand );		
				workspaceBrowser.addWorkspaceListener( newObjectCommand );
				workspaceBrowser.addMethodsListener( newObjectCommand );
				
				NewThreadCommand newThreadCommand = new NewThreadCommand( gate );
				commandToolBar.add( newThreadCommand );	
				workspaceBrowser.addWorkspaceListener( newThreadCommand );
				
				InvokeMethodCommand invokeMethodCommand = new InvokeMethodCommand( gate );
				commandToolBar.add( invokeMethodCommand );
//				workspaceBrowser.addWorkspaceListener( invokeMethodCommand );
				workspaceBrowser.addMethodsListener( invokeMethodCommand );
//				objectBrowser.addObjectListener( invokeMethodCommand );
				objectBrowser.addMethodsListener( invokeMethodCommand );
				
				RemoveObjectCommand removeObjectCommand = new RemoveObjectCommand( gate );
				commandToolBar.add( removeObjectCommand );	
				workspaceBrowser.addWorkspaceListener( removeObjectCommand );

				commandToolBar.addSeparator();
				ResumeCommand resumeCommand = new ResumeCommand( runtime );
				commandToolBar.add( resumeCommand );
				
				StepSwitchCommand stepSwitchCommand = new StepSwitchCommand( modelMgr );
				commandToolBar.add( stepSwitchCommand );
				workbench.addGUIListener( stepSwitchCommand );
				
				objectBrowser.addMethodsListener( invokeMethodCommand );
				
				commandToolBar.addSeparator();
				
				AboutCommand aboutCommand = new AboutCommand();
				commandToolBar.add( aboutCommand );
				
				QuitCommand quitCommand = new QuitCommand();
				commandToolBar.add( quitCommand );
//				classBrowser.addGUIListener( newClassCommand );
//				workspaceInspector.addGUIListener( newClassCommand );
				
				GameOver gameOver = new GameOver( modelMgr );
/*
				// Cortocircuitiamo il Visor
				objectBrowser.addMethodsListener( methodBrowser );
*/
				//// preference
				JPanel monitorPane = new MonitorPane( frontEndModel.getVisModel() );
				JPanel referencePane = new ReferencePane( frontEndModel.getVisModel() );
				JPanel timePane = new TimePane( frontEndModel.getTimeModel() );
				JPanel runPane = new RunPane( runtime );
				JPanel deepPane = new DeepCopyPane( modelMgr );
				
				JPanel[] panes = new JPanel[] { monitorPane, referencePane,timePane, runPane, deepPane };
				preferencesPanes = new PreferencesPanes( panes );
				preferencesPanes.show();

				status = "runtime resume";
				runtime.resume();
				current += 1;
				
				status = "gate connection";
				gate.listenConnection();
				current = getLengthOfTask();
			}
			catch( Exception e )
			{
				DC.log(  e );
				current = lengthOfTask;
			}
		}
	}


	public static void main( String[] args ) throws Exception
	{
		RuntimeManager runtime = new RuntimeManager( "-classpath build:libs", "debug.exec.ExecManager", "" );// "debug.bridge.server.BridgeServer", "" );
		GateManager gate = new GateManager();
//		gate.attach();

		DebuggeeConsole debuggeeConsole = new DebuggeeConsole();
		runtime.attach( debuggeeConsole );

		ModelManager model = new ModelManager( runtime );
		AgentsManager agents = new AgentsManager( runtime, model );
		
		DC.log( "canGetCurrentContendedMonitor: " + runtime.canGetCurrentContendedMonitor() );
		DC.log( "canGetMonitorInfo: " + runtime.canGetMonitorInfo() );
		DC.log( "canGetOwnedMonitorInfo: " + runtime.canGetOwnedMonitorInfo() );
		
		runtime.resume();
		gate.listenConnection();

		{
			NewClassLoader ncl1 = new NewClassLoader( "workspace/build" );
			Reply r1 = gate.cross( ncl1 );		
			DC.log( LEVEL, r1 );
		}

		Handler class_h;

		{
			String class_s = "Hello";
			NewClass nc = new NewClass( class_s );
			Reply r1 = gate.cross( nc );
			DC.log( LEVEL, r1 );
			class_h = (Handler) r1.getEnveloped();
		}
		
		Handler self_h;
		
		{
			String[] sig_a = new String[0];
			Serializable[] args_a = new Serializable[0];
			Invocation inv = new Invocation( class_h, sig_a, args_a );
			NewObject no = new NewObject( inv );
			Reply r2 = gate.cross( no );		
			DC.log( LEVEL, r2 );
			
			self_h = (Handler) r2.getEnveloped();
		}



		{
			String method_s = "f";
			String[] sig_a = new String[0];
			Serializable[] args_a = new Serializable[0];
			Invocation inv = new Invocation( self_h, method_s, sig_a, args_a );
			InvokeMethod mi = new InvokeMethod( inv );
			Reply r = gate.cross( mi );
			DC.log( LEVEL, r );
		}


		{
			String method_s = "h";
			String[] sig_a = new String[] { "int" };
			Serializable[] args_a = new Serializable[] { new Integer( 12 ) };
			Invocation inv = new Invocation( self_h, method_s, sig_a, args_a );
			InvokeMethod mi = new InvokeMethod( inv );
			Reply r = gate.cross( mi );
			DC.log( LEVEL, r );
		}


		{
			String method_s = "i";
			String[] sig_a = new String[] { "java.lang.String", "int" };
			Serializable[] args_a = new Serializable[] { "Aibo", new Integer( 12 ) };
			Invocation inv = new Invocation( self_h, method_s, sig_a, args_a );
			InvokeMethod mi = new InvokeMethod( inv );
			Reply r = gate.cross( mi );
			DC.log( LEVEL, r );
		}
		
		Handler class_A;
		
		{
			String class_s = "A";
			NewClass nc = new NewClass( class_s );
			Reply r1 = gate.cross( nc );
			DC.log( LEVEL, r1 );
			class_A = (Handler) r1.getEnveloped();
		}
		
		Handler A_h;
		
		{
			String[] sig_a = new String[0];
			Serializable[] args_a = new Serializable[0];
			Invocation inv = new Invocation( class_A, sig_a, args_a );
			NewObject no = new NewObject( inv );
			Reply r2 = gate.cross( no );		
			DC.log( LEVEL, r2 );
			A_h = (Handler) r2.getEnveloped();
		}

		{
			NewThread nt = new NewThread( self_h );
			Reply r1 = gate.cross( nt );
			DC.log( LEVEL, r1 );
		}					

		{
			String method_s = "j";
			String[] sig_a = new String[] { "A" };
			Serializable[] args_a = new Serializable[] { A_h };
			Invocation inv = new Invocation( self_h, method_s, sig_a, args_a );
			InvokeMethod mi = new InvokeMethod( inv );
			Reply r = gate.cross( mi );
			DC.log( LEVEL, r );
		}



		{
			RemoveObject ro = new RemoveObject( self_h );
			Reply r = gate.cross( ro );
			DC.log( LEVEL, r );
		}
		
		System.out.println( BackEndModel.getShared() );		
		
	}

}

/*	public static void main2( String[] args ) throws Exception
	{
		RuntimeManager runtime = new RuntimeManager( "-classpath build:libs", "debug.exec.ExecManager", "" );// "debug.bridge.server.BridgeServer", "" );
		GateManager gate = new GateManager();
		gate.attach();

		DebuggeeConsole debuggeeConsole = new DebuggeeConsole();
		runtime.attach( debuggeeConsole );

		ModelManager model = new ModelManager( runtime );
		AgentsManager agents = new AgentsManager( runtime, model );
		
		DC.log( "canGetCurrentContendedMonitor: " + runtime.canGetCurrentContendedMonitor() );
		DC.log( "canGetMonitorInfo: " + runtime.canGetMonitorInfo() );
		DC.log( "canGetOwnedMonitorInfo: " + runtime.canGetOwnedMonitorInfo() );
		
		runtime.resume();
		gate.waitConnection();
		
		ThreadModel threadModel = null;

		BackEndModel backEndModel = BackEndModel.getShared();
		
		{
			List live_threads = backEndModel.getLiveThreads();
			
			for ( Iterator i=live_threads.iterator(); i.hasNext(); )
			{ 
				ThreadModel thread_m = (ThreadModel) i.next();
				if (thread_m.getName().equals("main") )
				{
					threadModel = thread_m;
					break;
				}
			}
		}
		
		if (threadModel==null)	
			return;
			
//		ThreadFrontModel thread_fm = new ThreadFrontModel( model, threadModel );
		
		{
			NewClassLoader ncl1 = new NewClassLoader( "workspace/build" );
			Reply r1 = gate.cross( ncl1 );		
			DC.log( LEVEL, r1 );
		}
				
		Handler class_h;

		{
			String class_s = "Hello";
			NewClass nc = new NewClass( class_s );
			Reply r1 = gate.cross( nc );
			DC.log( LEVEL, r1 );
			class_h = (Handler) r1.getEnveloped();
		}
		
		Handler self_h;
		
		{
			String[] sig_a = new String[0];
			Serializable[] args_a = new Serializable[0];
			Invocation inv = new Invocation( class_h, sig_a, args_a );
			NewObject no = new NewObject( inv );
			Reply r2 = gate.cross( no );		
			DC.log( LEVEL, r2 );
			self_h = (Handler) r2.getEnveloped();
		}

		{
			NewThread nt = new NewThread( self_h );
			Reply r1 = gate.cross( nt );
			DC.log( LEVEL, r1 );
		}					


		{
			String method_s = "f";
			String[] sig_a = new String[0];
			Serializable[] args_a = new Serializable[0];
			Invocation inv = new Invocation( self_h, method_s, sig_a, args_a );
			InvokeMethod mi = new InvokeMethod( inv );
			Reply r = gate.cross( mi );
			DC.log( LEVEL, r );
		}

		{
			String method_s = "h";
			String[] sig_a = new String[] { "int" };
			Serializable[] args_a = new Serializable[] { new Integer( 12 ) };
			Invocation inv = new Invocation( self_h, method_s, sig_a, args_a );
			InvokeMethod mi = new InvokeMethod( inv );
			Reply r = gate.cross( mi );
			DC.log( LEVEL, r );
		}

		{
			String method_s = "i";
			String[] sig_a = new String[] { "java.lang.String", "int" };
			Serializable[] args_a = new Serializable[] { "Aibo", new Integer( 12 ) };
			Invocation inv = new Invocation( self_h, method_s, sig_a, args_a );
			InvokeMethod mi = new InvokeMethod( inv );
			Reply r = gate.cross( mi );
			DC.log( LEVEL, r );
		}
		
		Handler class_A;
		
		{
			String class_s = "A";
			NewClass nc = new NewClass( class_s );
			Reply r1 = gate.cross( nc );
			DC.log( LEVEL, r1 );
			class_A = (Handler) r1.getEnveloped();
		}
		
		Handler A_h;
		
		{
			String[] sig_a = new String[0];
			Serializable[] args_a = new Serializable[0];
			Invocation inv = new Invocation( class_A, sig_a, args_a );
			NewObject no = new NewObject( inv );
			Reply r2 = gate.cross( no );		
			DC.log( LEVEL, r2 );
			A_h = (Handler) r2.getEnveloped();
		}

		{
			String method_s = "j";
			String[] sig_a = new String[] { "A" };
			Serializable[] args_a = new Serializable[] { A_h };
			Invocation inv = new Invocation( self_h, method_s, sig_a, args_a );
			InvokeMethod mi = new InvokeMethod( inv );
			Reply r = gate.cross( mi );
			DC.log( LEVEL, r );
		}


		{
			RemoveObject ro = new RemoveObject( self_h );
			Reply r = gate.cross( ro );
			DC.log( LEVEL, r );
		}

		System.out.println( BackEndModel.getShared() );		
		



	}
*/	
