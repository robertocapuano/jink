package debug.ti.graph;

import javax.swing.*;
import java.awt.*;

import java.util.*;

import org.jgraph.plaf.basic.*;
import org.jgraph.graph.*;

import debug.ti.frontmodel.TiLayoutManager;

class TiGraphUI extends BasicGraphUI
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
		return new TiTransferHandler();
	}

	public class TiTransferHandler extends GraphTransferHandler
	{

		protected GraphTransferable create(	Object[] cells,	Map viewAttributes,	Rectangle bounds, ConnectionSet cs,	ParentMap pm )
		{
			return new TiTransferable("hello", cells, viewAttributes, bounds, cs, pm);
		}
	}

}

class TiTransferable extends GraphTransferable
{
	protected String text;

	public TiTransferable(
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

