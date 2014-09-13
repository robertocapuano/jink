package debug.ti;

import com.sun.jdi.*;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.*;


import java.util.*;

import org.jgraph.graph.*;

import debug.mock.*;

import debug.model.object.*;
import debug.model.thread.*;

import debug.bridge.*;

import debug.ti.backmodel.TiBackModel;
import debug.ti.frontmodel.*;
import debug.ti.graph.TiGraph;
import debug.ti.view.*;
import debug.ti.browser.*;

import tools.*;

class TiTest implements DebugOn
{
	public static void main( String[] args ) throws Exception
	{
/*
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
		// manca frontEndModel
//		TiBackModel TiBackModel = new TiBackModel( modelMgr );
//		TiFrontModel TiFrontModel = new TiFrontModel( TiBackModel, frontEndModel.getTimeModel(), frontEndModel.getVisModel() );
		TiBackModel TiBackModel = new TiBackModel( modelMgr );
		TiFrontModel TiFrontModel = new TiFrontModel( TiBackModel, null, null );
*/
		final TiBackModel tiBackModel = new TiBackModel( null );
		final TiFrontModel tiFrontModel = new TiFrontModel( null, tiBackModel, null, null );
		final TiGraph tiGraph = new TiGraph( tiFrontModel );
		
		TiView tiView = new TiView( tiGraph );
		TiBrowser tiBrowser = new TiBrowser( tiGraph, tiBackModel, tiFrontModel );
	
		JToolBar toolbar;
		{
			toolbar = new JToolBar();

			AbstractAction time_a = new AbstractAction( "Time Bump")
			{
				public void actionPerformed( ActionEvent e )
				{
					String time_s = (String) JOptionPane.showInputDialog( null, "Time", "Time Bump", JOptionPane.QUESTION_MESSAGE, null, null, "" );
					if (time_s!=null)
					{
						tiFrontModel.setLTime( Integer.parseInt(time_s) );
					}
				}
			};
			toolbar.add( time_a );
			
			AbstractAction collapse_a = new AbstractAction( "Collapse")
			{
				public void actionPerformed( ActionEvent e )
				{
					Object[] cells = tiGraph.getSelectionCells();
				
					if (cells!=null)
					{
						tiFrontModel.newCompound( cells );
					}
				}
			};
			toolbar.add( collapse_a );
					
			AbstractAction extract_a = new AbstractAction( "Extract")
			{
				public void actionPerformed( ActionEvent e )
				{
					Object cell =  tiGraph.getSelectionCell();
					if (cell!=null && cell instanceof CompoundCell)
					{
						CompoundCell compound_c = (CompoundCell) cell;
						tiFrontModel.extractCompound( compound_c );
					}
					
				}
			};
			toolbar.add( extract_a );

		}

		JFrame ti_frame;
		
		{
			ti_frame = new JFrame( "TI " + TiView.VERSION );
			ti_frame.setDefaultLookAndFeelDecorated( true );
			ti_frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE  );
			ti_frame.setBounds( 50, 50, 600, 400 );
			JPanel cp = (JPanel) ti_frame.getContentPane();

			cp.add( toolbar, BorderLayout.NORTH );
			cp.add( tiView );
			ti_frame.setVisible( true );
		}		
		
		{
			JFrame frame = new JFrame( "Browser " + TiBrowser.VERSION );
			frame.setDefaultLookAndFeelDecorated( true );
			frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE  );
			frame.setBounds(  700, 50, 200, 200 );
			JPanel cp = (JPanel) frame.getContentPane();
			cp.add( tiBrowser );
			frame.setVisible( true );
		}		

		ReferenceType mock_type;
		{
			Field[] fields = new Field[] { new FieldMock("a"), new FieldMock( "b") };
			List fields_l = Arrays.asList( fields );
			List methods_l = new LinkedList();
			mock_type = new ReferenceTypeMock( "MockType", fields_l, methods_l );		
		}
		
		ObjectReference object_r;
		{
			Value[] values = new Value[] { new IntegerValueMock(34), new IntegerValueMock(56) };
			List values_l = Arrays.asList( values );
		
			object_r = new ObjectReferenceMock( mock_type, values_l );
		}

		ObjectModel object_m;
		{
			object_m = new ObjectModel( object_r );

			object_m.setLabel( "MyFirstLabel" );
			Handler handler = new Handler( 1024 );
			
			object_m.setHandler( handler );
		}
		
		ObjectCell object_c;	
		{	
			DC.log( LEVEL, ""+ object_m.longDescription() );

			object_c = tiFrontModel.newObjectModel( object_m );
		}

		ReferenceType thread_type;
		{
			Method f = 	new MethodMock( "f", IntegerTypeMock.shared, new LinkedList() );
			List methods_l = new LinkedList();
			methods_l.add( f );
			thread_type = new ReferenceTypeMock( "MockType",  new LinkedList(), methods_l );		
		}

		ThreadReference thread_r;
		{
			thread_r = new ThreadReferenceMock( "myThread", thread_type);
			
			Map locals = new HashMap();
			LocalVariable lv = new LocalVariableMock( "a", IntegerTypeMock.shared);
			locals.put( lv, IntegerTypeMock.shared );
	
	//		StackFrame frame = new StackFrameMock( null, null, locals );
			StackFrame frame = new StackFrameMock( thread_r, null, locals );
	
			((ThreadReferenceMock)thread_r).pushFrame( frame );
		}

		ObjectModel object_m_2;
		{
			object_m_2 = new ObjectModel( object_r );

			object_m_2.setLabel( "MySecondLabel" );
			Handler handler = new Handler( 1025 );
			
			object_m_2.setHandler( handler );
		}
		
		ObjectCell object_c_2;
		{	
			object_c_2 = tiFrontModel.newObjectModel( object_m_2 );
		}

		ThreadModel thread_m;
		{
			thread_m = new ThreadModel( thread_r );
		}

		ThreadCell thread_c;
		{
			thread_c = tiFrontModel.newThreadModel( thread_m );
		}
		
		JumpModel prev_m;
		{
			Method method = new MethodMock( "f", IntegerTypeMock.shared, new LinkedList() );
			prev_m = new JumpModel( thread_m, object_m, method, false );
		}
		
		JumpModel jump_m;
		{
			Method method = new MethodMock( "f", IntegerTypeMock.shared, new LinkedList() );
			jump_m = new JumpModel( thread_m, object_m_2, method, false );
		}

		{
			tiFrontModel.newJumpModel( prev_m.getObject(), prev_m, jump_m.getObject() );
		}

		JumpModel next_m;
		{
			Method method = new MethodMock( "f", IntegerTypeMock.shared, new LinkedList() );
			next_m = new JumpModel( thread_m, object_m, method, true );
		}

		{
			tiFrontModel.newJumpModel( jump_m.getObject(), jump_m, next_m.getObject() );
		}
		
//		DefaultGraphCell compound_c = tiFrontModel.newCompound( new DetailCell[] { cell1, cell2}  );
//		TiFrontModel.remove( new GraphCell[] { cell1, cell2} );
//		TiFrontModel.add( object_c );
	}
}