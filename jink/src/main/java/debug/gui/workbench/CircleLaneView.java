package debug.gui.workbench;

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
import debug.model.*;
import debug.gui.event.*;

import tools.*;

public class CircleLaneView extends JPanel implements Disposable, DebugOn, GUIEventsSource
{
	public static final String VERSION = "0.4";
	
	protected CircleLaneModel model;
	protected JGraph graph;
	protected CircleLaneLayout layoutMgr;
	
	public CircleLaneView( FrontEndModel frontEndModel ) throws GUIException
	{
		super( new BorderLayout() );
		setName( "CircleLane "  + VERSION );
		
		GraphApparence apparenceMgr = new CircleLaneApparence();
		model = frontEndModel.getCircleLaneModel();
		model.attach( apparenceMgr );
		graph = new JGraph( model );
		layoutMgr = new CircleLaneLayout( graph );
		
		graph.setModel( model );					
		graph.setSelectNewCells(false);
		graph.setEditable(false);
		graph.setCloneable( false );
		graph.setDropEnabled( false );
		graph.setConnectable( false );
		graph.setDisconnectable( false );

		graph.addGraphSelectionListener( graphSelectionListener );
		add( new JScrollPane( graph ) );
	}
	
	public void dispose()
	{
		model.dispose();
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
					GraphCell cell = (GraphCell) cells_a[i];
					DetailModel detail_m = (DetailModel) cell.getAttributes().get( GraphConstants.VALUE );
					if (detail_m==null)
						DC.log( LEVEL, "(detail_m==null)" );
					else
						fire( detail_m );
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

	
}
