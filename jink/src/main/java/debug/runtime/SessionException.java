package debug.runtime;

/**
 ** Indica che con tutta probabilitˆ l'oggetto deve cambiare stato.
 ** Operazione supportata ma stato invalido.
 */
public class SessionException extends Exception
{
	public SessionException( Throwable cause )
	{
		super( cause );
	}
	
	public SessionException()
	{
		super();
	}
}

