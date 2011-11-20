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
 * Package containing core OXPath functionality
 */
package uk.ac.ox.comlab.diadem.oxpath.core;

/**
 * Enum encoding whether a <tt>position()</tt> and <tt>last()</tt> function needs to be handled by an AST node
 * @author AndrewJSel
 *
 */
public enum PositionFuncEnum {

	/**
	 * both position functions occur 
	 */
	BOTH {
		/**
		 * Combines two inputs (basically an OR function for two inputs - <tt>position()</tt> and <tt>last()</tt> boolean flags
		 * @param other the other enum value
		 * @return a representation of (this.position || other.position) and (this.last || other.last)
		 */
		public PositionFuncEnum combine(PositionFuncEnum other) {
			switch (other) {
			case BOTH:
			case POSITION:
			case LAST:
			case NEITHER:
			default:
				return BOTH;
			}
		}
	},

	/**
	 * only <tt>position()</tt> occurs
	 */
	POSITION {
		/**
		 * Combines two inputs (basically an OR function for two inputs - <tt>position()</tt> and <tt>last()</tt> boolean flags
		 * @param other the other enum value
		 * @return a representation of (this.position || other.position) and (this.last || other.last)
		 */
		public PositionFuncEnum combine(PositionFuncEnum other) {
			switch (other) {
			case BOTH:
			case LAST:
				return BOTH;
			case POSITION:
			case NEITHER:
			default:
				return POSITION;
			}
		}
	},

	/**
	 * only <tt>last()</tt> occurs
	 */
	LAST {
		/**
		 * Combines two inputs (basically an OR function for two inputs - <tt>position()</tt> and <tt>last()</tt> boolean flags
		 * @param other the other enum value
		 * @return a representation of (this.position || other.position) and (this.last || other.last)
		 */
		public PositionFuncEnum combine(PositionFuncEnum other) {
			switch (other) {
			case BOTH:
			case POSITION:
				return BOTH;
			case LAST:
			case NEITHER:
			default:
				return LAST;
			}
		}
	}, 

	/**
	 * neither position functions
	 */
	NEITHER {
		/**
		 * Combines two inputs (basically an OR function for two inputs - <tt>position()</tt> and <tt>last()</tt> boolean flags
		 * @param other the other enum value
		 * @return a representation of (this.position || other.position) and (this.last || other.last)
		 */
		public PositionFuncEnum combine(PositionFuncEnum other) {
			switch (other) {
			case BOTH:
				return BOTH;
			case POSITION:
				return POSITION;
			case LAST:
				return LAST;
			case NEITHER:
			default:
				return NEITHER;
			}
		}
	};

	/**
	 * Combines two inputs (basically an OR function for two inputs - <tt>position()</tt> and <tt>last()</tt> boolean flags
	 * @param other the other enum value
	 * @return a representation of (this.position || other.position) and (this.last || other.last)
	 */
	public abstract PositionFuncEnum combine(PositionFuncEnum other);

}
