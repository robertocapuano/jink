package debug.gui.abstractlistinspector;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import debug.model.ModelManager;

import debug.gui.*;
import debug.gui.event.*;

import tools.*;

public abstract class AbstractListInspector implements GUIEventsSource, Disposable, DebugOff
{
	public static final String VERSION = "0.3";

	//// begin listeners
	protected final List listeners = new LinkedList();
	
	public void addGUIListener( GUIListener listener )
	{
		listeners.add( listener );
	}
	
	public void removeGUIListener( GUIListener listener )
	{
		listeners.remove( listener );
	}
	
	protected void fire( Object selection )
	{
		for ( Iterator i = listeners.iterator(); i.hasNext(); )
		{
			GUIListener listener = (GUIListener) i.next();
			
			listener.selected( selection );
		}
	}
	////
	
	protected final JFrame frame;
	protected final JList view;
	protected final AbstractListModel model;
	
	protected ListSelectionListener guiSourceEvents = new ListSelectionListener()
	{
		public void valueChanged( ListSelectionEvent lse )
		{
			int first_index = lse.getFirstIndex();
			if (lse.getValueIsAdjusting() || first_index==-1)
				return;
			int last_index = lse.getLastIndex();

			JList source = (JList) lse.getSource();
		
			for ( int i=lse.getFirstIndex(); i<=last_index; i++ )
			{
				if (source.isSelectedIndex(i) )
				{
					Object selection = model.getElementAt( i );
					fire( selection );
					return;
				}
			}
			
			// cancella la selezione
			fire( null );
		}
	};
	
	protected ListDataListener viewScrollController = new ListDataListener()
	{
		public void contentsChanged( ListDataEvent e )
		{
		}
		
		public void intervalAdded( ListDataEvent e )
		{
			update();
		}
		
		public void intervalRemoved( ListDataEvent e )
		{
			update();
		}
	};
	
	
	protected final JScrollBar hscroller, vscroller;

	protected void update()
	{
		view.invalidate();
		
		SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
			{
				vscroller.setValue(vscroller.getMaximum());
				hscroller.setValue(hscroller.getMinimum());
			}
		});
	
	}

	
	public AbstractListInspector( String title, AbstractListModel _model, ListCellRenderer renderer ) throws GUIException
	{
    	frame = new JFrame(title);
		frame.setDefaultLookAndFeelDecorated( true );
		frame.setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE  );

		frame.setBounds( this.getBounds() );
	
		model = _model;
		
		view = new JList( model );
		view.setCellRenderer( renderer );
		view.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
		view.addListSelectionListener( guiSourceEvents );
		model.addListDataListener( viewScrollController );

		JScrollPane scrollView = new JScrollPane( view );
		vscroller = scrollView.getVerticalScrollBar();
		hscroller = scrollView.getHorizontalScrollBar();

		JPanel content_pane = (JPanel) frame.getContentPane();
		content_pane.add( scrollView );
		
		frame.setVisible( true );
	}

	public void dispose()
	{
		view.removeListSelectionListener( guiSourceEvents );
		model.removeListDataListener( viewScrollController );
		((Disposable) model).dispose();
	}
	
	
	abstract protected Rectangle getBounds();	
		
}
