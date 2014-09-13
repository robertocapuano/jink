package debug.model;

/**
 ** Richiesta di una operazione - tipo setValue() su uno stato Snapshot che è a sola lettura - incompatibile con lo stato dell'oggetto.
 ** Operazione non supportata
 ** 
 */
public class OperationException extends ModelException
{
	public OperationException( String msg )
	{
		super( msg );
	}

	public OperationException( Throwable cause )
	{
		super( cause );
	}

	public OperationException( String msg, Throwable cause )
	{
		super( msg, cause );
	}
	
	public OperationException()
	{
		super();
	}
}
