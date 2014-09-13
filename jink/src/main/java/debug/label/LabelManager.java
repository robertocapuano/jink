package debug.label;


import java.util.*;

/**
 ** Da uid ad una etichetta
 ** Ma anche da Compound ad una etichetta
 */
public class LabelManager
{
	public final static String VERSION = "0.1";
	
	private static LabelManager shared = new LabelManager();
	public static LabelManager getShared() { return shared; }
	
	////	
	
	private LabelManager()
	{}
	
	
	protected List repository = new LinkedList();
	
	public void putLabel( Label label )
	{
		repository.remove( label );
		repository.add( label );
		fire( label );
	}
	
	public List getLabels( )
	{
		return new LinkedList( repository );
	}
	
	//// Listeners
	protected List listeners = new LinkedList();
	
	public void addLabelListener( LabelListener labelListener )
	{
		listeners.add( labelListener );
	}
	
	public void removeLabelListener( LabelListener labelListener )
	{
		listeners.remove( labelListener );
	}
	
	protected void fire( Label label )
	{
		for ( Iterator i=listeners.iterator(); i.hasNext(); )
		{
			LabelListener listener = (LabelListener) i.next();
			listener.newLabel( label );	
		}
	}
	
		
}