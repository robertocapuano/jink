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
package debug.gui.preferences;

import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

import java.util.*;

import debug.model.event.*;
import debug.model.*;

import debug.gui.event.*;
import debug.gui.Disposable;
import debug.gui.model.*;

import debug.gui.*;

import tools.*;

public class TimePane extends JPanel implements DebugOff, Disposable
{
	protected TimeModel timeModel;
	JSlider ltime_slider;
	JCheckBox freeze_btn;

	AbstractAction nowAction = new AbstractAction( "Now" )
	{
		public void actionPerformed( ActionEvent e )
		{
			timeModel.now();
		}
	};
	
	ChangeListener changeListener = new ChangeListener()
	{
		public void stateChanged( ChangeEvent e)
		{
			int ltime = ((JSlider)e.getSource()).getValue();
			timeModel.setLTime( ltime );
		}
	};
	
	TimeListener timeListener = new TimeListener()
	{
		public void leap( TimeEvent time_e )
		{
			DC.log( LEVEL, "" + time_e );
		}
		
		public void now( TimeEvent time_e )
		{
			DC.log( LEVEL, "" + time_e );
			ltime_slider.setValue( time_e.gtime );
		}
		
		public void jump( TimeEvent time_e )
		{
			DC.log( LEVEL, "" + time_e );

			ltime_slider.setMaximum( time_e.gtime );
			int minor = (int) Math.max( Math.round( time_e.gtime/10 )+1, 1 );
			int major = (int) Math.max( Math.round( time_e.gtime/3 )+1, 3 );
	
					
			ltime_slider.setMinorTickSpacing( minor );
			ltime_slider.setMajorTickSpacing( major );
			Hashtable labels = ltime_slider.createStandardLabels(minor);
			Integer gtime = new Integer( time_e.gtime );
//			labels.put( gtime, new JLabel( gtime.toString() ) );
			ltime_slider.setLabelTable( labels );
			
			if (!time_e.freeze)
			{
				ltime_slider.setValue( time_e.gtime );
			}
		}
		
		public void freezed( TimeEvent time_e )
		{
			DC.log( LEVEL, "" + time_e );
			freeze_btn.setSelected( time_e.freeze );
		}
		
	};
	
	ActionListener freeze_al = new ActionListener()
	{
		public void actionPerformed( ActionEvent e )
		{
			timeModel.setFreezed( freeze_btn.isSelected() );
		}
	};
	
	public TimePane( TimeModel _timeModel )
	{
		super();

		setName( "TimePane" );
		setBorder( new TitledBorder( "TimePane" ) );
		timeModel = _timeModel;
		timeModel.addTimeListener( timeListener );
		int ltime = timeModel.getLTime();
		int gtime = timeModel.gtime();
		boolean freeze = timeModel.isFreezed();
		freeze_btn = new JCheckBox( "Freeze", freeze );
		add( freeze_btn );
		freeze_btn.addActionListener( freeze_al );
		
		ltime_slider = new JSlider( 0, gtime, ltime );
		ltime_slider.addChangeListener( changeListener );
		ltime_slider.setPaintTicks( true );
		ltime_slider.setPaintLabels( true );
		ltime_slider.setMinorTickSpacing( 1 );
		ltime_slider.setMajorTickSpacing( 10 );
		ltime_slider.setSnapToTicks( true );
		ltime_slider.setLabelTable( ltime_slider.createStandardLabels(1)  );
		add( ltime_slider );
		
		JButton now_btn = new JButton( nowAction );
		add( now_btn );
	}
	
	public void dispose()
	{
		timeModel = null;
	}
}

