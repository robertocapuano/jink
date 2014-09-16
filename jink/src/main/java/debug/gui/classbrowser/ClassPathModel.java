/* 
 * Copyright (c) 2014, Roberto Capuano <roberto@2think.it>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
	
