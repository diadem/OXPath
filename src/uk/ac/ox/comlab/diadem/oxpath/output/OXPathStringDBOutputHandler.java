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
import java.net.Socket;

import org.slf4j.Logger;

import diadem.common.test.StringDatabase;

import uk.ac.ox.comlab.diadem.oxpath.model.OXPathExtractionNode;

/**
 * Class for directing OXPath output into a String database for testing within the DIADEM testing framework
 * @author AndrewJSel
 *
 */
public class OXPathStringDBOutputHandler extends OXPathOutputHandler {

	/**
	 * Constructor for the handler.  This handler requires a host, port, and logger.
	 * @param iHost hostname
	 * @param iPort port on {@code iHost}
	 * @param iLogger logger environment
	 * @param sDatabase String database to handle output
	 */
	public OXPathStringDBOutputHandler(String iHost, int iPort, Logger iLogger, StringDatabase sDatabase) {
		super(iHost, iPort, iLogger);
		this.database=sDatabase;
		
	}
	
	/**
	 * Runs the thread receiving the OXPath output.  Upon call to {@code this.finishWithOuput()}, all nodes received
	 * on the target output stream are built into an XML document.  This process can be tested if finished with 
	 * {@code this.isDocumentBuilt()}, at which point the output can received with {@code this.returnDocument()} or
	 * {@code this.returnDocumentAsString()}. 
	 */
	@Override
	public void run() {
		try {
			Socket socket = new Socket(host,port);
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
			boolean done = false;
			
			while (!done) {
				Object outRaw = in.readObject();
				OXPathExtractionNode node;
				if (outRaw!=null) {
					node = ((OXPathExtractionNode) outRaw);
					if (node.isEndNode()) done=true;
					else {
						assert database.check(Integer.toString(node.getId()), node.toString());
					}
				}
			}
			in.close();
		} catch (Exception e) {
			logger.error("Problem receiving Extraction Tuple String.  Please see log for further information.",e);
		}
	}
	
	private final StringDatabase database;
	
}
