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
package debug.runtime;

import com.sun.jdi.*;
import com.sun.jdi.event.*;

import java.util.*;

import debug.event.*;

import javax.swing.SwingUtilities;

import tools.*;

class EventDispatcher implements Runnable, DebugOff
{
	protected final RuntimeManager runtime;
	protected final EventQueue queue;
	
    protected final RuntimeJDIController firstListener = new RuntimeJDIController();

	protected final Thread thread;
	
	EventDispatcher( RuntimeManager _runtime, EventQueue _queue )
	{
		runtime = _runtime;
		queue = _queue;

		thread = new Thread( this );
	}

	public void start()
	{
		thread.start();
	}
	
	public void interrupt()
	{
		thread.interrupt();
	}
	
	
	void suspend()
	{
	}
	
	synchronized void resume()
	{
		this.notifyAll();
	}
	
	/**
	 ** RunLoop
	 */
	public  void run()
	{
		try
		{
			AbstractEventSet aes;
		   
			do
			{
				synchronized( this )
				{
					while ( runtime.isSuspended() )
						this.wait();
				}		
					
				EventSet jdiEventSet = queue.remove();
				aes = AbstractEventSet.toSpecificEventSet( jdiEventSet );
				runtime.setSuspended( aes.suspendedAll() );
				
				dispatchEventSet(aes);
	//			finalizeEventSet( aes );
			}
			while ( !(aes instanceof VMDisconnectEventSet) );
		}
		catch ( InterruptedException ie )
		{
			System.out.println( "" + ie );
		}
	}

    private void dispatchEventSet( final AbstractEventSet aes )
    {
		SwingUtilities.invokeLater( new Runnable()
		{
			public void run()
        	{
				boolean listenerReqPause = false;
		
				boolean suspended_all = aes.suspendedAll();
				aes.notify(firstListener);
				
				for (Iterator it = runtime.getIteratorJDIListeners(); it.hasNext(); )
				{
					JDIListener jl = (JDIListener)it.next();
					if (aes.notify(jl))	listenerReqPause = true;
				}
				
				if (suspended_all && (!listenerReqPause || !runtime.reqPause()) )
				{
					try
					{
						runtime.resume( );
					}
					catch (RuntimeException se )
					{
						System.err.println( "" +se );
					}
				}
				if (aes instanceof ThreadDeathEventSet)
				{
					DC.log( aes );
			//                    ThreadReference t = ((ThreadDeathEventSet)es).getThread();
			//                    session.runtime.removeThreadInfo(t);
				}
            } // end run()
        }); // end new Runnable
    }
	
    //### Gross foul hackery!
  
	private void finalizeEventSet(AbstractEventSet aes) throws SessionException
	{
        if ( runtime.isSuspended() ) 
        {
            runtime.setSuspended( false );

            try
            {
                runtime.resume();
            }
            catch( RuntimeException re ) { DC.log( re ); }
            catch( VMDisconnectedException ee ) { }
            
        }
        
/*        if (aes instanceof ThreadDeathEventSet)
        {
            ThreadReference t = ((ThreadDeathEventSet)aes).getThread();
            session.runtime.removeThreadInfo(t);
        }
*/
    }


    

	
}
