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
package debug.event;

import com.sun.jdi.*;
import com.sun.jdi.event.*;

public class ExceptionEventSet extends LocatableEventSet {
    
    ExceptionEventSet(EventSet jdiEventSet) {
        super(jdiEventSet);
    }

    /**
     * Gets the thrown exception object. The exception object is 
     * an instance of java.lang.Throwable or a subclass in the 
     * target VM.
     *
     * @return an {@link ObjectReference} which mirrors the thrown object in
     * the target VM. 
     */
    public ObjectReference getException() {
        return ((ExceptionEvent)oneEvent).exception();
    }
    
    /**
     * Gets the location where the exception will be caught. An exception
     * is considered to be caught if, at the point of the throw, the
     * current location is dynamically enclosed in a try statement that
     * handles the exception. (See the JVM specification for details).
     * If there is such a try statement, the catch location is the 
     * first code index of the appropriate catch clause.
     * <p>
     * If there are native methods in the call stack at the time of the
     * exception, there are important restrictions to note about the 
     * returned catch location. In such cases,
     * it is not possible to predict whether an exception will be handled
     * by some native method on the call stack.
     * Thus, it is possible that exceptions considered uncaught
     * here will, in fact, be handled by a native method and not cause 
     * termination of the target VM. Also, it cannot be assumed that the 
     * catch location returned here will ever be reached by the throwing 
     * thread. If there is 
     * a native frame between the current location and the catch location,
     * the exception might be handled and cleared in that native method 
     * instead.
     * 
     * @return the {@link Location} where the exception will be caught or null if
     * the exception is uncaught.
     */
    public Location getCatchLocation() {
        return ((ExceptionEvent)oneEvent).catchLocation();
    }

    public boolean notify(JDIListener listener) {
        return listener.exception(this);
    }
}

