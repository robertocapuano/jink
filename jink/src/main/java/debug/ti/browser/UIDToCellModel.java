package debug.ti.browser;

import javax.swing.*;

import java.util.Map;
import java.util.List;

import java.io.Serializable;

import debug.ti.frontmodel.*;
import org.jgraph.graph.*;
import org.jgraph.event.*;

import java.util.*;

public class UIDToCellModel extends AbstractListModel
	implements GraphModelListener, FMListener
{
	Map map;
	
	List list;
	
    public UIDToCellModel(TiFrontModel frontModel)
    {
    	map = frontModel.uidToCell;
		list = new ArrayList();
		frontModel.addGraphModelListener( this );
  		frontModel.addFMListener( this );
        reload();
    }

	private void reload()
	{
		Set set = map.entrySet();
		list.clear();
		list.addAll( set );
		
		if ( list.size()==0)
			fireContentsChanged( this, 0, 0 );
		else		
			fireContentsChanged( this, 0, list.size()-1 );
	}
		
	public void graphChanged(GraphModelEvent e)
	{
		reload();
	}
	
	public void change()
	{
		reload();
	}
	

	// Start AbstractListModel
	public Object getElementAt(int index )
	{
		Map.Entry entry = (Map.Entry) list.get(index);

		if ( entry==null)
			return "";
		else			
			return entry.getKey().toString() + "->" + entry.getValue();
	}

	public int getSize( )
	{
		return list.size();
	}

}
