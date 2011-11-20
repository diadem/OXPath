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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StringWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.ox.comlab.diadem.oxpath.model.OXPathExtractionNode;

/**
 * Used to handle output from OXPath expressions and return the output as XML.  This class has the disadvantage
 * of handling all output in memory in order to build the document.
 * @author AndrewJSel
 *
 */
public class OXPathXMLOutputHandler extends OXPathOutputHandler {

	/**
	 * Constructs the XMLOutputHandler object.
	 * @param host the host for the stream receiving {@code OXPathExtractionNode} instances
	 * @param port the port for the stream receiving {@code OXPathExtractionNode} instances
	 * @param logger the logging environment associated with OXPath
	 * @param iLatch countdown latch that lets the caller know that the XML output document is completely built
	 */
	public OXPathXMLOutputHandler(String host, int port, Logger logger, CountDownLatch iLatch) {
		super(host, port, logger);
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
			this.outDoc = this.returnOutput();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		
		this.latch.countDown();
		this.setDocumentBuilt(true);
	}
	
	/**
	 * Returns {@code true} if the output {@code Document} is built; false otherwise.
	 * @return {@code true} if the output {@code Document} is built; false otherwise.
	 */
	public synchronized boolean isDocumentBuilt() {
		return this.documentBuilt;
	}
	
	/**
	 * Sets the output finished flag in object state
	 * @param setter {@true} when output is finished
	 */
	private synchronized void setDocumentBuilt(boolean setter) {
		this.documentBuilt = setter;
	}
	
	/**
	 * Returns the output {@code Document}, as long as it has been built; returns {@code null} otherwise.
	 * @return the output {@code Document}, as long as it has been built; returns {@code null} otherwise.
	 */
	public Document returnDocument() {
		return this.outDoc;
	}
	
	/**
	 * Returns the output {@code Document} as a {@code String}, as long as it has been built; returns {@code null} otherwise. 
	 * @return the output {@code Document} as a {@code String}, as long as it has been built; returns {@code null} otherwise.
	 */
	public String returnDocumentAsString() {
		return OXPathXMLOutputHandler.getStringFromDocument(this.returnDocument());
	}
	
	/**
	 * Returns output of query.  Builds the document from the nodes received on the output stream
	 * @return XML Document encoding the output of the OXPath expression evaluation
	 * @throws ParserConfigurationException in case of XML parse error
	 * @throws IOException in case of XML parse error
	 * @throws SAXException in case of XML parse error
	 */
	private Document returnOutput() throws ParserConfigurationException, SAXException, IOException {
		//TODO: Sort siblings based on document order
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder(); 
		Document tempDoc = db.newDocument();
		ArrayList<Element> elements = new ArrayList<Element>();
		//elements are identified by position in elements rather than by name 
		elements.add(tempDoc.createElement("results"));
		tempDoc.appendChild(elements.get(0));
		for (OXPathExtractionNode o : nodes) {
			elements.add(o.getId(), tempDoc.createElement(o.getLabel()));
			elements.get(o.getParent()).appendChild(elements.get(o.getId()));
			if (!o.getValue().equals("")) {
				while (o.getValue().contains(LESSTHANSUB)||o.getValue().contains(GREATERTHANSUB)) {
					this.logger.info("Reserved html sequence encountered");
					LESSTHANSUB += "asdfasdf";
					GREATERTHANSUB += "werqeew";
				}
				Text textNode = tempDoc.createTextNode(o.getValue().replace("<",LESSTHANSUB).replace(">",GREATERTHANSUB));//.replace("&", AMPSUB));
				elements.get(o.getId()).appendChild(textNode);
			}
		}
		String tempDocAsString = OXPathXMLOutputHandler.getStringFromDocument(tempDoc).replace(LESSTHANSUB,"<").replace(GREATERTHANSUB, ">");//.replace(AMPSUB,"&");
		Document FinalDoc = db.parse(new InputSource(new ByteArrayInputStream(tempDocAsString.getBytes("utf-8"))));
		
		
		return FinalDoc;

	}
	
	
	/**
	 * Used merely to get an XML string from a document.
	 * Method posted by Ravi Srirangam at http://www.theserverside.com/discussions/thread.tss?thread_id=26060 
	 * @param doc input document
	 * @return String representation of document
	 */
	public static String getStringFromDocument(Document doc)
	{
	    try
	    {
	       DOMSource domSource = new DOMSource(doc);
	       StringWriter writer = new StringWriter();
	       StreamResult result = new StreamResult(writer);
	       TransformerFactory tf = TransformerFactory.newInstance();
	       Transformer transformer = tf.newTransformer();
	       transformer.setOutputProperty(OutputKeys.INDENT, "yes");
           transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
	       transformer.transform(domSource, result);
	       return writer.toString();
	    }
	    catch(TransformerException ex)
	    {
	       ex.printStackTrace();
	       return null;
	    }
	}
	
	/**
	 * Instance field holding the output of the nodes received from the output stream
	 */
	private final ArrayList<OXPathExtractionNode> nodes;
	/**
	 * Instance field holding the output of the OXPath expression 
	 */
	private Document outDoc;
	/**
	 * Instance field encoding whether the document is built or not
	 */
	private boolean documentBuilt = false;
	
	/**
	 * Instance field referencing the countdown latch
	 */
	private CountDownLatch latch;
	
	
	
	//TODO: FIX these
	/**
	 * Some unique value (not occurring in XML) to substitute for < for dealing with the Java Document interface 
	 */
	public static String LESSTHANSUB = "etrwetwert";
	/**
	 * Some unique value (not occurring in XML) to substitute for > for dealing with the Java Document interface 
	 */
	public static String GREATERTHANSUB = "iouopiupiouopiu";
	/**
	 * Some unique value (not occurring in XML) to substitute for & for dealing with the Java Document interface 
	 */
	public static String AMPSUB = "xzvczcvzczxvczcxz";	

}
