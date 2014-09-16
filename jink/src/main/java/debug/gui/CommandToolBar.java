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
package debug.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.*;

import debug.runtime.RuntimeManager;
import debug.gate.GateManager;

import tools.*;
import debug.gui.command.*;

public class CommandToolBar implements DebugOff
{
	protected JWindow window;
	protected JToolBar toolbar;
	
	public CommandToolBar(GateManager gateMgr )
	{
		window = new JWindow( );

		window.setBounds( this.getBounds() );

		toolbar = new JToolBar();
		toolbar.setFloatable( false );

		JPanel content_pane = (JPanel) window.getContentPane();
		content_pane.add( toolbar, BorderLayout.CENTER );

		window.setVisible(true);
	}
	
	public void add( AbstractAction component )
	{
		toolbar.add( component );
	}
	
	public void addSeparator()
	{
		toolbar.addSeparator( new Dimension( 32, 32 ) );
	}
	
	protected Rectangle getBounds()
	{
		int x = (int) ( 0 * GUILayout.step_x ) ;
		int y = (int) ( 0 * GUILayout.step_y );
		int width = 6*GUILayout.step_x;
		int height = 60;
		
		Rectangle r = new Rectangle( x, y, width, height );
		return r;
	}	
	
}
/*public static void main( String[] args )
	{
		// runtime e gate sono due oggetti differenti
		// che comunicano con la vm debuggee a due livelli distinti.
		// ad un livello pi� basso vi � runtime che ragione in termini di eventi jdi (ObjectReference, ...) questi identificati da ref (long id)
		// ad un livello pi� alto gate che ragiona in termini di Agents (NewClassLoader,...) identificati da hashCode() (int hashCode())
		RuntimeManager runtime = new RuntimeManager( "-classpath build:libs", "debug.exec.ExecManager", "" );// "debug.bridge.server.BridgeServer", "" );
		GateManager gate = new GateManager();

		runtime.attach( gate );
*/
//lo mettiamo in un comando:		runtime.resume();

	
