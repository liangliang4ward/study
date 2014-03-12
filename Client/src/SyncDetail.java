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

/**
 *
 *
 */

public final class SyncDetail {

		// instance fields
		private long  uni_name;      // name of this entry
   	private long  entered;       // time entered
   	private Object[] output;        // output data array pointers
		private Object 	input;         // input area pointer, if any
   	private int   status;        // -1, not used, 0=available, 1=busy, 2=reset    
   	private int   nbr_que;       // total queue's in function
		private int   nbr_remaining; // remaining to be processed
		private int		next_output;   // next output in list	  
   	private int   wait_time;     // max wait time in seconds
		private Object requestor;		 // requestor obj to cancel wait
		private int[] pnp;					 // posted/not posted, 0 not posted, 1 is posted

/**
 *
 * 
 */

public SyncDetail() {
   
	  input	      	= null;  	// no input area
		output				= null; // no output pointer
	  uni_name			= 0;   	// no name of this entry
	  entered   		= 0;   	// time entered   
	  status     		= 0;   	// available               
	  nbr_que    		= 0;   	// total queue's in function
		next_output		= 0;    // no next output
		nbr_remaining	= 0;		// none remain to be processed
	  wait_time  		= 0;   	// max wait time in seconds
		requestor  		= null; // no requestor yet
		pnp 					= null;	// does not exist
					   
} // end-constructor   
/**
 *
 */
				 
public Object getInput() {
   	
   return input;  
	  
} // end-method      
/**
 *
 *
 */	
	              
public int getNbr() {
  	
   return nbr_que;
	  
} // end-method 
/**
 *
 *
 */	
	              
public int getNextOut() {
  	
   return next_output;
	  
} // end-method 
/**
 *
 *  
 */

public Object[] getOutput() {
   	
   return output;  
	  
} // end-method     
/**
 *
 *  
 */	

public int getRemaining() {
  	
   return nbr_remaining;
	  
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
 *  
 */

public long getUni() {
   	
   return uni_name;
	  
} // end-method    
/**
 *
 * 
 */

public int getWait() {
   	
   return wait_time;
	  
} // end-method      
/**
 * 
 * @return boolean
 */

public boolean isAlive() {

		// When status is working
		if  (status == 1)  {

				// say is alive
				return true;

		} // endif

		// not alive
		return false;

} // end-method
/**
 *
 *  
 */	

public int setDecrement() {
  	

		// When active
		if  (status > 0) {

				// decrement number remaining
				nbr_remaining--;	   

		} // endif

		return nbr_remaining;
	  
} // end-method 
/**
 * 
 *  
 */

public void setFirst ( ) {

		// this entry is the first and is not available
	  status = -1;  	
 
} // end-method
/**
 * 
 *  
 */

public void setFree ( ) {
	  
	  input      		= null;  	// no input area
	  output     		= null; // no output data  
	  uni_name   		= 0;   	// no name of this entry
	  nbr_remaining	= 0;   	// none remaining
	  entered    		= 0;   	// time entered   
	  status     		= 0;   	// available               
	  nbr_que    		= 0;   	// total queue's in function
	  wait_time  		= 0;   	// max wait time in seconds
		next_output		= 0;		// no next output
		requestor  		= null; // no requestor
		pnp						= null;	// no posted indicator
						  
} // end-method
/**
 * 
 * @param nbr Object
 *
 *  	
 */

public void setOutput (Object obj) {

		// When active
		if  (status > 0) {
		
				// add this entry 
			 	output[next_output] = obj;
			
				// bump next pointer
				next_output++;	
				
		} // endif
	  
} // end-method
/**
 *
 * pnp - is a pointer to an integer array of one item. 0 is not posted, 1 is posted.
 * requestor - is the Impl obj with waiting threads
 * 
 */

public void setPosted() {

		// When alive
		if  (requestor != null) {

				// get lock on RMI obj
				synchronized (requestor) {

						// is posted
						pnp[0] = 1;

						// wake up			
						requestor.notifyAll();

				} // end-sync
		} // endif

} // end-method
/**
 *
 *    for using an entry 
 */
 
public void setUsed(	Object u_input, 
			           			long u_uni,
			           			int  u_nbr_que,  
			           			int  u_wait,
											Object u_requestor,
											int[] u_pnp) {

		input      		= u_input;		// input pointer                
	  uni_name   		= u_uni;			// unique name                   
	  status     		= 1;					// is active                              
	  nbr_que    		= u_nbr_que;	// number of queues
		nbr_remaining = u_nbr_que;	// number remaining to be processed          
	  wait_time  		= u_wait;			// max seconds to wait
		requestor	 		= u_requestor;	// requestor obj
		next_output   = 0;         // next output position, invalid now		
		pnp 					= u_pnp; // post/not posted indicator

		// time entered 
		entered  = System.currentTimeMillis();    

		// new output array
		output = new Object[nbr_que];

		// invalid all the outputs
		for  (int i = 0; i < nbr_que; i++) {

					// not used
					output[i] = null;

		} // end-for		
					 
} // end-method    
} // end-class
