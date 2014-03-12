/* 
 * Copyright (C) 2001 Cooperative Software Systems, Inc.  <info@coopsoft.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You received a copy of the GNU General Public License
 * along with this software. For more information on GNU, see
 * http://www.gnu.org or write to: the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

/**
 * This is the parameter passed to the Server from Clients.
 * 
 */

public final class FrameWorkParm implements java.io.Serializable {

	// instance fields
	private Object input; // input data for your invoked method
	private String func_name; // Function name
	private int wait_time; // maximum time, in seconds, to wait for a reply when
							// using a Synchronous Request
	private int priority; // priority of the request

	/**
	 * This constructor loads the private instance fields from the passed
	 * parameters.
	 * 
	 * @param c_in
	 *            java.lang.String
	 * @param c_func
	 *            java.lang.String
	 * @param c_interval
	 *            int
	 * @param c_pri
	 *            int
	 * 
	 */

	public FrameWorkParm(Object c_in, // data for your invoked method
			String c_func, // Function name
			int c_interval, // max wait time, in seconds
			int c_pri) { // priority of request

		// parameters to instance fields
		input = c_in;
		func_name = c_func;
		wait_time = c_interval;
		priority = c_pri;

	} // end-constructor

	/**
	 * 
	 * 
	 * @return java.lang.String
	 */

	public String getFuncname() {

		return func_name;

	} // end-method

	/**
	 * 
	 * 
	 * @return Object
	 */

	public Object getInput() {

		return input;

	} // end-method

	/**
	 * 
	 * 
	 * @return int
	 */

	public int getPriority() {

		return priority;

	} // end-method

	/**
	 * 
	 * 
	 * @return int
	 */

	public int getTime() {

		return wait_time;

	} // end-method

	/**
	 * 
	 * 
	 * @param S
	 *            java.lang.String The function name to set.
	 */

	public void setFuncname(java.lang.String S) {

		func_name = S;

	} // end-method

	/**
	 * 
	 * 
	 * @param O
	 *            Object The input to set.
	 */

	public void setInput(Object O) {

		input = O;

	} // end-method

	/**
	 * 
	 * 
	 * @param S
	 *            java.lang.String The input to set.
	 */

	public void setInput(java.lang.String S) {

		input = S;

	} // end-method

	/**
	 * 
	 * 
	 * @param P
	 *            int The priority of the request to set.
	 */

	public void setPriority(int P) {

		priority = P;

	} // end-method

	/**
	 * 
	 * 
	 * @param T
	 *            int The maximum wait time to set.
	 */

	public void setTime(int T) {

		wait_time = T;

	} // end-method
} // end-class
