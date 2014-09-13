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

