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
 */

public final class SyncHeader {

   // instance fields
   
		private FrameWorkBase	T;						// base storage 
   	private int        	nbr_details;  // number of details in array
   
		private SyncHeader current; // current table addr		
	  private int loc;						// location of data within table
	
   	private SyncHeader 	next;					// next in list
   	private SyncHeader	prior;				// prior pointer
   
   	private SyncDetail[]	details;      // detail array

/**
 * 
 *
 */   

public SyncHeader(FrameWorkBase Ty, 
									SyncHeader last,
									int base) {
   
   // Base storage
   T = Ty;
   
   // total number of details 
	  nbr_details  = base;
	  
	  // chain
	  next 	= null;
	  prior = last;
	  
	  // get the initial array of details
	  details = new SyncDetail[nbr_details];
							  
	  // initialize the detail entries
	  for (int i = 0; i < nbr_details; i++) {

					// new detail entry
		      details[i] = new SyncDetail();

	  } // end-for                    
	  
	  // set the first entry as unusable
		details[0].setFirst();
				 
} // end-constructor
/**
 * 
 * @return Object
 * @param sr_nbr int
 */
public Object getInput (int sr_nbr) {	  
	  
	  // find the proper list
		if  (!locate(sr_nbr)) {

				// get out
				return null;

		} // endif			

	  // get the input
		return current.details[loc].getInput();
	  
} // end-method
/**
 * 
 * 
 */
 
public int getNbrEntries () {
		 
	  return nbr_details;
	  
} // end-method
/**
 * 
 * @return int
 * @param sr_nbr int
 */
public int getNbrQue (int sr_nbr) {	  
	  
	  // find the proper list
		if  (!locate(sr_nbr)) {

				// get out
				return -1;

		} // endif			
	  
	  // get the number of queues
		return current.details[loc].getNbr();
		
} // end-method
/**
 * 
 * @return Object[]
 * @param sr_nbr int
 */
public Object[] getOutput (int sr_nbr) {
	 	  
	  /// find the proper list
		if  (!locate(sr_nbr)) {

				// get out
				return null;

		} // endif			
	  
	  // the array of outputs
		return current.details[loc].getOutput();

} // end-method
/**
 * 
 * @return int
 * @param sr_nbr int
 */
public int getRemaining (int sr_nbr) {	  
	  
	  // find the proper list
		if  (!locate(sr_nbr)) {

				// get out
				return -1;

		} // endif			
	  
	  // get the number remaining
		return current.details[loc].getRemaining();
	  
} // end-method
/**
 * 
 * @return long
 * @param sr_nbr int
 */
public long getUni (int sr_nbr) {	  
	  
	 // find the proper list
		if  (!locate(sr_nbr)) {

				// get out
				return -1;

		} // endif			
	  
	  // get unique number
		return current.details[loc].getUni();
	  
} // end-method
/**
 * Locate the detail entry
 * @return boolean
 * @param sub int
 */
private boolean locate(int sub) {

		// base table
	  current = this;
	  
	  // which table
	  int tbl = (sub / nbr_details);
	  
	  // location of data within table
	  loc = (sub % nbr_details);
	  
	  // find which table
	  while (tbl > 0) {
	  		
				// next in chain
	  		current = current.next;

				// decrement number
	  		tbl--;
	  		
	  		// When invalid, exit
	  		if  (current == null) {
						
						// no good
	  				return false;

				} // endif	  		
		} // end-while	

		// good
		return true;

} // end-method
/**
 * 
 * @return int
 * @param sr_nbr int
 */
public int setDecrement (int sr_nbr) {
	  
	  // find the proper list
		if  (!locate(sr_nbr)) {

				// get out
				return -1;

		} // endif			
	  
	  // decrement the number remaining and return it
		return current.details[loc].setDecrement();
	  
} // end-method
/**
 * 
 * @param sr_nbr int
 */

public void setFree (int sr_nbr) {
	  
	  // find the proper list
		if  (!locate(sr_nbr)) {

				// get out
				return;

		} // endif				
	  
	  // free it
		current.details[loc].setFree();
	  
} // end-method
/**
 * 
 * 
 */

public int setNew (	Object input,
	                 	long	 uni,
	                  int 	 nbr_que, 
	                  int 	 waitime,
										Object requestor,
										int[] u_ecb ) {
	  
	 int i, j = 0;
	 
	 // base table	
	 current = this;
			 	 
	  // loop thru the details looking for an unused entry, (status = 0)
		while (true) {
	 			
	 			// find an available in this table
				for  (i = 0; i < nbr_details; i++) {
	     	
	 	   					// When found  			
	 	         	if  (current.details[i].getStatus() == 0) {	

	 	     					// add the entry 
	 								current.details[i].setUsed(	input, 
	 																						uni, 
	 	               														nbr_que,
	 	               														waitime,
																							requestor, 
																							u_ecb	);

									// return the table number + the entry number     									
	 	       				return ((j * nbr_details) + i);
										 	         				
	 	    			} // endif	 
				} // end-for	     
				
				// next table
				j++;
				
				// When at end:			 	   	
	 			if  (current.next == null) {

						// get out
						break;
				}
				else {						 	   	
						// bump to next table
	 					current = current.next;

	 			} // endif	
	  } // end-while 
	 
	  // get a new table
		current.next = new SyncHeader(T, current, nbr_details);
	
		// add the entry 
	 	current.next.details[0].setUsed(input,
 																		uni,
 	                 									nbr_que,
 	                 									waitime,	
																		requestor, 
																		u_ecb);
	 	                  			
		// return the table number +0     									
	 	return (j * nbr_details);
	 	         									
  	
} // end-method
/**
 *
 * 
 * @param sr_nbr int
 * @param nbr int
 */
public void setOutput (int sr_nbr, Object obj) {		
		
		// find the proper list
		if  (!locate(sr_nbr)) {

				// get out
				return;

		} // endif			

	 	// set output
		current.details[loc].setOutput(obj);
	  
} // end-method
/**
 * 
 * @param sr_nbr int
 */
public void setPosted (int sr_nbr) {		

		// find the proper list
		if  (!locate(sr_nbr)) {

				// get out
				return;

		} // endif			

		// set to posted
		current.details[loc].setPosted();
	  
} // end-method
} // end-class
