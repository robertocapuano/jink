package debug.gui;

/**
 ** Richiesta di una operazione - tipo setValue() su uno stato Snapshot che è a sola lettura - incompatibile con lo stato dell'oggetto.
 ** Operazione non supportata
 ** 
 */
public class GUIException extends Exception
{
	public GUIException( String msg )
	{
		super( msg );
	}

	public GUIException( Throwable cause )
	{
		super( cause );
	}

	public GUIException()
	{
		super();
	}
}
