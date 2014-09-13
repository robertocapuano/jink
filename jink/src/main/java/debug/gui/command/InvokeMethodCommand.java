package debug.gui.command;

import com.sun.jdi.*;

import javax.swing.*;
import java.io.Serializable;
import java.awt.event.*;
import java.util.*;

import agents.*;

import debug.gate.GateManager;
import debug.exec.Reply;
import debug.bridge.*;

import debug.gui.model.*;

import debug.model.classobject.*;
import debug.model.object.*;
import debug.model.*;
import debug.gui.event.*;

import tools.*;

public class InvokeMethodCommand extends AbstractAction implements DebugOff, GUIListener
{
	protected GateManager gateMgr;
	protected Handler object_h;
	protected Method method;
	
	public InvokeMethodCommand( GateManager _gateMgr )
	{
		super( " Invoke Method " );
		gateMgr = _gateMgr;
		object_h = null;
		method = null;
		setEnabled( false );
	}
	
	//// inter-gui communication :)
	public void selected( Object selection )
	{
/*		if (selection==null)
		{
			return;
		}
		else
		{
		
			setEnabled( true );

			if ((selection instanceof ObjectModel) && !(selection instanceof ClassObjectModel) )
			{
				ObjectModel object_m = (ObjectModel) selection;
				object_h = (Handler) object_m.getHandler();
				DC.log( LEVEL, object_h );
			}
			else
			if (selection instanceof Method )
			{
				method = (Method) selection;
			}
			else
*/
		if ( selection!=null && selection instanceof MethodModel)
		{
			MethodModel inv = (MethodModel) selection;
			ObjectModel object_m = inv.getObject();
			if (!object_m.hasHandler() )
				DC.log( LEVEL, "(!object_m.hasHandler() )");
			else
			if (!inv.getMethod().isConstructor())
			{
				object_h = (Handler) object_m.getHandler();
				method = inv.getMethod();
				setEnabled( true );
				return;
			}
		}
		
		setEnabled( false );					
		method = null;
		object_h = null;
	}
	
	////
	public void actionPerformed( ActionEvent e)
	{

		if (object_h==null)
		{
			JOptionPane.showMessageDialog(null,"no object selected");
		}
		else
		if (method == null )
		{
			JOptionPane.showMessageDialog(null,"no method selected");
		}
		else
		{
			final String[] sig =  MethodModel.getSignature( method );
			final Serializable[] args = MethodInvocationDialog.getMethodParamether( sig );

			if (args==null) // utente ha fatto cancel
				return;
			
			Runnable r = new Runnable()
			{
				public void run()
				{
					try
					{
						Invocation invocation = new Invocation( object_h, method.name(), sig, args, !method.isStatic() );
						InvokeMethod method_invocation = new InvokeMethod( invocation );
						Reply reply = gateMgr.cross( method_invocation );
						DC.log( LEVEL, reply );
					}
					catch( BridgeException be )
					{
						DC.log(  be );
					}
				}
			};
					
			new Thread( r ).start();
		}
	}
		
}



