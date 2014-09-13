/*
 * @(#)GraphTreeModel.java	1.0.5 06/01/03
 *
 * Copyright (C) 2002 Gaudenz Alder
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package debug.ti.browser;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.GraphModel;

public class GraphTreeModel
	extends DefaultTreeModel
	implements GraphModelListener {
	public GraphTreeModel(GraphModel model) {
		super(new GraphModelTreeNode(model));
		model.addGraphModelListener( this );
	}

	public void graphChanged(GraphModelEvent e) {
		reload();
	}

	public static class GraphModelTreeNode implements TreeNode {

		protected GraphModel model;

		public GraphModelTreeNode(GraphModel model) {
			this.model = model;
		}

		public Enumeration children() {
			Vector v = new Vector();
			for (int i = 0; i < model.getRootCount(); i++)
				v.add(model.getRootAt(i));
			return v.elements();
		}

		public boolean getAllowsChildren() {
			return true;
		}

		public TreeNode getChildAt(int childIndex) {
			return (TreeNode) model.getRootAt(childIndex);
		}

		public int getChildCount() {
			return model.getRootCount();
		}

		public int getIndex(TreeNode node) {
			return model.getIndexOfRoot(node);
		}

		public TreeNode getParent() {
			return null;
		}

		public boolean isLeaf() {
			return false;
		}

		public String toString() {
			return model.toString();
		}

	}

}
