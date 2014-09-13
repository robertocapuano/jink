package debug.gui;

import java.awt.*;

public abstract class GUILayout
{
	public final static int step_x, step_y;
	
	static {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension d = toolkit.getScreenSize();
		
		int istep_x = d.width / 10;
		int gap_x = istep_x/10;
		int istep_y = d.height / 10;
		int gap_y = istep_y/10;
		
		step_x = istep_x + gap_x;
		step_y = istep_y + gap_y;
	}
}
