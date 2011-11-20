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
 * This subpackage includes classes and interface relating to the OXPath language. 
 */
package uk.ac.ox.comlab.diadem.oxpath.model.language;

/**
 * Class encoding XPath name tests
 * @author AndrewJSel
 *
 */
public class XPathNameTest implements NodeTest {

	/**
	 * basic constructor
	 * @param iSuffix the value of the suffix as a {@code String}
	 * @param iSuffixIsWildcard {@code true} if the suffix is the XPath name test wildcard ('*'); false otherwise
	 */
	public XPathNameTest(String iSuffix, boolean iSuffixIsWildcard) {
		this.hasPrefix = false;
		this.prefix = "";
		this.suffix = iSuffix;
		this.suffixIsWildcard = iSuffixIsWildcard;
	}

	/**
	 * basic constructor
	 * @param iPrefix the value of the prefix as a {@code String}
	 * @param iSuffix the value of the suffix as a {@code String}
	 * @param iSuffixIsWildcard {@code true} if the suffix is the XPath name test wildcard ('*'); false otherwise
	 */
	public XPathNameTest(String iPrefix, String iSuffix, boolean iSuffixIsWildcard) {
		this.hasPrefix = true;
		this.prefix = iPrefix;
		this.suffix = iSuffix;
		this.suffixIsWildcard = iSuffixIsWildcard;
	}

	/**
	 * returns {@code true} if the XPath name test has a prefix; {@code false} otherwise
	 * @return {@code true} if the XPath name test has a prefix; {@code false} otherwise
	 */
	public boolean hasPrefix() {
		return this.hasPrefix;
	}

	/**
	 * returns {@code true} if the suffix is the XPath name test wildcard ('*'); false otherwise
	 * @return {@code true} if the suffix is the XPath name test wildcard ('*'); false otherwise
	 */
	public boolean isSuffixWildcard() {
		return this.suffixIsWildcard;
	}

	/**
	 * returns the value of the node test encoded as a {@code String} object
	 * @return the value of the node test encoded as a {@code String} object
	 */
	@Override
	public String getValue() {
		return hasPrefix? this.prefix + NAMESPACEDELIMITER + this.suffix : this.suffix;
	}

	/**
	 * returns the type associated with this node test
	 * @return the type associated with this node test
	 */
	@Override
	public NodeTestType getType() {
		return NodeTestType.NAMETEST;
	}

	/**
	 * instance field storing {@code true} if the name test has a prefix; {@code false} otherwise
	 */
	private final boolean hasPrefix;
	/**
	 * instance field storing the name test prefix
	 */
	private final String prefix;
	/**
	 * instance field storing the name test prefix
	 */
	private final String suffix;
	/**
	 * instance field storing {@code true} if the suffix is a wildcard; {@code false} otherwise
	 */
	private final boolean suffixIsWildcard;
	
	/**
	 * namespace delimiter
	 */
	public final static String NAMESPACEDELIMITER = ":";
	
	/**
	 * prebuilt object containing the unbounded wildcard
	 */
	public final static XPathNameTest WILDCARD = new XPathNameTest("*",true);

}