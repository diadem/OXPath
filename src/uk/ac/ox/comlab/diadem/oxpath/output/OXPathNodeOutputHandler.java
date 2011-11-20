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
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathExtractionNode;

/**
 * Used to handle output from OXPath expressions and return the output as a list of {@code OXPathExtractionNode} objects.
 * This class has the disadvantage of storing all output in memory before it is returned
 * of handling all output in memory in order to build the document.
 * @author AndrewJSel
 *
 */
public class OXPathNodeOutputHandler extends OXPathOutputHandler {

	/**
	 * Constructs the XMLOutputHandler object.
	 * @param host the host for the stream receiving {@code OXPathExtractionNode} instances
	 * @param port the port for the stream receiving {@code OXPathExtractionNode} instances
	 * @param logger the logging environment associated with OXPath
	 * @param iLatch countdown latch that lets the caller know that the XML output document is completely built
	 */
	public OXPathNodeOutputHandler(String host, int port, Logger logger, CountDownLatch iLatch) {
		super(host,port,logger);
		this.latch = iLatch;
		nodes = new ArrayList<OXPathExtractionNode>();
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
			ObjectInputStream in = new ObjectInputStream(new Socket(host,port).getInputStream());
			boolean done = false;
			while (!done) {
				Object outRaw = in.readObject();
				OXPathExtractionNode node;
				if (outRaw!=null) {
					node = ((OXPathExtractionNode) outRaw);
					if (node.getId()==-1) done=true;
					else nodes.add(node);
				}
			}
			
			//once finished, build the output document
			in.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		this.latch.countDown();
		this.outputFinished(true);
	}
	
	/**
	 * Returns {@code true} if the output {@code Document} is built; false otherwise.
	 * @return {@code true} if the output {@code Document} is built; false otherwise.
	 */
	public synchronized boolean isOutputFinished() {
		return this.outputFinished;
	}
	
	/**
	 * Sets the output finished flag in object state
	 * @param setter {@true} when output is finished
	 */
	private synchronized void outputFinished(boolean setter) {
		this.outputFinished = setter;
	}
	
	/**
	 * Returns the output extraction node list, as long as it has been built; returns {@code null} otherwise. 
	 * @return the output extraction node list, as long as it has been built; returns {@code null} otherwise.
	 */
	public ArrayList<OXPathExtractionNode> returnDocument() {
		return new ArrayList<OXPathExtractionNode>(this.nodes);
	}
	
	/**
	 * Instance field holding the output of the nodes received from the output stream
	 */
	private final ArrayList<OXPathExtractionNode> nodes;
	/**
	 * Instance field encoding whether the document is built or not
	 */
	private boolean outputFinished = false;
	
	/**
	 * Instance field referencing the countdown latch
	 */
	private CountDownLatch latch;
	
}
