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

