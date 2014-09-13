package debug.app;

import debug.gui.*;

import debug.gui.util.InitProgress;

public class Main
{
	public static final String VERSION = "0.8";
	public final static String RELEASE = "Jink " + VERSION + " (Jiro)";

	public static void main( String[] args )
	{
		InitProgress initProgress = new InitProgress( new AppInit() );
	}			
}
	