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
 * 
 */
package uk.ac.ox.comlab.oxpath;

import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Class for encoding the Next Links navigated to (in a nested fashion) within a single scraping environment. For API, can be abstracted as an iterator after construction. In this way, hasNext() methods returns if more paths exist, while the next() method returns the next node to be clicked for the page and advances the state so the proper path is returned for the next() path call
 * 
 * @author AndrewJSel
 * 
 */
public class NextNavigator {

	/**
	 * class constructor; requires a results page (first to be scraped) and a list of the path expressions for each nested level
	 * 
	 * @param page
	 *            the results page (first to be scraped)
	 * @param nextPaths
	 *            list of the path expressions (yielding nodesets) for each nested level
	 * @throws BadDataException
	 *             in case one of the XPath next expressions returns something other than nodes
	 */
	@SuppressWarnings("unchecked")
	// in reality, this is checked, but jvm can't detect this
	public NextNavigator(HtmlPage page, ArrayList<String> nextPaths) throws BadDataException {
		// return the nodes for each path expression, and load their paths into the object state

		for (String nextPath : nextPaths) {
			List<?> returnedValues = page.getByXPath(nextPath);
			// check to make sure we can use this
			if (!returnedValues.isEmpty() && !(returnedValues.get(0) instanceof DomNode))
				throw new BadDataException("One or more XPath expressions for next links did not return node sets!");
			List<DomNode> returnNodes = (List<DomNode>) returnedValues;
			ArrayList<String> tempPaths = new ArrayList<String>();
			for (DomNode returnNode : returnNodes) {
				tempPaths.add(returnNode.getCanonicalXPath());
			}
			// only add a nextNodes object if there are values to put in
			if (!returnedValues.isEmpty())
				this.nextNodesList.add(new NextNodes(tempPaths));
		}
		if (!this.nextNodesList.isEmpty()) {// if list isn't empty, the next link is the first path
			this.currentPath = this.nextNodesList.get(0).nodes.get(0);
			this.currentNextNodesLevel = 0;
		}
	}

	/**
	 * Determines if the current NextNavigator object has a next path if the currentPath (last return of next()) fails. Trivially, there will always be a next link available if the last link succeeded and the object has paths (as the same path is returned at minimum)
	 * 
	 * @return <tt>true</tt> if there is a next path, <tt>false</tt> otherwise
	 */
	public boolean hasNext() {
		if (this.isEmpty()) {
			return false;
		}
		boolean rv = false;// particularly useful in case this is false
		if (this.nextNodesList.get(this.currentNextNodesLevel).position < (this.nextNodesList.get(this.currentNextNodesLevel).nodes.size() - 1)) {
			rv = true;
			return rv;
		}
		for (int i = this.currentNextNodesLevel + 1; i < this.nextNodesList.size(); i++) {
			if (this.nextNodesList.get(i).position < this.nextNodesList.get(i).nodes.size()) {
				rv = true;
				return rv;
			}
		}
		return rv;// false
	}

	// for (NextNodes tempNext : this.nextNodesList) {
	// if ((tempNext.position==this.currentNextNodesLevel)&&tempNext.position < (tempNext.nodes.size() - 1)) {
	// rv = true;
	// return rv;
	// }
	// else if ((tempNext.position!=this.currentNextNodesLevel)&&tempNext.position < tempNext.nodes.size()) {
	// rv = true;
	// return rv;
	// }
	// }

	/**
	 * Returns the next path and advances the state of the object. Will return a different link depending on whether or not the link at the last return of next returned a new results page
	 * 
	 * @param lastSucceed
	 *            value if the last link returned succeeded or failed in returning a successful page (set to true if this is the first call to next())
	 * @return the next path
	 * @throws BadDataException
	 */
	public String next(boolean lastSucceed) throws BadDataException {
		// get return value (stored in constructor first time, by the side effects of this method all others
		String rv = this.currentPath;
		if (lastSucceed == true) {
			for (int i = 0; i < this.currentNextNodesLevel; i++) {
				this.nextNodesList.get(0).position = 0;
			}
			// specify state info that you start again at the lowest nodes if successful (unless you are already on the first [0] level)
			if (this.currentNextNodesLevel > 0) {
				this.currentPath = this.nextNodesList.get(0).nodes.get(this.nextNodesList.get(0).position);
				this.currentNextNodesLevel = 0;
			}
		} else {
			boolean done = false;
			if (this.nextNodesList.get(this.currentNextNodesLevel).position < this.nextNodesList.get(this.currentNextNodesLevel).nodes.size() - 1) {
				this.nextNodesList.get(this.currentNextNodesLevel).position++;
				this.currentPath = this.nextNodesList.get(this.currentNextNodesLevel).nodes.get(this.nextNodesList.get(this.currentNextNodesLevel).position);
				done = true;
			}
			if (!done)
				this.currentNextNodesLevel++;
			while (!done) {
				if (this.currentNextNodesLevel >= this.nextNodesList.size())
					throw new BadDataException("Called NextNavigator.next(false) when no next path available!");
				if (this.nextNodesList.get(this.currentNextNodesLevel).position < this.nextNodesList.get(this.currentNextNodesLevel).nodes.size()) {
					this.currentPath = this.nextNodesList.get(this.currentNextNodesLevel).nodes.get(this.nextNodesList.get(this.currentNextNodesLevel).position);
					this.nextNodesList.get(this.currentNextNodesLevel).position++;
					done = true;
				}
				if (!done)
					this.currentNextNodesLevel++;
			}
			for (int i = 0; i < this.currentNextNodesLevel; i++) {// reset all the lower level node levels
				this.nextNodesList.get(i).position = 0;
			}
		}
		return rv;
		// //advance the object's internal state
		// int advanceLevel = 0;//encodes what level the new path is initialized at so the lower levels can be reset
		// boolean done = false;//signals if more work is to be done
		// for (NextNodes tempNext : this.nextNodesList) {//go through nodes in order until the next is encountered
		// if ((this.currentNextNodesLevel==advanceLevel)&&!done&&(tempNext.position < tempNext.nodes.size()-1)) {
		// tempNext.position++;//when position is the size of the node, all links at this level are exhausted
		// this.currentPath = tempNext.nodes.get(tempNext.position);
		// done = true;
		// this.currentNextNodesLevel = advanceLevel;
		// }
		// else if ((this.currentNextNodesLevel!=advanceLevel)&&!done&&(tempNext.position < tempNext.nodes.size())) {
		// //when position is the size of the node, all links at this level are exhausted
		// this.currentPath = tempNext.nodes.get(tempNext.position);
		// done = true;
		// this.currentNextNodesLevel = advanceLevel;
		// }
		// if (!done) advanceLevel++;
		// }
		// if (!done) throw new BadDataException("Called next() on a NextNavigator object that had no more paths!");
		// for (int i = 0; i < advanceLevel; i++) {//reset all the lower level node levels
		// this.nextNodesList.get(i).position = 0;
		// }
		// return rv;
	}

	/**
	 * Returns the current "Next" navigational path (the result of the previous next), w/o side effect in respect to object state
	 * 
	 * @return the current "Next" navigational path
	 */
	public String getCurrentPath() {
		return this.currentPath;
	}

	/**
	 * method for determining if the implicit parameter is empty (stores no paths)
	 * 
	 * @return <tt>true</tt> if the implicit parameter stores no paths, <tt>false</tt> otherwise
	 */
	public boolean isEmpty() {
		return this.nextNodesList.isEmpty();// as the constructor only creates non-empty NextNodes objects
	}

	/**
	 * method for testing purposes that outputs state info in (pseudo)XML
	 */
	@Override
	public String toString() {
		String rv = "";
		rv += " <NextNavigator currentNextNodesLevel=" + this.currentNextNodesLevel + " currentPath=" + this.currentPath + ">\n";
		for (NextNodes nextNodes : this.nextNodesList) {
			rv += nextNodes.toString();
		}
		rv += " </NextNavigator>\n";
		return rv;
	}

	/**
	 * state info holding a series of paths resulting from the nodesets produced by the constructed next links
	 */
	private final ArrayList<NextNodes> nextNodesList = new ArrayList<NextNodes>();

	/**
	 * if this is a <tt>NextNavigator</tt> constructed by another <tt>NextNavigator</tt> object, this instance field stores the removed path (the next to be traversed)
	 */
	private String currentPath = "";

	/**
	 * instance field storing level which current path is on
	 */
	private int currentNextNodesLevel;

	/**
	 * private class encoding the nodeset to be traversed at a specific level
	 */
	private class NextNodes {
		/**
		 * typical constructor that starts with no state information
		 */
		private NextNodes() {
		}

		/**
		 * constructor that creates an object holding the paths specified by the parameter paths ArrayList of paths
		 */
		private NextNodes(ArrayList<String> paths) {
			this.nodes.addAll(paths);
			this.position = 0;// initialized to 0 as this is where we are in the array
		}

		/**
		 * method for testing purposes that outputs state info in (pseudo)XML
		 */
		@Override
		public String toString() {
			String rv = "";
			rv += " <NextNodes position=\"" + this.position + "\">\n";
			for (String node : this.nodes) {
				rv += "  <Node>" + node + "</Node>\n";
			}
			rv += " </NextNodes>\n";
			return rv;
		}

		/**
		 * arraylist holding the nodes
		 */
		private final ArrayList<String> nodes = new ArrayList<String>();

		/**
		 * integer holding where we the current next is in the array
		 */
		private int position;
	}
}
// legacy code before the interface was much more open (not a good idea)
// return the nodes for each next path expression on the current path
// ArrayList<List<DomNode>> nextNodes = new ArrayList<List<DomNode>>();

// for (String next : tempNextPaths) {
// nextNodes.add((List<DomNode>) page.getByXPath(next));
// }
// //now retrieve the XPath info for each returned node (on current page) and store so the corresponding node can be found on further pages
// ArrayList<ArrayList<String>> nextNodePaths = new ArrayList<ArrayList<String>>();
// for (int i = 0; i < nextNodes.size(); i++) {
// nextNodePaths.add(new ArrayList<String>());
// for (List<DomNode> nodeList : nextNodes.get(i)) {
//				
// }
// }
// }

// /**
// * constructor used only to produce a new object to be returned by the
// */
// private NextNavigator() {}
//	
// public NextNavigator prepareNextScrap() {
// NextNavigator rv = new NextNavigator();
// int numPaths = rv.getNumberPaths();
// if (numPaths>1) {
// for (int i = 0; i < numPaths-1; i++) {//we don't want to do the last path set here
// ArrayList<String> tempPaths = new ArrayList<String>();
// for (String path : this.nextNodesList.get(i).nodes) {
// tempPaths.add(path);
// }
// rv.nextNodesList.add(new NextNodes(tempPaths));
// }
// }
// if (numPaths>0) {//handle the last set
// int numNodes = this.getNumberNodesInPath(numPaths-1);
// if (numNodes>0) {//should always be true, but otherwise doesn't insert an empty NodeList
// rv.removedPath = this.getNodePath(numPaths-1, 0);
// if (numNodes>1) {
// ArrayList<String> tempPaths = new ArrayList<String>();
// for (int i = 1; i < NumNodes) {//don't insert the first string
// tempPaths.add(path);
// }
// rv.nextNodesList.add(new NextNodes(tempPaths));
// }
// }
// }
// return rv;
// }
//	
// /**
// * method for returning the removed path (the next to be traversed) if this is a <tt>NextNavigator</tt> constructed by another <tt>NextNavigator</tt> object
// * @return the next path to be traversed; "" if this NextNavigator was not constructed in that way
// */
// public String getNextPath() {
// return this.removedPath;
// }
//	
// /**
// * method for returning the number of path sets present
// * @return number of path sets
// */
// public int getNumberPaths() {
// return this.nextNodesList.size();
// }
//	
// /**
// * method for identifying the number of paths within a particular set
// * @param pathNumber path set number
// * @return number of paths within the set
// */
// public int getNumberNodesInPath(int pathNumber) {
// return this.nextNodesList.get(pathNumber).nodes.size();
// }
//	
// /**
// * method for retrieving a specific path
// * @param pathNumber path set number
// * @param nodeNumber path position within set
// * @return path as determined by the input parameters
// */
// public String getNodePath(int pathNumber, int nodeNumber) {
// return this.nextNodesList.get(pathNumber).nodes.get(nodeNumber);
// }
