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
 * Class for objects representing queries for Reed.co.uk
 * @author AndrewJSel
 *
 */
public class ReedQuery implements SiteQuery {

	public ReedQuery(String job, Double minSal, Double maxSal, int maxApp) {
		this.jobCategory = job;
		this.minSalary = minSal;
		this.maxSalary = maxSal;
		this.maxApplicants = maxApp;
	}
	
	public ReedQuery(String job, Double minSal, int maxApp) {
		this(job, minSal, 1000000.0, maxApp);
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.ox.comlab.oxpath.placeRankDemo.SiteQuery#getOXPath()
	 */
	/**
	 * Method for returning OXPath expression for this object, given a specific postcode
	 * @return OXPath expression from this object for the given postcode
	 */
	/**
	 * Method for returning OXPath expression for this object, given a specific postcode
	 * @return OXPath expression from this object for the given postcode
	 */
	@Override
	public String getOXPath(String postcode) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//instance fields
	/**
	 * Job category
	 */
	private String jobCategory = "";
	/**
	 * min salary
	 */
	private Double minSalary = 0.0;
	/**
	 * max salary; defaults to 1,000,000 GBP
	 */
	private Double maxSalary = 1000000.0;
	/**
	 * maximum applicants for the job
	 */
	private int maxApplicants = 10;
}
