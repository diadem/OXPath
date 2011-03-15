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
package uk.ac.ox.comlab.oxpath.placeRankDemo;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for holding post code information
 * @author AndrewJSel
 *
 */
public class PostCode {
	
	public PostCode(String in) throws IOException {
		String temp = in.trim();
		if (!temp.contains("GIR 0AA") || !temp.contains("GIR0AA")) {//only postcode that doesn't adhere to regex below
			Pattern postcode = Pattern.compile("[a-zA-Z][a-zA-Z]?\\d(\\d|[a-zA-Z])?[ ]?\\d[a-zA-Z][a-zA-Z]");
			Matcher match = postcode.matcher(temp);
			boolean valid = match.matches();
			if (!valid) throw new IOException(temp + " is not a valid postcode!");
		}
		if (temp.charAt(temp.length()-4)==' ') this.postcode=temp;//if space is there, pass on; if not, add it
		else this.postcode = temp.substring(0, temp.length()-3) + " " + temp.substring(temp.length()-3);	
	}
	
	/**
	 * Returns postcode (identical to toString() method)
	 * @return postcode as <tt>String</tt> object
	 */
	public String getPostCode() {
		return postcode;
	}
	
	/**
	 * Simply prints postcode
	 * @return postcode as <tt>String</tt> object
	 */
	@Override
	public String toString() {
		return this.postcode;
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((postcode == null) ? 0 : postcode.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof PostCode)) {
			return false;
		}
		PostCode other = (PostCode) obj;
		if (postcode == null) {
			if (other.postcode != null) {
				return false;
			}
		} else if (!postcode.equals(other.postcode)) {
			return false;
		}
		return true;
	}
	

	/**
	 * Instance field for postcode
	 */
	private String postcode;
}
