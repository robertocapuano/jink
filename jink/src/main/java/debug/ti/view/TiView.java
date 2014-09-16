/* 
 * Copyright (c) 2014, Roberto Capuano <roberto@2think.it>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package debug.ti.view;

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;

import java.util.*;

import java.awt.Rectangle;
import java.awt.Color;
import java.util.Map;

import org.jgraph.JGraph;
import org.jgraph.graph.*;
import org.jgraph.event.*;

import debug.gui.*;
import debug.gui.model.*;
import debug.gui.event.*;

import debug.model.*;
import debug.runtime.*;

import tools.*;

import debug.ti.backmodel.TiBackModel;
import debug.ti.frontmodel.TiFrontModel;
import debug.ti.graph.TiGraph;

import debug.ti.browser.*;

import debug.gui.model.FrontEndModel;


public class TiView extends JPanel implements Disposable, DebugOn, GUIEventsSource
{
	public static final String VERSION = "0.3";

	protected TiGraph tiGraph;
	
	public TiView( TiGraph _tiGraph ) throws GUIException
	{
		super( new BorderLayout() );
		setName( "FinishLane "  + VERSION );
		
		tiGraph = _tiGraph;

		tiGraph.addGraphSelectionListener( graphSelectionListener );
		
		add( new JScrollPane( tiGraph ) );
	}
	
	public void dispose()
	{
		removeAll();
		tiGraph = null;
	}
	
	protected GraphSelectionListener graphSelectionListener = new GraphSelectionListener()
	{
		public void valueChanged( GraphSelectionEvent e )
		{
			Object[] cells_a = e.getCells();

			for ( int i=0; i<cells_a.length; ++i )
			{
				if ( e.isAddedCell(i) )
				{
					GraphCell graphCell = (GraphCell) cells_a[i];
					Object userObject = graphCell.getAttributes().get( GraphConstants.VALUE );
					
					if ( userObject instanceof DetailModel )
					{
						DetailModel detail_m = (DetailModel) userObject;
						if (detail_m==null)
							DC.log( LEVEL, "(detail_m==null)" );
						else
							fire( detail_m );
					}
					else
						DC.log( LEVEL, "!(userObject instanceof DetailModel.class)" );
				}
			}
		}
	};
	
	////	
	protected LinkedList listeners = new LinkedList();

	public void addGUIListener( GUIListener listener )
	{
		listeners.add( listener );
	}
	
	public void removeGUIListener( GUIListener listener )
	{
		listeners.remove( listener );
	}
	
	public void fire( Object selection )
	{
		for ( Iterator i=listeners.iterator(); i.hasNext(); )
		{
			GUIListener listener = (GUIListener) i.next();
			listener.selected( selection );
		}
	}
	////


	
	public static void main( String[] args ) throws Exception
	{
/*		RuntimeManager runtime = new RuntimeManager( "-classpath build:libs", "debug.exec.ExecManager", "" );// "debug.bridge.server.BridgeServer", "" );
		GateManager gate = new GateManager();

		DebuggeeConsole debuggeeConsole = new DebuggeeConsole();
		runtime.attach( debuggeeConsole );

		ModelManager model = new ModelManager( runtime );
		AgentsManager agents = new AgentsManager( runtime, model );
		
		runtime.resume();
		gate.listenConnection();

		JFrame frame = new JFrame( "TI " + VERSION );
		JPanel cp = (JPanel) frame.getContentPane();
		JPanel ti = new TiView( model, runtime );
		
		cp.add( ti );
		
		frame.setBounds( 50, 50, 600, 400 );
		frame.setDefaultLookAndFeelDecorated( true );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE  );
		frame.setVisible( true );

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
*/
/*		Handler class_A;
		
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
	*/
	}
}
