/* 
 * Copyright (C) 2001 Edward Harned <ed@coopsoft.com>
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

public final class AsyncDetail {

		// instance fields
   
		private FrameWorkBase T;	   // base storage
		private long  entered;       // time entered
  	private long  gen_name;      // name generated
		private Object input;        // pointer to input
  	private QueueHeader out_agent;  // pointer to output agent name
  	private int   function;      // pointer to function name
  	private int   nbr_que;       // number of queue's in this function
  	private int   nbr_remaining; // remaining unprocessed
  	private int   status;        // 0 = available, 1 = busy
  	private int   next_output;   // next output subscript, -1 initially
  	private Object[] output;     // output array     
   

/**
 *
 *  
 */

public AsyncDetail(FrameWorkBase Ty) {

		// base 
		T = Ty;
   
	  entered       	= 0;    // time entered    
	  gen_name      	= 0;    // generated name
	  input         	= null;   // input name pointer
	  out_agent     	= null;   // output agent name pointer
	  function      	= -1;    // function pointer
	  nbr_que       	= 0;    // number of queues in funcion
	  nbr_remaining		= 0;    // remaining unprocessed
	  status        	= 0;    // available
	  next_output   	= 0;   // next output subscript
	  output        	= null; // no output array 
				
} // end-constructor                            
/**
 *
 *
 *
 */
 
public void freeEntry () {
	  	  

		// When this is dead
		if  (status == 0) {

				// get out
				return;

		} // endif

		// do all the used output   
		for  (int i = 0; i < nbr_que; i++) {
					 	     	    		       
					// free the output
					output[i] = null;

		} // end-for	

		// free the entry
		setFree();
	 	 
} // end-method
/**
 * 
 * @return QueueHeader
 */
public QueueHeader getAgent ( ) {
	
		// give back agent
 		return out_agent;
	  
} // end-method
/**
 *
 *
 */

public long getEntered() {

		// return time entered
		return entered;
		 
} // end-method 
/**
 *
 *
 */
			  
public int getFunction() {
	
		// return function
		return function; 
		 
} // end-method      
/**
 * 
 * @return Object
 */

public Object getInput ( ) {
	 
		// return input
	 	return  input;
	 
} // end-method
/**
 *
 *
 */

public long getName() {
		
		// return name
		return gen_name;
		 
} // end-method
/**
 *
 *
 */
			  
public int getNbr() {
	
		// return number of queues
	  return nbr_que;
		 
} // end-method     
/**
 *
 */
			  
public int getNextOut() {
	
		// return next output number
	  return next_output;
		 
} // end-method
/**
 *
 *
 */
			  
public QueueHeader getOutAgent() {
	
	  return out_agent;  
		 
} // end-method
/**
 *
 *
 */
			  
public Object[] getOutput() {
	
		// return outputs
	  return output; 
		 
} // end-method
/**
 *
 *
 */
			  
public int getRemaining() {

		// When the entry is used
		if  (status > 0) {
	
	  		return nbr_remaining;  

		} // endif

		return -1;
		 
} // end-method  
/**
 *
 *
 */
			  
public int getStatus() {
	
	  return status;  
		 
} // end-method 
/**
 * 
 * @return boolean
 */

public boolean isAlive() {

		// When status is working
		if  (status == 1)  {

				// When all queues have finished 
				if  (nbr_remaining == 0) {

						// free the entry
						freeEntry();

						// say not alive
						return false;
				}
				else {
						// say is alive
						return true;

				} // endif
		} // endif

		// not alive
		return false;

} // end-method
/**
 * 
 * @return int
 */
public int setDecrement () {
	  
	  // number of remaining queue's
	  nbr_remaining--;
	  
	  return nbr_remaining;
	  	    
} // end-method
/**
 *
 *
 */ 

public void setFirst()  {
				  
		// status is unavailable for use
		status = -1;
   
} // end-method          
/**
 *
 *
 */ 

public void setFree()  {
				  
		entered       = 0;   // time entered
		gen_name      = 0;   // generated name
		input         = null;  // input pointer
		out_agent     = null;  // output agent queue name pointer
		function      = -1;   // function name
		nbr_que       = 0;   // number of queues
		nbr_remaining = 0;   // number remaining unprocessed
		status        = 0;   // status= 0-available, 1-busy
		next_output   = 0;  // next available output subscript

		// do all the used output   
		for  (int i = 0; (i < nbr_que && output[i] != null); i++) {

				// null
				output[i] = null;

		} // end-for

    output = null; // output array
	  
   
} // end-method          
/**
 * 
 * @param out Object
 */
public void setOutput (Object out) {
	  

		// When this entry is used
		if  (status > 0) {

			  // add the output to the array
		    output[next_output] = out;
				
				// next available position
				next_output++;

		} // endif
	  	    
} // end-method
/** 
 *
 *
 */

public void setUsed(long 	u_entered,
				            long 	u_name,
				           	Object u_input,
				           	QueueHeader	u_out_agent,
				           	int  	u_function,
						        int  	u_nbr_que) {
				  
		entered       	= u_entered;   // time entered
		gen_name      	= u_name;      // generated name
		input         	= u_input;     // input pointer
		out_agent     	= u_out_agent; // output agent queue name pointer
		function      	= u_function;  // function name
		nbr_que       	= u_nbr_que;   // number of queues
		nbr_remaining 	= nbr_que;     // number remaining unprocessed
		status        	= 1;           // status= 0-available, 1-busy 
		next_output   	= 0;          // next available output subscript

		// work
		int i;
	
		
		// new output array from each finishing queue
		output = new Object[nbr_que];   
	
		// invalid all the outputs
		for  (i = 0; i < nbr_que; i++) {

					output[i] = null;

		} // end-for	
   
} // end-method          
} // end-class
