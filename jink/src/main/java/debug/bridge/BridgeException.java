package debug.bridge;

/**
 ** Indica che con tutta probabilitˆ l'oggetto deve cambiare stato.
 ** Operazione supportata ma stato invalido.
 */
public class BridgeException extends Exception
{
	public BridgeException( Throwable cause )
	{
		super( cause );
	}
	
	public BridgeException()
	{
		super();
	}
	public BridgeException( String str )
	{
		super( str );
	}
}

