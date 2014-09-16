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

