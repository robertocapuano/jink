package debug.gui.workbench;

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;

import java.awt.Dimension;

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

public class SwimmingLaneView extends JPanel implements Disposable, DebugOn, GUIEventsSource
{
	public static final String VERSION = "0.7";
	
	protected SwimmingLaneModel model;
	protected WBGraph graph;

	private final JScrollPane scroller;
    private final JScrollBar hscroller, vscroller;
	
	public SwimmingLaneView( FrontEndModel frontEndModel ) throws GUIException
	{
		super( new BorderLayout() );
		setName( "SwimmingLane "  + VERSION );
		model = frontEndModel.getSwimmingLaneModel();
		
		graph = new WBGraph( model );
		graph.setSelectNewCells(false);
		graph.setEditable(false);
		graph.setDragEnabled( false );
		graph.setCloneable( false );
		graph.setDropEnabled( false );
		graph.setConnectable( false );
		graph.setDisconnectable( false );

		graph.addGraphSelectionListener( graphSelectionListener );
		model.addGraphModelListener( graphModelListener );

		scroller = new JScrollPane( graph );
		vscroller = scroller.getVerticalScrollBar();
		hscroller = scroller.getHorizontalScrollBar();
		add( scroller );
	}
	
	public void dispose()
	{
		model.dispose();
	}
	
	SwimmingLaneModel getModel()
	{
		return model;
	}
	
	protected GraphModelListener graphModelListener = new GraphModelListener()
	{
		public void graphChanged( final GraphModelEvent e )
		{
			GraphModelEvent.GraphModelChange change = e.getChange();
			Object[] cells = (Object[]) change.getInserted();
			
			if ( cells!=null && cells.length>0)
			{
				for ( int i=0; i<cells.length; ++i )
				{
					DefaultGraphCell cell = (DefaultGraphCell) cells[i];
					if ( !(cell instanceof DefaultEdge) && !(cell instanceof DefaultPort) ) 
					{
						Map attr = cell.getAttributes();
						final Rectangle rect = GraphConstants.getBounds( attr );
						final Dimension size = SwimmingLaneView.this.getSize();
						
						SwingUtilities.invokeLater( new Runnable()
						{
							public void run()
							{
								hscroller.setValue(Math.max( rect.x - size.width + rect.width, 0 ) );
								vscroller.setValue(Math.max( rect.y - size.height + rect.height, 0 ) );
	/*							Rectangle r = SwingUtilities.convertRectangle( graph, rect, SwimmingLaneView.this );
								scroller.scrollRectToVisible( r );
								vscroller.set
*/							}
						});			
					}
				}
			}
				
//					vscroller.setValue(vscroller.getMaximum());
	//				hscroller.setValue(hscroller.getMinimum());
		}
	
	};
	
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
