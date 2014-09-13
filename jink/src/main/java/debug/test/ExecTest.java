package debug.test;

import java.util.*;

import java.io.*;
import java.net.*;

import debug.bridge.*;

import tools.*;

import debug.agents.*;

import debug.exec.*;

public class ExecTest
{

	public static void main( String[] args ) throws Exception
	{
		ServerSocket server_sk = new ServerSocket( Bridge.JINK_PORT );
		Socket connection_sk = server_sk.accept();
		InputStream in = connection_sk.getInputStream();
		OutputStream out = connection_sk.getOutputStream();
		
		BufferedReader reader = new BufferedReader( new InputStreamReader( in ),1 );
		BufferedWriter writer = new PrintWriter( new OutputStreamWriter( out ) );

		// mettere del codice per cortocicuitare i socket con lo stdin e stadout
		// lo trovi nel package tools.

	}

}