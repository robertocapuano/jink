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
package debug.gui.processbrowser;

import com.sun.jdi.Method;

import javax.swing.*;
import java.awt.*;

import debug.model.thread.JumpModel;
import debug.model.object.ObjectModel;
import debug.model.object.ObjectState;
import debug.model.classobject.ClassObjectModel;

import debug.gui.abstractlistinspector.AbstractCellRenderer;

class ThreadCellRenderer extends AbstractCellRenderer
{
	protected String getLabel( Object value )
	{
		JumpModel jump_m = (JumpModel) value;
		
		ObjectModel object_m = jump_m.getObject();
		ClassObjectModel class_m = (ClassObjectModel) object_m.getClassModel();
		Method method = jump_m.getMethod();
		String object_s = object_m.getStateName();
		String op = jump_m.isExitStep()  ? "return" : "invoke";
		int time = jump_m.getTime();
		
		String label = time + " " + op + " " + class_m.getName() + " " + method + " " + object_s;
		return label;
	}
	
}