package debug.bridge;

import java.io.*;

import com.mindprod.base64.Base64; 

import tools.*;

public class Bridge implements DebugOff
{
	public static final String VERSION = "0.2";

	public final static int JINK_PORT = 49152;
	
	public static String encodeObject( Object obj ) throws BridgeException
	{
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream( baos );
			oos.writeObject( obj );
			byte[] coded_ba = baos.toByteArray();
			oos.reset();
			Base64 base64 = new Base64();
			base64.setLineLength(10240);
			String coded_s = base64.encode( coded_ba );
			return coded_s;
		}
		catch( IOException ioe )
		{
			throw new BridgeException( ioe );
		}
		catch( ArrayIndexOutOfBoundsException aioobe )
		{
			throw new BridgeException( aioobe );
		}
		
	}

	public static Object decodeObject( String coded ) throws BridgeException
	{
		try
		{
			Base64 base64 = new Base64();
			base64.setLineLength(10240);
			byte[] decoded_ba = base64.decode( coded );
			
			ByteArrayInputStream bais = new ByteArrayInputStream( decoded_ba );
			ObjectInputStream ois = new ObjectInputStream( bais );
			Object decoded_o = ois.readObject();
			ois.close();
			bais.close();
			return decoded_o;
		}
		catch( IOException ioe )
		{
			DC.log( ioe );
			throw new BridgeException( ioe );
		}
		catch( ClassNotFoundException cnfe )
		{
			DC.log( cnfe );
			throw new BridgeException( cnfe );
		}
		catch( ArrayIndexOutOfBoundsException aioobe )
		{
			DC.log( aioobe );
			throw new BridgeException( aioobe );
		}
		
	}
	
	public static void main( String[] args ) throws BridgeException
	{
	
		class A implements Serializable
		{
			int a;
			private int b;
			
			A( int _a, int _b )
			{ a = _a; b = _b; }
			
			public String toString()
			{
				return a + "," + b;
			
			}
		};

		String str = "Hello";
		A a1 = new A( 1, 2 );
		A a2 = new A( 3, 4 );
		
		String coded;
		Object decoded;
				
		coded = encodeObject( a1 );
		System.out.println( ">" + coded );
		decoded = decodeObject( coded );
		System.out.println( "<" + decoded  + ",class:" + decoded.getClass() );


		coded = encodeObject( a2 );
		System.out.println( ">" + coded );
		decoded = decodeObject( coded );
		System.out.println( "<" + decoded  + ",class:" + decoded.getClass() );
	
			
	}
	
	
}


/*
/*
	attach( RuntimeManager runtime )
	{
	}
	
	attachConsole()
	
	
			Console con = new Console();
			System.setIn( con.getInputStream() );
			System.setOut( con.getOutputStream() );
			System.setErr( con.getOutputStream() );

	private final InputStream consoleIN;
	private final PrintStream consoleOUT;
		
	private final InputStream execIN = System.in;
	private final OutputStream execOUT = System.out;
	
	private final InputStream gateIN = System.in;
	private final OutputStream gateOUT = System.out;
	
	public InputStream consoleIN()
	{
		return consoleIN;
	}
	
	public PrintStream consoleOUT()
	{
		return consoleOUT;
	}

	public PrintStream consoleERR()
	{
		return consoleOUT;
	}

	public InputStream execIN()
	{
		return execIN;
	}

	public OutputStream execOUT()
	{
		return execOUT;
	}

	public InputStream gateIN()
	{
		return gateIN;
	}
	
	public OutputStream gateOUT()
	{
		return gateOUT;
	}

	public Bridge() throws IOException
	{
		consoleIN = new FileInputStream( new File( "/dev/null") );
		consoleOUT = new PrintStream( new FileOutputStream( new File( "/dev/null" ) ) );
	}
	
	
	public attach( Session session )
	{
	}
	
	Cimitero del codice
	
	private static String objectToXML( Object obj )
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder( baos );
		encoder.writeObject( obj );
		encoder.close();
		String xml = baos.toString().replace(  '\n', ' ' ) + "\n";
		return xml;
	}

	private static Object xmlToObject( String xml )
	{

		XMLDecoder decoder = new XMLDecoder( new ByteArrayInputStream( xml.getBytes() ) );
//		XMLDecoder decoder = new XMLDecoder( new StringBufferInputStream( xml ) );
		Object o = decoder.readObject();
		decoder.close();
		return o;
	}

*/