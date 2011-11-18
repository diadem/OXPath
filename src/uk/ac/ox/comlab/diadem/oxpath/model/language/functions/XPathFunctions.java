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
 * This subpackage includes classes and interface relating to the functions of the OXPath language. 
 */
package uk.ac.ox.comlab.diadem.oxpath.model.language.functions;

import java.util.ArrayList;

import uk.ac.ox.comlab.diadem.oxpath.core.state.PAATStateEvalSet;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathContextNode;
import uk.ac.ox.comlab.diadem.oxpath.model.OXPathType;
import uk.ac.ox.comlab.diadem.oxpath.utils.OXPathException;

/**
 * @author AndrewJSel
 *
 */
public enum XPathFunctions implements XPathFunction {
	
	/**
	 * XPath <tt>position</tt> function
	 */
	POSITION("position",0,0) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return new OXPathType(state.getPosition());
		}
	},
	
	/**
	 * XPath <tt>last</tt> function
	 */
	LAST("last",0,0) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return new OXPathType(state.getLast());
		}
	},
	
	/**
	 * XPath <tt>count</tt> function
	 */
	COUNT("count",1,1) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return new OXPathType(args.get(0).nodeList().size());
		}
	},
	
	/**
	 * XPath <tt>id</tt> function
	 */
	ID("id",1,1) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return new OXPathType(args.get(0).nodeList().getByXPath("id(.)"));/*a bit of hack; our getByXPath function builds the set
			   *by constructing results from a single node at a time;
			   *meant normally for navigation*/
		}
	},
	
	/**
	 * XPath <tt>namespace-uri</tt> function
	 */
	NAMESPACEURI("namespace-uri",0,1) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			if (args.isEmpty()) return state.getContextSet().get(0).getByXPath("namespace-uri(.)");
			else return args.get(0).nodeList().get(0).getByXPath("namespace-uri(.)");
		}
	},
	
	/**
	 * XPath <tt>local-name</tt> function
	 */
	LOCALNAME("local-name",0,1) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			if (args.isEmpty()) return state.getContextSet().get(0).getByXPath("local-name(.)");
			else return args.get(0).nodeList().get(0).getByXPath("local-name(.)");
		}
	},

	/**
	 * XPath <tt>name</tt> function
	 */
	NAME("name",0,1) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			if (args.isEmpty()) return state.getContextSet().get(0).getByXPath("name(.)");
			else return args.get(0).nodeList().get(0).getByXPath("name(.)");
		}	
	},
	
	/**
	 * XPath <tt>string</tt> function
	 */
	STRING("string",0,1) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			if (args.isEmpty()) return new OXPathType (new OXPathType(state.getContextSet()).string());
			else return new OXPathType(args.get(0).string());
		}
	},
	
	/**
	 * XPath <tt>concat</tt> function
	 */
	CONCAT("concat",2,Integer.MAX_VALUE) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			StringBuilder sb = new StringBuilder();
			for (OXPathType arg : args) {
				sb.append(arg.string());
			}
			return new OXPathType(sb.toString());
		}
	},
	
	/**
	 * XPath <tt>starts-with</tt> function
	 */
	STARTSWITH("starts-with",2,2) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return new OXPathType(args.get(0).string().startsWith(args.get(1).string()));
		}
	},
	
	/**
	 * XPath <tt>contains</tt> function
	 */
	CONTAINS("contains",2,2)  {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return new OXPathType(args.get(0).string().contains(args.get(1).string()));
		}
	},
	
	/**
	 * XPath <tt>substring-before</tt> function
	 */
	SUBSTRINGBEFORE("substring-before",2,2) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			if (!(args.get(0).string().contains(args.get(1).string()))) return new OXPathType(false);
			return new OXPathType(args.get(0).string().substring(0, args.get(0).string().indexOf(args.get(1).string())));
		}
	},
	
	/**
	 * XPath <tt>substring-after</tt> function
	 */
	SUBSTRINGAFTER("substring-after",2,2)  {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			if (!(args.get(0).string().contains(args.get(1).string()))) return new OXPathType(false);
			//have to adjust for the length of the second argument for substring after
			return new OXPathType(args.get(0).string().substring(args.get(0).string().indexOf(args.get(1).string())+args.get(1).string().length()));
		}		
	},
	
	/**
	 * XPath <tt>substring</tt> function
	 */
	SUBSTRING("substring",2,3)  {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			//-1 because XPath begins string index at 1, java at 0
			if (args.size()==2) return new OXPathType(args.get(0).string().substring(args.get(1).number().intValue()+1));
			else return new OXPathType(args.get(0).string().substring(args.get(1).number().intValue()-1,
					                                                  args.get(1).number().intValue()-1+args.get(2).number().intValue()));
		}		
	},
	
	/**
	 * XPath <tt>string-length</tt> function
	 */
	SUBSTRINGLENGTH("string-length",0,1)  {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			if (args.isEmpty()) return new OXPathType(new OXPathType(state.getContextSet()).string().length());
			else return new OXPathType(args.get(0).string().length());
		}
	},
	
	/**
	 * XPath <tt>normalize-space</tt> function
	 */
	NORMALIZESPACE("normalize-space",0,1)  {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			if (args.isEmpty()) return new OXPathType(new OXPathType(state.getContextSet()).string().trim());//NOTE: java trim() strips C0 control chars, xpath normalize-space() doesn't
			else return new OXPathType(args.get(0).string().trim());
		}
	},
	
	/**
	 * XPath <tt>translate</tt> function
	 */
	TRANSLATE("translate",3,3)  {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			String target = args.get(1).string();
			String replacement = args.get(2).string();
			if (replacement.length() > target.length()) replacement = replacement.substring(0, target.length());
			return new OXPathType(args.get(0).string().replace(target, replacement));
		}
	},
	
	/**
	 * XPath <tt>Boolean</tt> function
	 */
	BOOLEAN("Boolean",1,1)  {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return new OXPathType(args.get(0).booleanValue());
		}		
	},
		
	/**
	 * XPath <tt>not</tt> function
	 */
	NOT("not",1,1) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return new OXPathType(!args.get(0).booleanValue());
		}
	},
	
	/**
	 * XPath <tt>true</tt> function
	 */
	TRUE("true",0,0) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return new OXPathType(true);
		}
	},
	
	/**
	 * XPath <tt>false</tt> function
	 */
	FALSE("false",0,0) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return new OXPathType(false);
		}
	},
	
	/**
	 * XPath <tt>lang</tt> function
	 */
	LANG("lang",1,1)  {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return state.getContextSet().get(0).getByXPath("lang(" + args.get(0).string() + ")");
		}		
	},
	
	/**
	 * XPath <tt>number</tt> function
	 */
	NUMBER("number",0,1) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			if (args.isEmpty()) return new OXPathType(new OXPathType(state.getContextSet()).number());
		    return new OXPathType(args.get(0).number());
		}
	},
	
	/**
	 * XPath <tt>sum</tt> function
	 */
	SUM("sum",1,1) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			double sum = 0;
			for (OXPathContextNode o : args.get(0).nodeList()) {
				double d = o.getByXPath("number(.)").number();
				sum += d;
			}
			return new OXPathType(sum);
		}
	},
	
	/**
	 * XPath <tt>floor</tt> function
	 */
	FLOOR("floor",1,1) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return new OXPathType(Math.floor(args.get(0).number()));
		}
	},
	
	/**
	 * XPath <tt>ceiling</tt> function
	 */
	CEILING("ceiling",1,1) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return new OXPathType(Math.ceil(args.get(0).number()));
		}
	},
	
	/**
	 * XPath <tt>round</tt> function
	 */
	ROUND("round",1,1) {
		/**
		 * Evaluates the function (by PAAT with a list of arguments).  Number of arguments is not checked.  
		 * This should be checked beforehand with {@code checkParameterCount(int)}
		 * @param args the list of parameters, computed as a list of {@code OXPathType} objects
		 * @param state the state of the evaluation
		 * @return the return value of the function as prescribed in the XPath 1.0 standard {@link http://www.w3.org/TR/xpath/}
		 * @throws OXPathException in case of function error
		 */
		public OXPathType evaluate(ArrayList<OXPathType> args, PAATStateEvalSet state) throws OXPathException {
			return new OXPathType(Math.round(args.get(0).number()));
		}
	};	
	
	/**
	 * Constructor for each object in the {@code enum}
	 * @param iName name of the function
	 * @param minP minimum number of parameters it accepts
	 * @param maxP maximum number of parameters it accepts
	 */
	private XPathFunctions(String iName, int minP, int maxP) {
		this.name = iName;
		this.minParam = minP;
		this.maxParam = maxP;
	}
	
	/**
	 * Returns the function name (no parentheses)
	 * @return the function name (no parentheses)
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns the minimum number of parameters this function accepts
	 * @return the minimum number of parameters this function accepts
	 */
	public int getMinParameters() {
		return this.minParam;
	}
	
	/**
	 * Returns the maximum number of parameters this function accepts
	 * @return the maximum number of parameters this function accepts
	 */
	public int getMaxParameters() {
		return this.maxParam;
	}
	
	/**
	 * Given the number of parameters, can statically check if this function accepts this number of parameters
	 * @param numParam num of parameters to check in a function call
	 * @return {@code true} if the number of parameters are legal; {@code false} otherwise
	 */
	public boolean checkParameterCount(int numParam) {
		return ((numParam >= this.minParam) && (numParam <= maxParam)) ? true : false ;
	}	
	
	/**
	 * instance field encoding name for each object
	 */
	private final String name;
	/**
	 * instance field encoding minimum parameters for each object
	 */
	private final int minParam;
	/**
	 * instance field encoding maximum parameters for each object
	 */
	private final int maxParam;

}
