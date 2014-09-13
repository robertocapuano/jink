package debug.model;

/**
 ** Richiesta di una operazione - tipo setValue() su uno stato Snapshot che è a sola lettura - incompatibile con lo stato dell'oggetto.
 ** Operazione non supportata
 ** 
 */
public class StateException extends ModelException
{
	public StateException( Throwable cause )
	{
		super( cause );
	}

	public StateException()
	{
		super();
	}
}
