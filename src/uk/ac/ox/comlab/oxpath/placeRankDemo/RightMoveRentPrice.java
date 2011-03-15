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
package uk.ac.ox.comlab.oxpath.placeRankDemo;

public enum RightMoveRentPrice {
	NOMAX("No max"),
	PCM100("100 PCM"),
	PCM200("200 PCM"),
	PCM300("300 PCM"),
	PCM400("400 PCM"),
	PCM500("500 PCM"),
	PCM600("600 PCM"),
	PCM700("700 PCM"),
	PCM800("800 PCM"),
	PCM900("900 PCM"),
	PCM1000("1,000 PCM"),
	PCM1250("1,250 PCM"),
	PCM1500("1,500 PCM"),
	PCM1750("1,750 PCM"),
	PCM2000("2,000 PCM"),
	PCM2500("2,500 PCM"),
	PCM3000("3,000 PCM"),
	PCM3500("3,500 PCM"),
	PCM4000("4,000 PCM"),
	PCM4500("4,500 PCM"),
	PCM5000("5,000 PCM"),
	PCM6000("6,000 PCM"),
	PCM7000("7,000 PCM"),
	PCM8000("8,000 PCM"),
	PCM9000("9,000 PCM"),
	PCM10000("10,000 PCM"),
	PCM15000("15,000 PCM"),
	PCM20000("20,000 PCM"),
	PCM30000("30,000 PCM");
	
	/**
	 * Constructor for each enum instance
	 * @param iValue value for the instance
	 */
	private RightMoveRentPrice(String iValue) {
		this.value=iValue;
	}
	
	/**
	 * Returns <tt>String</tt> value of each object
	 * @return <tt>String</tt> value of each object
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * instance field storing <tt>String</tt> representation of instance
	 */
	String value;
}
