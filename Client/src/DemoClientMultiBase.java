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
 * Multiple test, base storage for all threads.
 *
 */

public final class DemoClientMultiBase {  
    
    // the list of data for all threads.  
    //  1st array is number of threads, (placeholder for the second array.)
    //  2nd array is three integers:
    //      0 - the status, 0 = continuing
    //                       1 = done
    //      1 - return code
    //      2 - number of times used
    
    private int[][]   tmt_list;
        
    // number of threads 
    private int nbr_in_list = 0;
    
    // shut down mode, 0=no   
    private int shutdown    = 0;         

/**
 * multi test constructor:
 *    Initialize the base integer[][] array.
 *    Start a temporary thread that starts each Client thread.
 *
 * @param nbr int number of threads to start
 *
 */
 
public DemoClientMultiBase (int nbr) {    
    
    // total number of threads
    nbr_in_list = nbr;
    
    // get a new list
    // 1st array is number of threads
    // 2nd is always 3
    tmt_list = new int[nbr][3];

    // initialize the list
    for  (int i = 0; i < nbr; i++) {
            
          // status, rc, times used all zero
          tmt_list[i][0] = 0;
          tmt_list[i][1] = 0;
          tmt_list[i][2] = 0;

    } // end-for

    // start the temporary thread.  This thread then starts the threads
    //   for the processing
    //   Without this temporary thread, the window, when added, would appear to hang. 
    new DemoClientMultiInstant(this, nbr).start();
    
} // end-constructor
/**
 * 
 *
 * @return int[][]
 * 
 */
public int[][] getList () {   
        
    return tmt_list;
        
} // end-method
/**
 * 
 *
 * @return boolean
 */
public boolean getShutdown ( ) {    
    
    // the shut down status
    if  (shutdown != 0) {
    
        // shut down
        return true;
          
    } // endif
        
    // not shut down    
    return false;
        
} // end-method

/**
 *  *
 *  Using volatile on the definition or syncronizing the method is at your discretion. 
 *
 * @param who int 
 *
 */
 
public synchronized void setDone (int who) {    
    
    // When the index is invalid, exit
    if  ((who < 0) ||
         (who >= nbr_in_list)) {
          
        return;    
    } // endif
    
    // status is done
    tmt_list[who][0] = 1; 
    
} // end-method
/**
 * 
 * 
 */
 
public void setShutdown ( ) {   
    
    // set the shut down status
    shutdown = 1;
        
    return;
        
} // end-method
/**
 * 
 *
 * Update the return code,  (second integer in second array), 
 *   and        times used, (third integer in second array).
 *
 *  Using volatile on the array definition or syncronizing the method is at your discretion. 
 *
 * @param who int
 * @param rc int
 * @param times int
 * 
 */
 
public synchronized void setUpdate (int who, int rc, int times) {   
    
    // When the index is invalid, exit
    if  ((who < 0) ||
         (who >= nbr_in_list)) {
          
        return;    
    } // endif
    
    // save the return code
    tmt_list[who][1] = rc;
    
    // save the number of times used
    tmt_list[who][2] = times;
    
} // end-method
} // end-class
