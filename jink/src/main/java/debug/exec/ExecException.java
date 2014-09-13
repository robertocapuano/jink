package debug.exec;

/**
 ** Indica che con tutta probabilitˆ l'oggetto deve cambiare stato.
 ** Operazione supportata ma stato invalido.
 */
public class ExecException extends Exception
{
	public ExecException( Throwable cause )
	{
		super( cause );
	}
	
	public ExecException()
	{
		super();
	}
	public ExecException( String str )
	{
		super( str );
	}
}

