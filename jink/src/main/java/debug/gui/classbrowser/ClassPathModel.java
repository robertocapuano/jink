package debug.gui.classbrowser;

import java.io.*;
import java.util.*;

class ClassPathModel
{
	protected String classpath_s;
	protected File classpath_f;
	protected Vector classes_v; // <String>
	
	
	ClassPathModel( String _classpath )
	{
		classpath_s = _classpath;
		classpath_f = new File( classpath_s );
		classes_v = new Vector();
		
		update(classpath_f);
	}
	
	protected void update( File base )
	{
		LinkedList classes_l = new LinkedList();
		recursive( base, classes_l, classpath_f );
		classes_v.clear();
		classes_v.addAll( classes_l );
	}

	protected void recursive( File base, List pop, File entry )
	{
		if ( ! entry.isDirectory() )
		{
			if ( entry.getName().endsWith(".class") )
			{
				pop.add( pathToFQCN(base, entry) );
			}
		}
		else
		{
			File[] sub = entry.listFiles();

			for ( int i=0; i<sub.length; i++ )
			{
				recursive( base, pop, sub[i] );
			}
		}
	}
	
	protected static String pathToFQCN( File base_f, File class_f )
	{
		String base_s = base_f.getPath();
		String class_s = class_f.getPath();
		
		int base_len = base_s.length();
		int class_len = class_s.length();
		
		String rel_class_s = class_s.substring( base_len + File.pathSeparator.length(), class_len - ".class".length() );
		String fqcn_s = rel_class_s.replace( File.separatorChar, '.' );
		return fqcn_s;
	}
	
	protected Vector getClasses()
	{
		return classes_v;
	}

	protected String getClassPathString()
	{
		return classpath_s;
	}

}
	
