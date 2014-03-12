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

public final class FuncHeader {

	// instance fields
	private int nbr_func;     // number of functions
	private int curr_sub;     // used only for building tablerch
	 
	private FuncDetail[] details;  // detail entries 

/**
 * 
 *
 */ 
 
public FuncHeader(int numb) {
  
	// number of functions in all
	nbr_func   = numb; 

	// used for building detail table
	curr_sub   = 0;  
	
	// new details array  
	details = new FuncDetail[nbr_func];
	
	int i;

	// initialize all the details
	for  (i = 0; i < nbr_func; i++) {

		  // initialize entry
		  details[i] = new FuncDetail();

	} // end-for

} // end-constructor
/**
 * Get a function for a sync or async request
 *
 */
 
public FuncDetail getEntry(String name) {


		return lookSerialString(name);
	  
} // end-method
/**
 *
 *
 */
           
public String getName(int nbr) {

    // When invalid detail number, return null
    if  ((nbr < 0) || (nbr >= nbr_func)) {

        // none
        return null;

    } // endif      
    
    // name of detail
    return details[nbr].getName();
    
} // end-method    
/**
 *
 *
 */
 
public int getNbrQue(int nbr) {

    // When invalid detail number, return null
    if  ((nbr < 0) || (nbr >= nbr_func)) {

        // invalid
        return -1;

    } // endif      
    
    // return number of queues
    return details[nbr].getNbrQue();
    
} // end-method     
/**
 *
 *
 */
           
public FuncDetail getNextEntry(int nbr) {

    // When invalid detail number, return null
    if  ((nbr < 0) || (nbr >= nbr_func)) {

        // invalid
        return null;

    } // endif      
    
    // return detail obj
    return details[nbr];
    
} // end-method     
/**
 *
 *
 */
      
public int getNumb() {

    return nbr_func;     
                      
} // end-method    
/**
 *
 *
 */
           
public long getUsed(int nbr) {

    // When invalid detail number, return null
    if  ((nbr < 0) || (nbr >= nbr_func)) {

        // invalid
        return -1;

    } // endif      
    
    // return number used
    return details[nbr].getUsed();
    
} // end-method         
/**
 * 
 * @return FuncDetail
 * 
 */

private FuncDetail lookSerialString(String name) {


	// look for a matching string
	for  (int i = 0; i < nbr_func; i++) {

		  // When a match   
		  if  (details[i].isEqual(name)) {

			  // return the Function Detail
			  return details[i];                  

		  } // endif      
	} // end-for
	
	// not found
	return null;

} // end-method
/**
 * New entry 
 *
 */
   
public void setNew(String name,
				  QueueHeader agent,
				  int nbr_que,
				  QueueHeader[] Q) {
   
		// add a new entry with a Q table
		details[curr_sub].setNew(name,
								 agent,
								 curr_sub,
								 nbr_que,
								 Q);
	   
	// bump pointer
	curr_sub++;	
	
} // end-method    
} // end-class
