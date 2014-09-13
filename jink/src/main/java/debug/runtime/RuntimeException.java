package debug.runtime;

/**
 ** Indica che con tutta probabilitˆ l'oggetto deve cambiare stato.
 ** Operazione supportata ma stato invalido.
 */
public class RuntimeException extends Exception
{
	public RuntimeException( Throwable cause )
	{
		super( cause );
	}
	
	public RuntimeException()
	{
		super();
	}
}

