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
import com.sun.jdi.request.*;

import java.util.*;

public abstract class AbstractEventSet extends EventObject implements EventSet
{
    
    private final EventSet jdiEventSet;
    final Event oneEvent;

    /**
     */
    AbstractEventSet(EventSet jdiEventSet)
    {
        super(jdiEventSet.virtualMachine());
        this.jdiEventSet = jdiEventSet;
        this.oneEvent = eventIterator().nextEvent();
    }

    public static AbstractEventSet toSpecificEventSet(EventSet jdiEventSet)
    {
        Event evt = jdiEventSet.eventIterator().nextEvent();
        if (evt instanceof LocatableEvent)
        {
            if (evt instanceof ExceptionEvent)
            {
                return new ExceptionEventSet(jdiEventSet);
            }
            else if (evt instanceof WatchpointEvent)
            {
                if (evt instanceof AccessWatchpointEvent)
                {
                    return new AccessWatchpointEventSet(jdiEventSet);
                }
                else
                {
                    return new ModificationWatchpointEventSet(jdiEventSet);
                }
            }
            else
            {
                return new LocationTriggerEventSet(jdiEventSet);
            }
        } else if (evt instanceof ClassPrepareEvent) {
            return new ClassPrepareEventSet(jdiEventSet);
        } else if (evt instanceof ClassUnloadEvent) {
            return new ClassUnloadEventSet(jdiEventSet);
        } else if (evt instanceof ThreadDeathEvent) {
            return new ThreadDeathEventSet(jdiEventSet);
        } else if (evt instanceof ThreadStartEvent) {
            return new ThreadStartEventSet(jdiEventSet);
        } else if (evt instanceof VMDeathEvent) {
            return new VMDeathEventSet(jdiEventSet);
        } else if (evt instanceof VMDisconnectEvent) {
            return new VMDisconnectEventSet(jdiEventSet);
        } else if (evt instanceof VMStartEvent) {
            return new VMStartEventSet(jdiEventSet);
        } else {
            throw new IllegalArgumentException("Unknown event " + evt);
        }
    }

    public abstract boolean notify(JDIListener listener);

    // Implement Mirror

    public VirtualMachine virtualMachine() {
        return jdiEventSet.virtualMachine();
    }

    public VirtualMachine getVirtualMachine() {
        return jdiEventSet.virtualMachine();
    }

    // Implement EventSet

    /**
     * Returns the policy used to suspend threads in the target VM
     * for this event set. This policy is selected from the suspend
     * policies for each event's request. The one that suspends the 
     * most threads is chosen when the event occurs in the target VM
     * and that policy is returned here. See 
     * com.sun.jdi.request.EventRequest for the possible policy values.
     * 
     * @return the integer suspendPolicy
     */
    public int getSuspendPolicy() {
        return jdiEventSet.suspendPolicy();
    }

    public void resume() {
        jdiEventSet.resume();
    }

    public int suspendPolicy() {
        return jdiEventSet.suspendPolicy();
    }

    public boolean suspendedAll() {
        return jdiEventSet.suspendPolicy() == EventRequest.SUSPEND_ALL;
    }

    public boolean suspendedEventThread() {
        return jdiEventSet.suspendPolicy() == EventRequest.SUSPEND_EVENT_THREAD;
    }

    public boolean suspendedNone() {
        return jdiEventSet.suspendPolicy() == EventRequest.SUSPEND_NONE;
    }

    /**
     * Return an iterator specific to {@link Event} objects.
     */
    public EventIterator eventIterator() {
        return jdiEventSet.eventIterator();
    }


    // Implement java.util.Set (by pass through)

    /**
     * Returns the number of elements in this set (its cardinality).  If this
     * set contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this set (its cardinality).
     */
    public int size() {
        return jdiEventSet.size();
    }

    /**
     * Returns <tt>true</tt> if this set contains no elements.
     *
     * @return <tt>true</tt> if this set contains no elements.
     */
    public boolean isEmpty() {
        return jdiEventSet.isEmpty();
    }

    /**
     * Returns <tt>true</tt> if this set contains the specified element.  More
     * formally, returns <tt>true</tt> if and only if this set contains an
     * element <code>e</code> such that <code>(o==null ? e==null :
     * o.equals(e))</code>.
     *
     * @return <tt>true</tt> if this set contains the specified element.
     */
    public boolean contains(Object o) {
        return jdiEventSet.contains(o);
    }

    /**
     * Returns an iterator over the elements in this set.  The elements are
     * returned in no particular order (unless this set is an instance of some
     * class that provides a guarantee).
     *
     * @return an iterator over the elements in this set.
     */
    public Iterator iterator() {
        return jdiEventSet.iterator();
    }

    /**
     * Returns an array containing all of the elements in this set.
     * Obeys the general contract of the <tt>Collection.toArray</tt> method.
     *
     * @return an array containing all of the elements in this set.
     */
    public Object[] toArray() {
        return jdiEventSet.toArray();
    }

    /**
     * Returns an array containing all of the elements in this set whose
     * runtime type is that of the specified array.  Obeys the general
     * contract of the <tt>Collection.toArray(Object[])</tt> method.
     *
     * @param a the array into which the elements of this set are to
     *		be stored, if it is big enough {
        return jdiEventSet.XXX();
    } otherwise, a new array of the
     * 		same runtime type is allocated for this purpose.
     * @return an array containing the elements of this set.
     * @throws    ArrayStoreException the runtime type of a is not a supertype
     * of the runtime type of every element in this set.
     */
    public Object[] toArray(Object a[]) {
        return jdiEventSet.toArray(a);
    }

    // Bulk Operations

    /**
     * Returns <tt>true</tt> if this set contains all of the elements of the
     * specified collection.  If the specified collection is also a set, this
     * method returns <tt>true</tt> if it is a <i>subset</i> of this set.
     *
     * @param c collection to be checked for containment in this set.
     * @return <tt>true</tt> if this set contains all of the elements of the
     * 	       specified collection.
     */
    public boolean containsAll(Collection c) {
        return jdiEventSet.containsAll(c);
    }


    // Make the rest of Set unmodifiable

    public boolean add(Object o){
        throw new UnsupportedOperationException();
    }
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }
    public boolean addAll(Collection coll) {
        throw new UnsupportedOperationException();
    }
    public boolean removeAll(Collection coll) {
        throw new UnsupportedOperationException();
    }
    public boolean retainAll(Collection coll) {
        throw new UnsupportedOperationException();
    }
    public void clear() {
        throw new UnsupportedOperationException();
    }
}

