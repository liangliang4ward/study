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
 */

public final class FrameWorkMain {

   // instance fields
   private int nbr_entries; // number of Area entries
   private int curr_sub;    // current location         

   private QueueHeader[] details;  // detail entries        
   
/**
 * 
 *
 */

public FrameWorkMain(int numb) {
  
    nbr_entries = numb;   // number of queue's in all
    curr_sub    = 0;      // current location 

    // new details        
    details = new QueueHeader[nbr_entries];
    
} // end-constructor

/**
 * 
 * @return int
 */
 
public int getNbrEntries ( ) {
  
    return nbr_entries;
    
} // end-method

/**
 *
 *
 */
           
public int isEqual(String i_name) {
   
    String s;
    int i, j = -1;
    
    // loop thru all the details looking for a match
    for  (i = 0; (j < 0 && i < nbr_entries); i++) {
      
          // When its the one, save subscript
          if  (details[i].getName().equals(i_name)) { 

              // found here
              j = i;

            } // end-if
    } // end-for      

    // return what was found
    return j;      
    
} // end-method                


/**
 *
 * @return Queue
 * @param que int
 */
 
public QueueHeader getArea (int que) {
    
    // When bad que number  
    if  ((que >= nbr_entries) ||
         (que < 0)) {

        // error
        return null;

    } // endif            
  
    return details[que];
    
} // end-method 

/**
 *
 *
 */

public int setNew(QueueHeader A) {
    
    // current position in array
    int i = curr_sub;
    
    // add the new area   
    details[curr_sub] = A;
    
    // next available position        
    curr_sub++;
       
    return i;           
   
} // end-method
} // end-class
