package debug.ti.browser;

import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;

import debug.ti.view.TiView;

import debug.ti.backmodel.TiBackModel;
import debug.ti.frontmodel.*;
import debug.ti.graph.TiGraph;

import debug.gui.*;

public class TiBrowser extends JPanel implements Disposable
{
	public final static String VERSION = "0.1";
	
	protected TiBackModel backModel;
	protected TiFrontModel frontModel;
	protected TiGraph tiGraph;
	
	GraphTreeModel gtModel;
	UIDToCellModel mtcModel;
	
	protected TiView tiView;
	
//	protected CoreView backModelView;
//	protected ModelView frontModelView;
	
	public TiBrowser( TiGraph _tiGraph, TiBackModel _backModel, TiFrontModel _frontModel )
	{
		super(new GridLayout( 3, 1, 5, 5 ) );
		setName( "TiBrowser" );
		setOpaque( true );
		
		backModel = _backModel;
		frontModel = _frontModel;
		tiGraph = _tiGraph;
		
		JPanel command_panel = new JPanel();
		{
			AbstractAction time_a = new AbstractAction( "Time Bump")
			{
				public void actionPerformed( ActionEvent e )
				{
					String time_s = (String) JOptionPane.showInputDialog( null, "Time", "Time Bump", JOptionPane.QUESTION_MESSAGE, null, null, "" );
					if (time_s!=null)
					{
						frontModel.setLTime( Integer.parseInt(time_s) );
					}
				}
			};
			command_panel.add( new JButton( time_a ) );
			
			AbstractAction collapse_a = new AbstractAction( "Collapse")
			{
				public void actionPerformed( ActionEvent e )
				{
					Object[] cells = tiGraph.getSelectionCells();
				
					if (cells!=null)
					{
						frontModel.newCompound( cells );
					}
				}
			};
			command_panel.add( new JButton( collapse_a ) );
					
			AbstractAction extract_a = new AbstractAction( "Extract")
			{
				public void actionPerformed( ActionEvent e )
				{
					Object cell =  tiGraph.getSelectionCell();
					if (cell!=null && cell instanceof CompoundCell)
					{
						CompoundCell compound_c = (CompoundCell) cell;
						frontModel.extractCompound( compound_c );
					}
					
				}
			};
			command_panel.add( new JButton( extract_a ) );
		
		}
		add( command_panel );
		
		gtModel = new GraphTreeModel(frontModel);
		JTree tiTree = new JTree( gtModel );
		add( new JScrollPane( tiTree ));
		
		mtcModel = new UIDToCellModel( frontModel );
		JList mtcList = new JList( mtcModel );
		add( new JScrollPane( mtcList ) );


		
				
//			JSplitPane split = 	new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tiTree, tiView);
//			split.setDividerLocation( 120 );
//			cp.add( split );
		
//		graph
		
//		tiView = _tiView;
		
//		frontModelView = new GraphModelView( );
//		backModelView = new CoreModelView();
	}
	
	public void dispose()
	{
	}
}

