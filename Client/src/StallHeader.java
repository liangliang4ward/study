  
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
  *
  *
  */
  
public final class StallHeader {

    // instance fields
    
    private FrameWorkBase T;      // base storage   
    private int     nbr_details;  // number of details in array
   
    private StallHeader current; // current table addr    
    private int loc;            // location of data within table
   
    private StallHeader   next;     // next in list
    private StallHeader   prior;    // prior pointer

    private StallDetail[] details;  // detail array 

/**
 * 
 * 
 */
 
public int addEntry (int name, long at_name, int why) { 
    
    int i;
    
    // base table
    current = this;
         
    // loop thru the details looking for a matching entry
    while (current != null) {
                
        // loop thru the details looking for an existing entry
        for  (i = 0; i < nbr_details; i++) {
      
              // When already here, get out
              if  (current.details[i].matchName(name, at_name)) {
                  
                  // bye
                  return 1;

              } // endif
        } // endfor 
        
        // next table
        current = current.next;
        
    } // end-while        
    
    // first table
    current = this;
         
    // loop thru the details looking for an unused entry, (status = 0)
    while (current != null) {
    
        // loop until the first available     
        for  (i = 0; i < nbr_details; i++) {
                
              // When the status is available
              if  (current.details[i].getStatus() == 0) {              
                      
                  // insert a new entry
                  current.details[i].setUsed(name, at_name, why);
                    
                  // all done
                  return 0;
                    
              } // endif
        } // end-for       
        
        // next table
        if  (current.next == null) {

            // no next table
            break;
        }
        else {                  
            // set next table
            current = current.next;     

        } // endif  
    } // end-while 
    
    
    // get a new table
    current.next = new StallHeader(T, current, nbr_details);
        
    // store the entry
    current.next.details[0].setUsed(name, at_name, why);  

    // good
    return 0;
    
} // end-method

/**
 * 
 * @return int
 */
 
public int getNbrEntries ( ) {
  
    return nbr_details;
    
} // end-method      

/**
 * *
 *
 */
   
public StallHeader( FrameWorkBase Ty, 
                    StallHeader last,
                    int base) {
   
    T            = Ty;    // base storage
    nbr_details  = base; // total number of details
    
    next  = null; // no next pointer
    prior = last; // prior table
   
       
    // get the initial array of details
    details = new StallDetail[nbr_details];
            
    // initialize the detail entries
    for  (int i = 0; i < nbr_details; i++) {

          // get a new detail           
          details[i] = new StallDetail();

    } // end-for                    
         
} // end-constructor   

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
    
    // verify location
    if  ((loc < 0) ||
         (loc >= nbr_details)) {

        // no good
        return false;

    } // endif
    
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
} // end-class
