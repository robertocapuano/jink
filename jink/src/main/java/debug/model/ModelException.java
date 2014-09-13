package debug.model;

/**
 ** Richiesta di una operazione - tipo setValue() su uno stato Snapshot che è a sola lettura - incompatibile con lo stato dell'oggetto.
 ** Operazione non supportata
 ** 
 */
public class ModelException extends Exception
{
	public ModelException( String msg )
	{
		super( msg );
	}

	public ModelException( Throwable cause )
	{
		super( cause );
	}

	public ModelException()
	{
		super();
	}
	
	public ModelException( String msg, Throwable cause )
	{
		super( msg, cause );
	}
	
}
