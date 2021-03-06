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
package debug.model.object;

import com.sun.jdi.*;

import java.util.*;

import debug.model.*;
import debug.model.monitor.*;
import debug.model.thread.*;

public interface ObjectState
{
	ObjectState transition() throws StateException, OperationException;

	int getTime();
	
	/**
	 ** Gestione Campi
	 */
	DetailModel getField( Field field ) throws StateException, OperationException;
	Map getFields() throws StateException, OperationException;

	 void setField( Field field, DetailModel value ) throws StateException, OperationException;
	/**
	 ** Resituisce l'ObjectReference se c'�
	 */
	Value getWrappedValue() throws StateException;
	
	// Sezione Monitor
	boolean isLocked() throws StateException, OperationException;
	MonitorModel getMonitorModel() throws StateException;

	// Sezione Threads.
	/**
	 ** Restituisce tutti i threads che passano per questo oggetto 
	 ** Ha senso solo per gli oggetti lives.
	 ** Altrimenti restituisce solo l'oggetto ThreadModel a cui appartiene lo snapshot.
	 ** **todo: da verificare questa logica.
	 */
	List getRunners() throws StateException;
	void enter( ThreadModel thread_m ) throws StateException;
	void leave( ThreadModel thread_m ) throws StateException;

	//// Stato
	boolean isLive();
	boolean isSnapshot();
	boolean isHistory();
	boolean isDead();

}

