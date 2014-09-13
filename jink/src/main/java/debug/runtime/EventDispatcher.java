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
