package debug.app;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.*;

import debug.runtime.RuntimeManager;
import debug.gate.GateManager;
import debug.agentsmgr.AgentsManager;
import debug.bridge.Bridge;
import debug.exec.ExecManager;
import debug.app.Main;
import debug.model.ModelManager;
import debug.mock.MockVersion;
import debug.gui.workbench.Workbench;
import debug.gui.visor.Visor;
import debug.gui.workbench.SwimmingLaneView;
import debug.gui.classbrowser.ClassBrowser;

import debug.ti.view.TiView;
import debug.gui.*;
import tools.*;

import debug.label.*;

public class AboutBox extends JFrame implements DebugOff
{
	public static AboutBox shared = null;

	public static AboutBox getShared()
	{
		if ( shared == null )
			shared = new AboutBox();
		return shared;
	}
	
	public AboutBox()
	{
		super( "About Box" );
		setResizable( false );
		setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE  );
//		
		JPanel panel = new JPanel( new GridBagLayout() );
		
		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 1;
		c.weighty = 1;
		c.gridwidth = 1;
		c.gridheight = 1;
		
//		JPanel panel2 = new JPanel(); 
		c.gridx = 0; c.gridy =0; c.gridwidth =2;
		panel.add( new JLabel( Main.RELEASE ), c );

		c.gridx = 0; c.gridy =1; c.gridwidth =1;
		panel.add( new JLabel( "RuntimeManager " ), c);
		c.gridx = 1; 
		panel.add( new JLabel( RuntimeManager.VERSION ), c  );

		c.gridx = 0; c.gridy =2; 
		panel.add( new JLabel( "GateManager" , SwingConstants.CENTER ),c  );
		c.gridx = 1;
		panel.add( new JLabel( GateManager.VERSION ), c  );

		c.gridx = 0; c.gridy =3;
		panel.add( new JLabel( "AgentsManager" ), c );
		c.gridx = 1;
		panel.add( new JLabel( AgentsManager.VERSION),c );

		c.gridx = 0; c.gridy =4;
		panel.add( new JLabel( "Bridge"  ),c  );
		c.gridx = 1;
		panel.add( new JLabel(Bridge.VERSION),c  );

		c.gridx = 0; c.gridy =5;
		panel.add( new JLabel( "ExecManager"), c  );
		c.gridx = 1;
		panel.add( new JLabel( ExecManager.VERSION ), c  );

		c.gridx = 0; c.gridy =6;
		panel.add( new JLabel( "ModelManager" ),c  );
		c.gridx = 1;
		panel.add( new JLabel( ModelManager.VERSION),c  );

		c.gridx = 0; c.gridy =7;
		panel.add( new JLabel( "Agents" ), c );
		c.gridx = 1;
		panel.add( new JLabel( agents.VERSION.VERSION ),c );

		c.gridx = 0; c.gridy =8;
		panel.add( new JLabel( "TI" ), c );
		c.gridx = 1;
		panel.add( new JLabel( TiView.VERSION ), c );

		c.gridx = 0; c.gridy =9;
		panel.add( new JLabel( "JDIMock" ), c );
		c.gridx = 1;
		panel.add( new JLabel( MockVersion.VERSION ), c );

		c.gridx = 0; c.gridy =10;
		panel.add( new JLabel( "LabelManager" ), c );
		c.gridx = 1;
		panel.add( new JLabel( LabelManager.VERSION ), c );

		c.gridx = 0; c.gridy =11;
		panel.add( new JLabel( "Visor" ), c );
		c.gridx = 1;
		panel.add( new JLabel( Visor.VERSION ), c );

		c.gridx = 0; c.gridy =12;
		panel.add( new JLabel( "SwimmingLane" ), c );
		c.gridx = 1;
		panel.add( new JLabel( SwimmingLaneView.VERSION ), c );

		c.gridx = 0; c.gridy =13;
		panel.add( new JLabel( "Workbench"), c );
		c.gridx = 1; 
		panel.add( new JLabel( Workbench.VERSION), c );

		c.gridx = 0; c.gridy =14;
		panel.add( new JLabel( "ClassBrowser"), c );
		c.gridx = 1; 
		panel.add( new JLabel( ClassBrowser.VERSION), c );

		JPanel content_pane = (JPanel) getContentPane();
		content_pane.add(panel);
		
		JButton btn =  new JButton( new  AbstractAction( "Close" ) { public void actionPerformed( ActionEvent e ) { setVisible(false); } } );
		JPanel pnl = new JPanel();
		pnl.add( btn );
		content_pane.add( pnl, BorderLayout.SOUTH );

		setSize( new Dimension( 320, 320 ) );
		//		pack();

		{
			Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
			int w = getWidth();
			int h = getHeight();
			setLocation( (dim.width -w)/2, (dim.height-h)/2 );
		}

	}
	
	public static void main( String[] args )
	{
		AboutBox about = new AboutBox();
		about.show();
	}
	
}
/*public static void main( String[] args )
	{
		// runtime e gate sono due oggetti differenti
		// che comunicano con la vm debuggee a due livelli distinti.
		// ad un livello più basso vi è runtime che ragione in termini di eventi jdi (ObjectReference, ...) questi identificati da ref (long id)
		// ad un livello più alto gate che ragiona in termini di Agents (NewClassLoader,...) identificati da hashCode() (int hashCode())
		RuntimeManager runtime = new RuntimeManager( "-classpath build:libs", "debug.exec.ExecManager", "" );// "debug.bridge.server.BridgeServer", "" );
		GateManager gate = new GateManager();

		runtime.attach( gate );
*/
//lo mettiamo in un comando:		runtime.resume();

	
