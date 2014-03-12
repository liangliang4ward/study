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

public final class AsyncHeader {
   
		private FrameWorkBase	T;		// base storage

		private int nbr_details;  // number of details in array
   
	 	private AsyncHeader current; // current table addr		
	  private int loc;						// location of data within table

  	private AsyncHeader next;	// next table addr
   	private AsyncHeader prior;	// prior table addr
   
   	private AsyncDetail[] details;	// detail array
   

/**
 *
 * 
 */
	 
public AsyncHeader(	FrameWorkBase Ty, 
										AsyncHeader last,
										int base) {
   
		T           = Ty;		// base storage
		nbr_details	= base; // total number of details
  	next				= null; // no next pointer yet
  	prior				= last;	// prior table 
	    
  	// get the array of details
  	details = new AsyncDetail[nbr_details];
						
	 	// initialize the detail entries
	 	for (int i = 0; i < nbr_details; i++) {

	  	   details[i] = new AsyncDetail(T);

	  } // end-for                    
		
		// 1st is not available
		details[0].setFirst();
						 
} // end-constructor
/**
 * 
 * @param ar_nbr int
 * 
 */

public void freeEntry (int ar_nbr) {
		
		// find the proper list
		if  (!locate(ar_nbr)) {

				// get out
				return;

		} // endif		
	  			
	  // free it 
		current.details[loc].freeEntry();
	  
} // end-method
/**
 * 
 * @return QueueHeader
 * @param ar_nbr int
 */
public QueueHeader getAgent (int ar_nbr) {	  
	  
	  // find the proper list
		if  (!locate(ar_nbr)) {

				// get out
				return null;

		} // endif			  			
	  
	  // get the Output Agent Queue
		return current.details[loc].getAgent();
	  
} // end-method
/**
 *
 * @return Object
 * @param ar_nbr int  
 */

public Object getInput (int ar_nbr)  {
		
		// find the proper list
		if  (!locate(ar_nbr)) {

				// get out
				return null;

		} // endif			  	
	  	
		// get the input number
		return current.details[loc].getInput();
	 
} // end-method	 
/**
 * 
 * @return long
 * @param ar_nbr int
 */
public long getName (int ar_nbr ) {	  
	  
	  // find the proper list
		if  (!locate(ar_nbr)) {

				// get out
				return -1;

		} // endif			  		
	  	
		// get the name
		return current.details[loc].getName();
	  
} // end-method
/**
 * 
 * @return int
 * @param ar_nbr int
 */
public int getNextOut (int ar_nbr) {			
	  
		// find the proper list
		if  (!locate(ar_nbr)) {

				// get out
				return -1;

		} // endif			  	
	  	
		// pass the next avail out # to it
		return current.details[loc].getNextOut();
	  
} // end-method
/**
 * 
 * @return int
 * @param ar_nbr int
 */
public int getNextUsed (int ar_nbr) {
		
		
		// table
	  current = this;
	  
	  // starting from first
	  int here = 0;
	  
	  // which table
	  int tbl = (ar_nbr / nbr_details);
	  
	  // location of data within table
	  loc = (ar_nbr % nbr_details);
	  
	  // verify starting location
	  if	((loc < 0) ||
	  		 (loc > nbr_details)) {
	  	  return -1;
	  } // endif
	  
	  // When over by one, use next table
	  if	(loc == nbr_details) {
	  	  loc = 0;
	  	  tbl++;
	  } // endif
	  
	  // find which table
	  while (tbl > 0) {
	  		
	  		current = current.next;
	  		tbl--;
	  		here++;
	  				
	  		// When invalid, exit
	  		if  (current == null) {
	  				return -1;
				} // endif
	  		
		} // end-while		
	  
	  // do all the tables from here forward
	  while (current != null) {
	  
	  		// look for the next used entry from here 
				for  (int i = loc; i < nbr_details; i++) {
			
								// When the entry is used, return the position
								if  (current.details[i].isAlive()) {

										// found here
										return ((here * nbr_details) + i);

	  						} //endif
	  		} // end-for					    	    
	  		
	  		// next table
	  		current = current.next;
	  		here++;
	  		loc = 0;
	  		
		} // end-while		  
			
		// none found  
	  return -1;
	  
} // end-method
/**
 * 
 * @return Object[]
 * @param ar_nbr int
 */
public Object[] getOutput (int ar_nbr) {		
		
		// find the proper list
		if  (!locate(ar_nbr)) {

				// get out
				return null;

		} // endif			  	
	  	
		// pass back the outputs 
		return current.details[loc].getOutput();
	  
} // end-method
/**
 * 
 * @return int
 * @param ar_nbr int
 */
public int getRemaining (int ar_nbr) {
		  
	  // find the proper list
		if  (!locate(ar_nbr)) {

				// get out
				return -1;

		} // endif			  	
	  	
		// pass the #remaining back
		return current.details[loc].getRemaining();
	  
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
 * @param ar_nbr int
 * @param out int
 */
public int setDecrement (int ar_nbr) {	  
	  
		// find the proper list
		if  (!locate(ar_nbr)) {

				// get out
				return -1;

		} // endif			  
	  
	  // decrement used and pass remaining back
		return current.details[loc].setDecrement();
	  
} // end-method
/**
 * 
 * @return int
 */
 
public int setNew (	long entered,
	                 	long 	name,
	                  int  	nbr_que,
	                  Object input,
	                  QueueHeader  	out_agent,
	                  int  	function) {
		
		int i, j = 0;
		
		// base table
		current = this;
			 	 
	  // loop thru the details looking for an unused entry, (status = 0)
	  while (current != null) {
	 			
	 			// find an available in this table
				for  (i = 0; i < nbr_details; i++) {
	     	
	 	   					// When found  			
	 	         	if  (current.details[i].getStatus() == 0) {	 	     	     
	 	         				
	 	     					// add the entry    				
	 	         			current.details[i].setUsed(	entered,
	  	             														name,
	               															input,
	               															out_agent,
	               															function,
	               															nbr_que);
	                     									 
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
						// bump to next list
 						current = current.next;

	 			} // endif		   	 
	  } // end-while 
	 
	  // get a new table
	  current.next = new AsyncHeader(T, current, nbr_details);
		
		// add the request to the first entry     				
	 	current.next.details[0].setUsed(entered,
			  														name,
			   														input,
			   														out_agent,
			   														function,
			   														nbr_que);
	                     									 
	  // return the table number      									
	 	return (j * nbr_details); 
	 	         					
  	
} // end-method
/**
 * 
 * @param ar_nbr int
 * @param out Object
 */
public void setOutput (int ar_nbr, Object out) {
	  
		// find the proper list
		if  (!locate(ar_nbr)) {

				// get out
				return;

		} // endif			  	
	  
	  // pass the output to it
		current.details[loc].setOutput(out);
	  
} // end-method
} // end-class
