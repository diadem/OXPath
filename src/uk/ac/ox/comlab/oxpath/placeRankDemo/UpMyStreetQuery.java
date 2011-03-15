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

/**
 * @author AndrewJSel
 *
 */
public class UpMyStreetQuery implements SiteQuery {

	/**
	 * Basic Constructor for UpMyStreet Queries
	 * @param income 
	 * @param education
	 * @param housing
	 * @param children
	 */
	public UpMyStreetQuery(boolean income, boolean education, boolean housing, boolean children) {
		this.hasIncome = income;
		this.hasEducation = education;
		this.hasHousing = housing;
		this.hasChildren = children;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ox.comlab.oxpath.placeRankDemo.SiteQuery#getOXPath(java.lang.String)
	 */
	/**
	 * Returns OXPath expression formed from implicit parameter
	 * @param postcode postcode for query
	 * @return OXPath expression created by object
	 */
	@Override
	public String getOXPath(String postcode) {
		StringBuilder sb = new StringBuilder();
		sb.append("path={<http://www.upmystreet.com>/}/descendant::field()[1]/{\"OX1\"}/following::field()[1]/{click/}//*[@id#='neighbourhoodProfile']:<profile>");
		if (hasIncome) {
			sb.append("[*[.#='Family income']/following::*[1]:<income=string(.)>]");
		}
		if (hasEducation) {
			sb.append("[*[.#='Educated']/following::*[1]:<education=string(.)>]");
		}
		if (hasHousing) {
			sb.append("[*[.#='Housing']/following::*[1]:<housing=string(.)>]");
		}
		if (hasChildren) {
			sb.append("[*[.#='children']/following::*[1]:<children=string(.)>]");
		}
		return sb.toString();
	}

	//instance fields
	private boolean hasIncome;
	private boolean hasEducation;
	private boolean hasHousing;
	private boolean hasChildren;
}
