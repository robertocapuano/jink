package tools;

public class Stringer implements DebugOn
{
	public static void main( String[] args )
	{
		System.out.println( extractClassName( args[0] ) );
		System.out.println( extractClassState( args[0] ) );
	}
		
	public static String extractClassName( String fqcn )
	{
		int last_dot = fqcn.lastIndexOf( '.', fqcn.length() );
		return fqcn.substring( last_dot+1, fqcn.length() - "Model".length() );
	}
	
	public static String extractClassState( String fqcn )
	{
		int last_dot = fqcn.lastIndexOf( '.', fqcn.length() );
		return fqcn.substring( last_dot+1, fqcn.length() - "State".length() );
	}	

}