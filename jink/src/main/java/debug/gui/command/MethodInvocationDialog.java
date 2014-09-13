package debug.gui.command;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;

import debug.bridge.Handler;

class MethodInvocationDialog 
{
	JDialog dialog;
	Serializable[] values;	
	MethodInvocationForm mi_form;
	
	AbstractAction invoke_a = new AbstractAction( "Invoke" )
	{
		public void actionPerformed( ActionEvent event )
		{
			values = mi_form.getValues();
			dialog.setVisible( false );
		}
	};
	
	AbstractAction cancel_a = new AbstractAction( "Cancel" )
	{
		public void actionPerformed( ActionEvent event )
		{
			values = null;
			dialog.setVisible( false );
		}
	};
	
	MethodInvocationDialog( String[] _sig_a)
	{
		dialog = new JDialog( (JFrame) null, "Method Invocation", true );
		
		mi_form = new MethodInvocationForm( _sig_a );
		dialog.setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
		JPanel cp = (JPanel) dialog.getContentPane();
		cp.add( mi_form, BorderLayout.NORTH );
		
		JPanel p = new JPanel();
		p.add( new JButton( invoke_a ) );
		p.add( new JButton( cancel_a ) );
		cp.add( p, BorderLayout.SOUTH );
		
		dialog.pack();
		
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int w = dialog.getWidth();
		int h = dialog.getHeight();
		
       	dialog.setLocation( (dim.width -w)/2, (dim.height-h)/2 );
	}
	
	void show()
	{
		dialog.show();
	}
	
	public static Serializable[] getMethodParamether( String[] _sig_a )
	{
		MethodInvocationDialog mi_dialog = new MethodInvocationDialog( _sig_a );
		mi_dialog.show();
		return mi_dialog.values;
	}
}


class MethodInvocationForm extends JPanel
{
	public final static String VERSION = "0.1";
	public final static int COLUMNS = 8;
	
	protected JTextField[] fields;
	protected String[] sig_a;
	
	MethodInvocationForm( String[] _sig_a )
	{
		super( new BorderLayout() );
		
		sig_a = _sig_a;
		
		JPanel labelPanel = new JPanel( new GridLayout( sig_a.length, 1 ) );
		JPanel fieldPanel = new JPanel( new GridLayout( sig_a.length, 1 ) );
		
		add( labelPanel, BorderLayout.WEST );
		add( fieldPanel, BorderLayout.CENTER );
		fields = new JTextField[sig_a.length];
		
		for ( int i=0; i<fields.length; ++i )
		{
			fields[i] = new JTextField();
			fields[i].setColumns( COLUMNS );
			
			JLabel label = new JLabel( "arg " + i + "<" + sig_a[i] + ">", JLabel.RIGHT );
			label.setLabelFor( fields[i] );
			
			if (i<10)	label.setDisplayedMnemonic( (""+i).charAt(0) );
			labelPanel.add( label );
			JPanel p = new JPanel( new FlowLayout( FlowLayout.LEFT ));
			p.add( fields[i] );
			fieldPanel.add(p);
		}
	}
	
	public Serializable[] getValues()
	{
		Serializable[] ser_a = new Serializable[sig_a.length];
		
		for ( int i=0; i<fields.length; ++i )
		{
			 ser_a[i] = primitiveToSerializable( sig_a[i], fields[i].getText() );
		}
		
		return ser_a;
	}
	
	static Serializable primitiveToSerializable( String sig, String value_s )
	{
		if (sig.equals( "int" ) )
		{
			return new Integer( value_s );
		}
		else
		if (sig.equals( "long" ) )
		{
			return new Long( value_s );
		}
		else
		if (sig.equals( "short" ) )
		{
			return new Short( value_s );
		}
		else
		if (sig.equals( "byte" ))
		{
			return new Byte( value_s );
		}
		else
		if (sig.equals( "char" ))
		{
			return new Character( value_s.charAt(0) );
		}
		else
		if (sig.equals( "boolean" ) )
		{
			return new Boolean( value_s );
		}
		else
		if (sig.equals("float" ) )
		{
			return new Float( value_s );
		}
		else
		if (sig.equals("double" ) )
		{
			return new Double( value_s );
		}
		else
		if (sig.equals("java.lang.String"))
		{
			return value_s;
		}
		else
		{
			return new Handler( new Integer( value_s).intValue() );
		}
	}
}