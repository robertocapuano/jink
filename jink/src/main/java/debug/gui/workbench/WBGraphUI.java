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
package debug.gui.workbench;

import javax.swing.*;
import java.awt.*;

import java.util.*;

import org.jgraph.plaf.basic.*;
import org.jgraph.graph.*;

class WBGraphUI extends BasicGraphUI
{
	//	public GPGraph getGPGraph() {
	//		return (GPGraph) graph;
	//	}

	//
	// Override Parent Methods
	//
	// @jdk14
	protected TransferHandler createTransferHandler()
	{
		return new WBTransferHandler();
	}

	public class WBTransferHandler extends GraphTransferHandler
	{

		protected GraphTransferable create(	Object[] cells,	Map viewAttributes,	Rectangle bounds, ConnectionSet cs,	ParentMap pm )
		{
			return new WBTransferable("hello", cells, viewAttributes, bounds, cs, pm);
		}
	}

}

class WBTransferable extends GraphTransferable
{
	protected String text;

	public WBTransferable(
		String text,
		Object[] cells,
		Map viewAttributes,
		Rectangle bounds,
		ConnectionSet cs,
		ParentMap pm)
	{
		super(cells, viewAttributes, bounds, cs, pm);
		this.text = text;
	}

	public boolean isPlainSupported()
	{
		return (text != null);
	}

	public String getPlainData()
	{
		return text;
	}

}

