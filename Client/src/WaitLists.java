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

public final class WaitLists {
   
   // instance variables
   private FrameWorkBase  T; // pointer to base storage
   private WaitLists next;   // next pointer, null=none
   private WaitLists prior;  // prior pointer, null=1st    
   private int times_used;   // times this list was used
   private int nbr_entries;  // number of details in array
   private int overflow;     // overflows a wait list
   
   // detail entries
   private int[] details;    // array of detail entries          

/**
 * Compress, pop up, all in-use entries.
 * 
 * 
 */
 
private void compress ( ) {                                           
    
    int i, avail = -1, used = -1;
    int new_req;
    byte type;
    
    // find first available
    for  (i = 0; i < nbr_entries; i++) {
                   
          // When status is available 
          if  (details[i] == 0) {

              // set avail pointer
              avail = i;

              // get out
              break;

          } // endif           
    } // end-for 
    
    // When none avail or at end of table
    if  ((avail < 0) || ((avail + 1) >= nbr_entries)) {

        // go back
        return;

    } // endif          
    
    
    // find first used after first available
    for  (i = avail + 1; i < nbr_entries; i++) {
           
            // When status is busy, set used 
          if  (details[i] != 0) {

              // set where found
               used = i;

              // found one
              break;

          } // endif           
    } // end-for 
    
    // When none used, done
    if  (used < 0) {

        // go back
        return;

    } // endif          
    
    // *--- pop up all used entries ---*    

    // move used to the top of the list, and free the space
    while (true)  {

        // none used so far
        used = -1;
      
        // find first used after first available
        for  (i = avail + 1; i < nbr_entries; i++) {
                 
              // When status is busy, set used 
              if  (details[i] != 0) {

                  // set where found
                  used  = i;

                  // found one
                  break;

              } // endif           
        } // end-for
    
        // When none used, done
        if  (used < 0) {

            // go back
            return;

        } // endif          
    
                
        // make 'to' entry used
        details[avail] = details[used]; 
          
        // make 'from' entry available 
        details[used] = 0;
          
        // next avail is last used
        avail = used;
  
    } // end-while  
        
} // end-method

/**
 * 
 * @return int
 */
 
public int fetchFirstBusy ( ) {
    
    int sub = 0;

    // When the first entry in the table is busy, return the value,
    //   free the entry and compress the array.
    if  (details[0] != 0) {
          
        // get the first entry      
        sub = details[0];

        // free the entry            
        details[0] = 0;
     
        // compress the table    
        compress();
        
    } // endif 
    
    return sub;
    
} // end-method
/**
 *
 * @return int
 */

public int getAvail ( ) {                                              
    
    int i;
    
    // loop thru all the detail entries in this waitlist
    for  (i = 0; i < nbr_entries; i++) {
      
          // When status: 0=not busy, 1=busy 
          if  (details[i] == 0) {

              // found one
              return 0;

          } // endif
    } // end-for

    // none found
    return 1;
    
} // end-method                                                     

/**
 * 
 */
 
public int getTimesUsed ( ) {
    
    return times_used;
    
} // end-method
/**
 * 
 * @return int
 */

public int getWlBusy () {
    
    // return the number of entries that are busy
    
    int i, count = 0;
    
    // loop thru all the detail entries in this waitlist
    for  (i = 0; i < nbr_entries; i++) {
      
          // When status: 0=not busy, 1=busy 
          if  (details[i] != 0) {

              // incre count
              count++;

          } // endif
    } // end-for
    
    return count;
   
} // end-method
/**
 * 
 * @return int
 * @param req int
 * 
 */
 
public int matchEntry (int req) {
    
    
    // look thru all the entries  
    for  (int i = 0; i < nbr_entries; i++) {
                 
        // When the request matches
        if  (req == details[i]) {
                      
            // found it 
            return 1;

          } // endif 
      } // end-for              
     
    // not found 
    return 0;
           
} // end-method                                                      

/**
 * 
 * @return int
 * @param req int
 * @param type byte
 */

public int setNewWaitlist (int req, byte type ) {
    
    
    // locate the first available entry
    for  (int i = 0; i < nbr_entries; i++) {
              
          // When available
          if  (details[i] == 0) {

              // When type of request is async, use the complement
              if  (type == 1) {

                  // async reqeust is complement
                  details[i] = (req * -1);
              }
              else {
                  // sync request
                  details[i] = req; 

              } // end-if

              // bump count
              setTimesUsed();
                    
              // all done
              return 0;   
    
          } // endif
     } // end-for               
     
     // no available found
     return -1;
     
} // end-method                                                       

/**
 * 
 */
 
public void setTimesUsed ( ) {
    
    times_used++;
    
} // end-method
/**
 * Set the new number of entries in a wait list
 *   
 */
 
public void setWlEntries (int nbr) {
  
    nbr_entries = nbr;
    
} // end-method 

/*
 * 
 *
 */

public WaitLists(FrameWorkBase Ty, WaitLists old, int max, int entries) {
  
    T            = Ty;      // base storage  
    prior        = old;     // ptr passed 
    next         = null;    // initially next is none
    overflow     = 0;       // base variables
    times_used   = 0;       // +
    nbr_entries  = entries; // total details in array
    details      = new int[entries]; // get array for details
    
    // initialize the detail entries
    for (int i = 0; i < entries; i++) {

          // not used
          details[i] = 0;

    } // end-for                   
    
    // when more WailLists, do the next one
    if  ((max - 1) > 0) {

        // do the next one
        next = new WaitLists(T, this, (max - 1), nbr_entries);

    } // endif
    
} // end-constructor   

/**
 * 
 * @return WaitLists
 */
 
public WaitLists getNext () {
    
    return next;
    
} // end-method

/**
 * 
 */

public int getOverflow ( ) {
    
    return overflow;
    
} // end-method

/**
 * 
 */

public void setOverflow ( ) {
    
    overflow++;
    
} // end-method
} // end-class
