/*
 * Copyright (c)2011, DIADEM Team
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the DIADEM team nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DIADEM Team BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
/**
 * Package containing consumer classes for OXPath output ({@code OXPathExtractionNode} objects).  The package
 * consists of the {@code abstract} class {@code OXPathOutputHandler} and its children implementations. 
 */
package uk.ac.ox.comlab.diadem.oxpath.output;

import java.io.ObjectInputStream;
import org.slf4j.Logger;

/**
 * Abstract class for different implementations of OXPath output ({@code OXPathOutputHandler} instances) consumers
 * @author AndrewJSel
 *
 */
public abstract class OXPathOutputHandler extends Thread {

	/**
	 * Input Stream receiving OXPath input
	 */
	private ObjectInputStream in;
	
	/**
	 * Logger object for the OXPath system
	 */
	protected final Logger logger;
	
	protected String host;
	protected int port;
	
	/**
	 * Constructor that takes an output stream and a logger to incorporate into an OXPath process
	 * @param iHost the host for the stream receiving {@code OXPathExtractionNode} instances
	 * @param iPort the port for the stream receiving {@code OXPathExtractionNode} instances
	 * @param iLogger the logging environment associated with OXPath
	 */
	public OXPathOutputHandler(String iHost, int iPort, Logger iLogger) {
		logger = iLogger;
		host = iHost;
		port = iPort;
	}	
	
	/**
	 * Returns the {@code ObjectInputStream} object associated with OXPath output.  Not a defensive copy, but the 
	 * actual object so the handler is exposed to all input
	 * @return the {@code ObjectInputStream} object associated with OXPath output
	 */
	public ObjectInputStream getInput() {
		return this.in;
	}
	
}
