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

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import uk.ac.ox.comlab.oxpath.scriptParser.OXPathScripter;
import uk.ac.ox.comlab.oxpath.scriptParser.ParseException;
import uk.ac.ox.comlab.oxpath.scriptParser.SimpleNode;


/**
 * class is deliberately made separate from GroundedOXPathExpression to force user to consider what type of OXPathExpression they have
 * @author AndrewJSel
 */
public class UnGroundedOXPathExpression extends OXPathExpression {
	/**
	 * used for constructing UnGroundedOXPathExpression from a file 
	 * @param inFileName file name with state information
	 * @throws BadDataException thrown if supplied data in <tt>inFileName</tt> is not correct
	 * @throws IOException in case of file read error
	 * @throws RuntimeException in case of bad data
	 * @throws ParseException in case of parsing error
	 */
	public UnGroundedOXPathExpression(String inFileName) throws BadDataException, IOException, ParseException, RuntimeException {
		//calls superclass to initialize instance fields of objects
		super();
		//inputs file into scanner; scanner class used for parsing/tokenizing capabilities
		FileReader reader = new FileReader(inFileName);
		Scanner in = new Scanner(reader);
		
		//flags for determining if database, URL, path, and XQuery have been read
		boolean databaseRead = false;
		boolean urlRead = false;
		boolean pathRead = false;
		boolean XQueryRead = false;
		
		/* use variables to track whether a BadDataException must be thrown (with tracking message);
		 * never thrown immediately, as constructor elegantly deals with bad data and closes scanner object at end.  Also, finds as many errors as possible
		 */
		boolean needException = false;
		String exceptionMessage = "Unable to read data from file!  Errors found:\n";
		//add standard variables that are part of the language, added at the beginning so script can override standard variables
//		variables.putAll(new StandardVariables().getVariables());
		//consider each line in isolation as each can be parsed separately, separate tokenizer object returns parameter and value
		while (in.hasNextLine()&&!XQueryRead) {
			String line = in.nextLine();
			EqualTokenizer tokenMaker = new EqualTokenizer(line);
			//check if we've arrived at the XQuery signal, else process as database, URL, or path 
			if (tokenMaker.getArgument().equalsIgnoreCase(OXPathExpression.XQUERYSIGNAL)) XQueryRead=true;
			else {
				//handle database declaration
				if (tokenMaker.getArgument().equalsIgnoreCase(OXPathExpression.DATABASESIGNAL)) {
					if (!databaseRead) {
						setDatabaseUsed(true);
						setDatabaseName(tokenMaker.getValue());
						databaseRead=true;
					}
					else {
						needException = true;
						exceptionMessage += " Another database declaration found!\n";
					}
				}
				//handle url declaration
				else if (tokenMaker.getArgument().equalsIgnoreCase(OXPathExpression.URLSIGNAL)) {
					if (!urlRead) {
						setURLName(tokenMaker.getValue());
						urlRead=true;
					}
					else {
						needException = true;
						exceptionMessage += " Another URL declaration found!\n";
					}
				}
				//handle path declaration
				else if (tokenMaker.getArgument().equalsIgnoreCase(OXPathExpression.PATHSIGNAL)) {
//					if (!pathRead) {
						this.addPathName(tokenMaker.getValue());
						pathRead=true;
//					}
//					else {
//						needException = true;
//						exceptionMessage += " Another path declaration found!\n";
//					}
				}
				else if (tokenMaker.getArgument().startsWith(VARIABLEDECLARE)) {
//					variables.put(tokenMaker.getArgument(), tokenMaker.getValue());
					variables.putAll(UnGroundedOXPathExpression.variableStringToTuple(tokenMaker.toString()));
				}
			}
		}		
		//Determine if required info (URL, path) are missing and document
		if (!urlRead) {
			needException = true;
			exceptionMessage += " No URL declaration found!\n";
		}
		if (!pathRead) {
			needException = true;
			exceptionMessage += " No path declaration found!\n";
		}
		
		while (in.hasNextLine()) {
			String line = in.nextLine();
			EqualTokenizer tokenMaker = new EqualTokenizer(line);
			if ((tokenMaker.hasNonEmptyArgument()||tokenMaker.hasNonEmptyValue())&&!hasScrapedInfoPath(tokenMaker.getArgument())) {
//				addScrapedInfo(tokenMaker.getArgument(),tokenMaker.getValue());
			}
			else {
				needException = true;
				exceptionMessage += " Improper Scrapped Information declaration found!\n";
			}
		}
		
		//close the Scanner object and FileRead object to make sure all IO is completed
		in.close();
		reader.close();
		
		//clean each path from the <XQ while next> tag
		ArrayList<String> paths = new ArrayList<String>();
		paths.addAll(this.getPathNames());
		//Because strings are immutable, another arrayList is necessary
		ArrayList<String> newPaths = new ArrayList<String>();
		for (String path : paths) {
			//finally, isolate the <XQ while next> tag, if present
			int begin = path.lastIndexOf(XQSTART);
			int end = path.lastIndexOf(XQEND);
			if ((begin>CharacterNotPresent)&&(end>(CharacterNotPresent+XQSTART.length()))&&(begin<end)) {
				String xq = path.substring(begin+XQSTART.length(),end);
				if (xq.contains(OXPathExpression.NEXTSIGNAL)) {
					xq = xq.substring(xq.indexOf(OXPathExpression.NEXTSIGNAL) + OXPathExpression.NEXTSIGNAL.length()).trim();
					this.setNextUsed(true);
					int openParan = xq.indexOf('(');
					int closeParan = xq.lastIndexOf(')');
					//check if parentheses are present, if used correctly, trim off if so
					if ((openParan!=-1||closeParan!=-1)&&((openParan==-1)||(closeParan==-1)||(openParan>=closeParan))) {
						throw new BadDataException("Parentheses not used correctly in <XQ while ()> segment of path");
					}
					else if (openParan<closeParan) {//not true if they are both -1
						xq = xq.substring(openParan+1,closeParan);
					}
					this.addNextLinkNames(TermHelper.commaTokenizer(xq));
				}			
				//having processed it, remove the XQ statement from the end, along with any new trailing path delimiter (/) if present
				path = path.substring(0, begin);
				while (path.lastIndexOf('/')==(path.length()-1)) {
					path = path.substring(0, path.lastIndexOf('/'));
				}
			}
			newPaths.add(path);
		}
		this.setPathNames(newPaths);
		
		//legacy code from when a single path was used and XQ clause was required
//		else {
//			needException = true;
//			exceptionMessage += " XQ not defined properly in navigational XPath!\n";
//		}
		
		//throw exception if necessary data isn't collected or formatted correctly
		if (needException) throw new BadDataException(exceptionMessage);
	}
	
	/**
	 * Method for building an UnGroundedOXPathExpression object from the an AST built from
	 * an OXPath script.
	 * @param root root of the Abstract Syntax Tree
	 */
	public UnGroundedOXPathExpression(SimpleNode root) throws BadDataException{
		super();//calls super() to instantiate instance fields
		this.variables.putAll((new StandardVariables()).getVariables());
		SimpleNode declarations = OXPathScripter.getChildByName(root, "Declarations");
		SimpleNode xqdeclarations = OXPathScripter.getChildByName(root, "XQDeclarations");
		for (int i = 0; i < declarations.jjtGetNumChildren(); i++) {
			SimpleNode context = OXPathScripter.safeGetChild(declarations, i);
			String nodeType = context.toString();
			if (nodeType.equals("DatabaseFileName")) {
				String filenameLiteral = OXPathScripter.getValue(context);
				//TODO: Fix the line below:
				this.setDatabaseName(filenameLiteral.substring(1,filenameLiteral.lastIndexOf(filenameLiteral.charAt(0))));
				this.setDatabaseUsed(true);
			}
			else if (nodeType.equals("URLName")) {
				this.setURLName(OXPathScripter.getValue(context));
				this.setURLUsed(true);
			}
			else if (nodeType.equals("VariableDeclaration")) {
				String variableName = OXPathScripter.getValue(OXPathScripter.getChildByName(context, "VariableName"));
				SimpleNode variablePath = OXPathScripter.getChildByName(context, "OXPathPathSegment");
				variables.put(variableName, variablePath);
			}
			else if (nodeType.equals("PathDeclaration")) {
				if (OXPathScripter.hasChildByName(context, "URLName")) {
					pathobjects.add(new PathObject(OXPathScripter.getValue(OXPathScripter.getChildByName(context, "URLName")), OXPathScripter.getChildByName(context, "OXPathPathSegmentEntry")));
				}
				else if (this.getURLUsed()==true) {
					pathobjects.add(new PathObject(this.getURLName(),OXPathScripter.getChildByName(context, "OXPathPathSegmentEntry")));
				}
				else throw new BadDataException("Path declaration doesn't have a URL specified and no global URL has been defined for the OXPath Expression!");
			}
			else throw new BadDataException("AST structure not consistent with known specification!");
		}
		for (int i = 0; i < xqdeclarations.jjtGetNumChildren(); i++) {
			SimpleNode xqdeclaration = OXPathScripter.safeGetChild(xqdeclarations, i);
			String nodeType = xqdeclaration.toString();
			if (nodeType.equals("ScrapeDeclaration")) {
				this.addScrapedInfo(xqdeclaration);
			}
			else throw new BadDataException("AST structure not consistent with known specification!");
		}
	}
	
	/**
	 * Method for building a list of instantiated paths based on the path attribute of implicit parameter (this object)
	 * Implemented here in order to have access to UnGroundedOXPathExpression state information.
	 * Use this method if you wish to initiate it according to standard procedure from the OXPathExpression script
	 * @return list of grounded paths
	 * @throws BadDataException if QTerms ({*}) aren't formed correctly
	 * @throws SQLException if SQL exception occurs
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public ArrayList<String> GroundedXPathBuilder() throws BadDataException, IOException, ClassNotFoundException, SQLException {
		if (this.getDatabaseUsed()) {
			DatabaseHelper dbhelper = new DatabaseHelper();
			Connection con = dbhelper.getNewConnection(this.getDatabaseName());
			Map<String,String> dbTerms = new HashMap<String,String>();
			return GroundedXPathBuilder(this.getPathNames(),con,dbTerms);
		}
		else {
			return GroundedXPathBuilder(this.getPathNames(),null,null);
		}
	}
	
	/**
	 * Method for building a list of instantiated paths based on the path attribute of implicit parameter (this object)
	 * Implemented here in order to have access to UnGroundedOXPathExpression state information.
	 * @param paths list of ungrounded paths
	 * @param con connection to the database, null if database is not used
	 * @param dBTerms used for recursive calls for forming conjunctive queries for enforcing field association
	 * @return list of grounded paths
	 * @throws BadDataException if QTerms ({*}) aren't formed correctly
	 * @throws SQLException if SQL exception occurs
	 */
	private ArrayList<String> GroundedXPathBuilder(ArrayList<String> paths, Connection con, Map<String, String> dbTerms) throws BadDataException, SQLException {
		ArrayList<String> allPaths = new ArrayList<String>();
		for (String path : paths) {
			allPaths.addAll(this.GroundedXPathBuilder(this.substituteVariablePaths(path), con, dbTerms));
		}
		return allPaths;
	}

	/**
	 * Method for substituting variable names into paths 
	 * @param path path that may (or may not) contain variables declared in the implicit parameter
	 * @return path with variables substituted explicitly
	 * @throws BadDataException in case a variable encountered hasn't been declared
	 */
	public String substituteVariablePaths(String path) throws BadDataException {
		//recursive method so that variables can contain other variables
		//base case
		if (!path.contains(VARIABLEREFERENCEBEGIN)) {
			return path;
		}
		else {
			String rv = "";
			int beginVariable = path.indexOf(VARIABLEREFERENCEBEGIN);
			int endVariable = path.indexOf(VARIABLEREFERENCEEND, beginVariable + 1); //+1 so that / from /$ is detected; safe as long as /$ is more than one character long
			String variableName = path.substring(beginVariable+"/".length(), endVariable);//we don't want to include the leading or trailing /
			if (!this.containsVariable(variableName)) throw new BadDataException("Encountered variable " + variableName + " not declared!");
			rv += path.substring(0, beginVariable);//don't include the leading /
			rv += this.getVariableValue(variableName);
			rv += path.substring(endVariable);//include the trailing /
			return this.substituteVariablePaths(rv);
		}
	}

	/**
	 * Method for building a list of instantiated paths based on the path attribute of implicit parameter (this object)
	 * Implemented here in order to have access to UnGroundedOXPathExpression state information.
	 * @param path the ungrounded path (or segment)
	 * @param con connection to the database, null if database is not used
	 * @param dBTerms used for recursive calls for forming conjunctive queries for enforcing field association
	 * @return list of grounded paths
	 * @throws BadDataException if QTerms ({*}) aren't formed correctly
	 * @throws SQLException if SQL exception occurs
	 */
	private ArrayList<String> GroundedXPathBuilder(String path, Connection con, Map<String,String> dBTerms) throws BadDataException,SQLException{
		ArrayList<String> paths = new ArrayList<String>();
		int locBeginQTerm = path.indexOf(BEGINQTERM);
		int locEndQTerm = path.indexOf(ENDQTERM);
		if ((locBeginQTerm > locEndQTerm) || ((locBeginQTerm==CharacterNotPresent)&&(locEndQTerm!=CharacterNotPresent)) || ((locBeginQTerm!=CharacterNotPresent)&&(locEndQTerm==CharacterNotPresent))) {
			throw new BadDataException(" Malformed QTerm Found!\n");
		}
		//base case, last term
		if ((locBeginQTerm==CharacterNotPresent)&&(locEndQTerm==CharacterNotPresent)) {
			paths.add(path);
		}
		else {
			//stores reference to current working term (info inside first {*})
			String currTerm = path.substring((locBeginQTerm+BEGINQTERM.length()), locEndQTerm);
			//handle attribute, if present
			String attribute = "";
			if (currTerm.startsWith(ActionTerm.ATTRIBUTESTART)) {//store the attribute, including the delimiter for insertion below into grounded OXPathExpressions
				attribute = currTerm.substring(0, (currTerm.indexOf(ActionTerm.ATTRIBUTEDELIMITER) + ActionTerm.ATTRIBUTEDELIMITER.length()));
				currTerm = currTerm.substring(currTerm.indexOf(ActionTerm.ATTRIBUTEDELIMITER) + ActionTerm.ATTRIBUTEDELIMITER.length());
			}
			TermHelper.TermTypes termType = TermHelper.getTermType(currTerm);
			switch (termType) {
			case DATABASE:
				String relationName = currTerm.substring(0, currTerm.indexOf(".")).trim();
				String fieldName;
				if (currTerm.contains(BEGINDEPTERM)) {
					fieldName = currTerm.substring((currTerm.indexOf(".")+1), currTerm.indexOf(QTERMSEPARATOR)).trim();
				}
				else {
					fieldName = currTerm.substring((currTerm.indexOf(".")+1)).trim();
				}
				ArrayList<String> depFields = new ArrayList<String>();
				if (currTerm.contains(BEGINDEPTERM))
					depFields = TermHelper.commaTokenizer(currTerm.substring(currTerm.indexOf(BEGINDEPTERM)+BEGINDEPTERM.length(), currTerm.indexOf(ENDDEPTERM)));
				if ((relationName.length()<1)||(fieldName.length()<1)) {
					throw new BadDataException(" Malformed QTerm Found!\n");
				}
				//use PreparedStatement class to form robust SQL Query for term entry retrieval
				//first, we construct the query String, then populate with values 
				String query = "SELECT DISTINCT " + fieldName + " FROM " + relationName;
				if (!depFields.isEmpty()) {
					query += (" WHERE " + depFields.get(0) + " = ?");
					//size()-1 because we added the first above
					for (int i=1;i<=depFields.size()-1;i++) query += (" AND " + depFields.get(i) + " = ?");
				}
				//populate query with terms
				PreparedStatement prep = con.prepareStatement(query);
				if (!depFields.isEmpty()) {
					for (int i=0;i<=(depFields.size()-1);i++) {
						//+1 because ? start at 1, ArrayList starts at 0
						prep.setString(i+1,dBTerms.get(depFields.get(i)));						
					}
				}
				ResultSet result = prep.executeQuery();
				while (result.next()) {
					String resultTuple = result.getString(fieldName).trim();
					ArrayList<String> recursiveReturn = new ArrayList<String>();
					//add current term to recursive call to capture association
					dBTerms.put(fieldName, resultTuple);
					//call recursively on rest of string after current end term
					recursiveReturn = this.GroundedXPathBuilder(path.substring(locEndQTerm+1), con, dBTerms);
					//remove this fieldName for future iterations
					dBTerms.remove(fieldName);
					for (String recRet : recursiveReturn) {
						//construct the new paths, indexes are chosen so that new paths include {} and "s
						paths.add(path.substring(0,locBeginQTerm) + BEGINQTERM + attribute + "\"" + resultTuple + "\"" + ENDQTERM + recRet);
					}
				}
				break;
			case EXPLICIT:
				ArrayList<String> expTerms = new ArrayList<String>();
				expTerms = TermHelper.commaTokenizer(currTerm);
				ArrayList<String> expRecursiveCall = new ArrayList<String>();
				expRecursiveCall = this.GroundedXPathBuilder(path.substring(locEndQTerm+1), con, dBTerms);
				for (String iTerm : expTerms) {
					TermHelper.quoteChecker(iTerm);
					for (String expRC : expRecursiveCall) {
						paths.add(path.substring(0,locBeginQTerm) + BEGINQTERM + attribute + iTerm + ENDQTERM + expRC);
					}					
				}
				break;
			case POSITION:
				ArrayList<String> posTerms = new ArrayList<String>();
				posTerms = TermHelper.commaTokenizer(currTerm);
				ArrayList<String> posRecursiveCall = new ArrayList<String>();
				posRecursiveCall = this.GroundedXPathBuilder(path.substring(locEndQTerm+1), con, dBTerms);
				for (String iTerm : posTerms) {
					TermHelper.numberChecker(iTerm);
					for (String posRC : posRecursiveCall) {
						paths.add(path.substring(0,locBeginQTerm) + BEGINQTERM + attribute + iTerm + ENDQTERM + posRC);
					}
				}
				break;
			case KEYWORD:
				ArrayList<String> keyRecursiveCall = new ArrayList<String>();
				keyRecursiveCall = this.GroundedXPathBuilder(path.substring(locEndQTerm+1), con, dBTerms);
				for (String keyRC : keyRecursiveCall)
					paths.add(path.substring(0,locBeginQTerm) + BEGINQTERM + attribute + currTerm + ENDQTERM + keyRC);
			}
		}
		return paths;
	}
	
	
	
	/**
	 * method used for removing old path names and replacing them with a new set
	 */
	public void setPathNames(ArrayList<String> paths) {
		this.pathNames.clear();
		this.pathNames.addAll(paths);
	}
	/**
	 * method for adding navigation paths
	 * @param aPathName navigation path
	 */
	public void addPathName(String aPathName){
		pathNames.add(aPathName);
	}
	
	/**
	 * **INTERNAL API - Do not use** method for retrieving navigation paths
	 * @return navigational paths through URL
	 */
	protected ArrayList<String> getPathNames(){
		return this.pathNames;//protect method as it returns the mutable state object to the user
	}
	
	/**
	 * method for getting the number of paths
	 * @return the number of path objects associated with the ungrounded OXPath expression
	 */
	public int getNumberOfPaths() {
		return pathobjects.size();
	}
	
	public PathObject getPath(int index) {
		return new PathObject(pathobjects.get(index).getPathURL(),pathobjects.get(index).getPathAST());
	}
	
	/**
	 * method for retrieving a variable's value by name
	 * @param name the name of the variable to return
	 * @return path value corresponding to the name of the input parameter; returns <tt>null</tt> if no such mapping is found.
	 */
	public SimpleNode getVariableValue(String name) {
		return this.variables.get(name);
	}
	
	/**
	 * method retrieving the set of variables names associated with this object
	 * @return the names of the variables associated with this object
	 */
	public Set<String> getVariableNames() {
		return this.variables.keySet();
	}
	
	/**
	 * method for determining if a variable has a known value
	 * @param name name of variable
	 * @return <tt>true</tt> if <b>name</b> is present, <tt>false</tt> otherwise
	 */
	public boolean containsVariable(String name) {
		return this.variables.containsKey(name);
	}
	
	/**
	 * overridden method used for converting an UnGroundedOXPathExpression object to a String object with state info for testing purposes
	 * @return String object with state info
	 */
	public String toString() {
		StringBuilder out = new StringBuilder();
		Set<String> keys = this.getVariableNames();
		for (String key : keys) {
			out.append(" [variable (");
			out.append(key);
			out.append(" ==> ");
			out.append(OXPathScripter.stringDump(this.getVariableValue(key),""));
			out.append(")]\n");
		}
//		for (String pathName : pathNames) {
//			out.append(" [path ==> " + pathName + "]\n");
//		}
		for (PathObject pathObject : this.pathobjects) {
			out.append(" [ path ( url  ==> ");
			out.append(pathObject.getPathURL());
			out.append("\n");
			out.append("          path ==> ");
			out.append(OXPathScripter.stringDump(pathObject.getPathAST(),"                   "));
			out.append(")]\n");
		}
		return this.toString(out.toString());
	}
	
	/**
	 * instance field for navigation paths of OXPathExpression through form (ungrounded),
	 * stored as an ArrayList of Strings for use with standard XPath class.  Legacy field 
	 * used with first OXPath prototype.  Newer version uses pathList instead.
	 * @deprecated
	 */
	private ArrayList<String> pathNames = new ArrayList<String>();
	
	/**
	 * instance field for navigation paths of OXPathExpression objects (ungrounded),
	 * stored as an ArrayList of PathObjects, which serve to associate a URL with each
	 * path.  The paths are encoded as a reference to the root of the Abstract Syntax
	 * Tree.
	 */
	private ArrayList<PathObject> pathobjects = new ArrayList<PathObject>();
	
	/**
	 * instance field for recording variables (path segments) and their values.  Maps (name,value).
	 */
	Map<String,SimpleNode> variables = new HashMap<String,SimpleNode>();
	
	/**
	 * private class containing the standard variable declarations
	 */
	private class StandardVariables {
		
		/**
		 * standard constructor populating the object with standard variables (below)
		 */
		private StandardVariables() {
			try {
				for (String dec : standardVariablesDec) {
					this.standardVariables.putAll(UnGroundedOXPathExpression.variableStringToTuple(dec));
				}
			} catch (Exception e) {//no input variables here and code is tested, this catch is just to fool the compiler
				e.printStackTrace();
			}
		}
				
		/**
		 * Method that returns the standard variables
		 * @return standard variables in the form of a Map object
		 */
		private Map<String,SimpleNode> getVariables() {
			return this.standardVariables;
		}
		
		/**
		 * instance field storing the standard variable information
		 */
		private Map<String,SimpleNode> standardVariables = new HashMap<String,SimpleNode>();
		
		/**
		 * static field for next declaration
		 */
		private String nextDec = "$next=/next-field::*;";
		/**
		 * static field for previous declaration
		 */
		private String previousDec = "$previous=/previous-field::*;";
		/**
		 * static field for nextclick declaration
		 */
		private String nextClickDec = "$nextClick=/next-field::*/{click};";
		/**
		 * ArrayList containing standard variable declarations
		 */
		private String[] standardVariablesDec = { nextDec, previousDec, nextClickDec };
	}
	
	/**
	 * INTERNAL API - DO NOT USE!!  Use to create small, quickly constructed tuples for insertion into other HashMaps  
	 * @param input
	 * @return
	 * @throws ParseException
	 * @throws BadDataException 
	 * @throws RuntimeException 
	 */
	protected static Map<String,SimpleNode> variableStringToTuple(String input) throws ParseException, RuntimeException, BadDataException {
		Map<String,SimpleNode> rv = new HashMap<String,SimpleNode>(1);//small, quickly created object
		SimpleNode parent = new OXPathScripter(new ByteArrayInputStream(input.getBytes())).VariableDeclaration();
		String variableName = OXPathScripter.getValue(OXPathScripter.getChildByName(parent, "VariableName"));
		SimpleNode variablePath = OXPathScripter.getChildByName(parent, "OXPathPathSegment");
		rv.put(variableName, variablePath);
		return rv;
	}
	
	/**
	 * Constant
	 */
	public final static int CharacterNotPresent = -1;
	/**
	 * Constant
	 */
	public final static String BEGINQTERM = "{";
	/**
	 * Constant
	 */
	public final static String ENDQTERM = "}";
	/**
	 * Constant
	 */
	public final static String QTERMSEPARATOR = ",";
	/**
	 * Constant
	 */
	public final static String BEGINDEPTERM = "dep(";
	/**
	 * Constant
	 */
	public final static String ENDDEPTERM = ")";
	/**
	 * Constant
	 */
	public final static String XQSTART = "<XQ";
	/**
	 * Constant
	 */
	public final static String XQEND = ">";
	/**
	 * Constant
	 */
	public final static String VARIABLEDECLARE = "$";
	/**
	 * Constant
	 */
	public final static String VARIABLEREFERENCEBEGIN = "/$";
	/**
	 * Constant
	 */
	public final static String VARIABLEREFERENCEEND = "/";
}
