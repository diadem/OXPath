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
 * Package containing supporting classes, derived from the OXPath model (which itself extends the XPath model).
 */
package uk.ac.ox.comlab.diadem.oxpath.model;

import static uk.ac.ox.comlab.diadem.oxpath.model.OXPathType.OXPathTypes.NODESET;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import diadem.common.web.dom.DOMNode;

import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;


/**
 * 
 * Extends <tt>DomNode</tt> instead of <tt>Node</tt> because we will use <tt>DomNode</tt> specific methods like <tt>getByXPath()</tt>.  Implemented as
 * contracted by Java library interfaces except where noted.  In particular, for efficiency, lists only contain each element at most one time (set-based
 * paradigm)
 * @author AndrewJSel
 *
 */
public class OXPathNodeList<T extends OXPathContextNode> implements List<T>, Comparator<T> {//previous version also supported W3C's NodeList

	public OXPathNodeList() {}
	
	public OXPathNodeList(T node) {
		this();
		this.add(node);
	}

	/**
	 * Adopts set paradigm; only adds an object if it isn't present already
	 */
	@Override
	public boolean add(T arg0) {
		return  (!nodelist.contains(arg0)) ? nodelist.add(arg0) : false;
	}

	@Override
	public void add(int arg0, T arg1) {
		if (!nodelist.contains(arg1)) nodelist.add(arg0,arg1);
	}

	/**
	 * Returns <true> if any value is added because it's not already present in the list; false otherwise
	 */
	@Override
	public boolean addAll(Collection<? extends T> arg0) {
		boolean rv = false;
		for (T iArg : arg0) {
			if (!this.nodelist.contains(iArg)) {
				rv=nodelist.add(iArg);				
			}
		}
		return rv;
	}

	/**
	 * Returns <true> if any value is added because it's not already present in the list; false otherwise
	 */
	@Override
	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		boolean rv = false;
		int count = arg0;
		for (T iArg : arg1) {
			if (!nodelist.contains(iArg)) {
				nodelist.add(count,iArg);
				rv=true;
				count++;
			}
		}
		return rv;
	}
	
	/**
	 * Adds list elements only if they are compatible
	 * @param arg0 list to add to data structure
	 */
	@SuppressWarnings("unchecked")
	public void addList(List<?> arg0) {
		for (Object iArg : arg0) {
			if (iArg instanceof OXPathContextNode) {
				this.add((T) iArg);
			}
		}
	}
	
	@Override
	public void clear() {
		nodelist.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return nodelist.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return nodelist.containsAll(arg0);
	}

	@Override
	public T get(int arg0) {
		return nodelist.get(arg0);
	}

	@Override
	public int indexOf(Object arg0) {
		return nodelist.indexOf(arg0);
	}

	@Override
	public boolean isEmpty() {
		return nodelist.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return nodelist.iterator();
	}

	@Override
	public int lastIndexOf(Object arg0) {
		return nodelist.lastIndexOf(arg0);
	}

	@Override
	public ListIterator<T> listIterator() {
		return nodelist.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int arg0) {
		return nodelist.listIterator(arg0);
	}

	@Override
	public boolean remove(Object arg0) {
		return nodelist.remove(arg0);
	}

	@Override
	public T remove(int arg0) {
		return nodelist.remove(arg0);
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return nodelist.removeAll(arg0);
	}

	@Override
	public boolean retainAll(Collection<?> arg0) {
		return nodelist.retainAll(arg0);
	}

	@Override
	public T set(int arg0, T arg1) {
		return nodelist.set(arg0, arg1);
	}

	@Override
	public int size() {
		return nodelist.size();
	}

	@Override
	public List<T> subList(int arg0, int arg1) {
		return nodelist.subList(arg0,arg1);
	}

	@Override
	public Object[] toArray() {
		return nodelist.toArray();
	}

	@SuppressWarnings("hiding")
	@Override
	public <T> T[] toArray(T[] arg0) {
		return nodelist.toArray(arg0);
	}
	
	/**
	 * Calls HtmlUnit's getByXPath method over the entire context and returns an (emulated) sorted set
	 * @param path XPath to pass to HtmlUnit 
	 * @throws OXPathException in case of bad data
	 */
	public OXPathNodeList<OXPathContextNode> getByXPath(String path) throws OXPathException {
		return this.getByXPath(path, true).nodeList();
	}
	
	/**
	 * Calls HtmlUnit's getByXPath method over the entire context and returns an (emulated) sorted set.  Used for a backwards axis.
	 * @param path XPath to pass to HtmlUnit 
	 * @throws OXPathException in case of bad data
	 */
	public OXPathNodeList<OXPathContextNode> getByXPathBackwardsAxis(String path) throws OXPathException {
		return this.getByXPath(path, false).nodeList();
	}
	
	/**
	 * Calls HtmlUnit's getByXPath method over the entire context and returns an (emulated) sorted set
	 * @param path XPath to pass to HtmlUnit
	 * @param forward <tt>true</tt> if sort by document order (because of forward axis) or <tt>false</tt> if sort by reverse document order (because of inverse axis)
	 * @throws OXPathException in case of bad data
	 */
	public OXPathType getByXPath(String path, boolean forward) throws OXPathException {
		OXPathNodeList<T> result = new OXPathNodeList<T>();
		for(T iCC : this.nodelist) {
			//since we are passing in XPath, no extraction are encountered, so parent and current are the same
			OXPathType iResult = iCC.getByXPath(path);
			if (iResult.isType().equals(NODESET)) {
				result.addList(iResult.nodeList());
			}
			else return iResult;//assumption here is that a boolean, string, number returned from one expression will not return a node set for a different context node for the same expression
		}
		if (forward) return new OXPathType(result.sortForwardOrder());
		else return new OXPathType(result.sortReverseOrder());
	}
	
	/**
	 * Returns the same node list, sorted in document order
	 * @return the same node list, sorted in document order
	 */
	public OXPathNodeList<T> sortForwardOrder() {
		Collections.sort(this, this);
		return this;
	}
	
	/**
	 * Returns the same node list, sorted in reverse document order
	 * @return the same node list, sorted in reverse document order
	 */
	public OXPathNodeList<T> sortReverseOrder() {
		Collections.sort(this, Collections.reverseOrder(this));
		return this;
	}
	
	/**
	 * Uses XPathHelper to compare and sort nodes.  Unfortunately, the interface does not allow an exception to be raised if the nodes are not comparable, so care should be taken before sorting 
	 * (perhaps by checking all nodes are in the same document).  Currently returns {@code Integer.MAX_VALUE} if not a number
	 * @param o1 first OXPathContextNode to compare
	 * @param o2 second OXPathContextNode to compare
	 * @return standard Java Comparator convention
	 */
	@Override
	public int compare(T o1, T o2) {
		//compare position of DOMNodes
		DOMNode n1 = o1.getNode();
		DOMNode n2 = o2.getNode();
		
		short position = n1.compareDocumentPosition(n2);
		
		//This seems counterintuitive, but this is what we want based on Java's definition of "natural ordering" - for us, we assume document order to be the natural ordering, so preceding nodes have higher ordering values
		if (position==0) return 0;
		else if ((position & DOCUMENT_POSITION_PRECEDING)== DOCUMENT_POSITION_PRECEDING) return 1;
		else if ((position & DOCUMENT_POSITION_FOLLOWING)== DOCUMENT_POSITION_FOLLOWING) return -1;
		else return Integer.MAX_VALUE;
	}

	
	/**
	 * structure holding state data
	 */
	private ArrayList<T> nodelist = new ArrayList<T>();

	/**
	 * Returns XML as a {@code String} object
	 * @return XML as a {@code String} object
	 */
	public String asXML() {
		StringBuilder out = new StringBuilder();
		for (T node : nodelist) {
			DOMNode domnode = node.getNode();
			out.append(domnode.toPrettyHTML());
		}
		return out.toString();
	}
	
	
	// DocumentPosition constants
	/**
	 * Document position constants
	 */
	  static final short      DOCUMENT_POSITION_DISCONNECTED = 0x01;
	  /**
		 * Document position constants
		 */
	  static final short      DOCUMENT_POSITION_PRECEDING    = 0x02;
	  /**
		 * Document position constants
		 */
	  static final short      DOCUMENT_POSITION_FOLLOWING    = 0x04;
	  /**
		 * Document position constants
		 */
	  static final short      DOCUMENT_POSITION_CONTAINS     = 0x08;
	  /**
		 * Document position constants
		 */
	  static final short      DOCUMENT_POSITION_CONTAINED_BY = 0x10;
	  /**
		 * Document position constants
		 */
	  static final short      DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 0x20;
	
}
