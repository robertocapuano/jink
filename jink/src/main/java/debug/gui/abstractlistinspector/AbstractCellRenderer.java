package debug.gui.abstractlistinspector;

import javax.swing.*;
import java.awt.*;


public abstract class AbstractCellRenderer extends JLabel implements ListCellRenderer
{
	protected static Font font = new Font ( "SanSerif", Font.PLAIN, 14 );
	protected static final Color HIGHLIGHT_COLOR = new Color( 0,0,128);
	
	protected AbstractCellRenderer()
	{
		setOpaque(true);
		setIconTextGap(12);
		setFont( font );
	}
	
	protected abstract String getLabel( Object value );
	
	public Component getListCellRendererComponent( JList jlist, Object value, int index, boolean isSelected, boolean cellHasFocus )
	{
		String label = getLabel( value );
		
		setText( label );
		
		if (isSelected)
		{
			setBackground(HIGHLIGHT_COLOR );
			setForeground(Color.white);
		}
		else
		{
			setBackground(Color.white);
			setForeground(Color.black);
		}

		return this;

	}
	
}