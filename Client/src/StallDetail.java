/* 
 *Copyright (C) 2001 Cooperative Software Systems, Inc.  <info@coopsoft.com>
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

public final class StallDetail {

   // instance fields
   
    private long  entered;       // time entered
    private long  at_name;       // Async Table generated name   
    private int   gen_name;      // name of Async Table entry
    private int   status;        // 0 = available, 1 = busy 
    private int   times_checked; // times checked     
    private int   failed_reason;  // why it is here 

/**
  * 
  *
  */

public StallDetail() {
   
    entered       = 0;    // time entered   
    status        = 0;    // available
    times_checked = 0;    // not checked
    gen_name      = -1;   // AR table name
    at_name       = 0;    // AR gen name
    failed_reason = 0;    // not failed
        
} // end-constructor

/**
  *
  *
  */
         
public long getAtName() {
    
    return at_name; 
     
} // end-method     

/**
 *
 *
 */
   
public StallDetail getDetail() {
    
    return this;
     
} // end-method            
/**
 *
 *
 */
         
public long getEntered() {
    
    return entered; 
     
} // end-method
/**
 *
 *
 */
         
public int getFailedReason() {
    
    return failed_reason;
     
} // end-method      
/**
 *
 *
 */
   
public int getName() {
    
    return gen_name;
     
} // end-method
/**
 *
 *
 */
         
public int getStatus() {
    
    return status;  
     
} // end-method          

/**
 * C
 * @param a_name int
 * @param a_at_name long
 */
 
public boolean matchName(int a_name, long a_at_name) {

  // When a match:
    if  ((a_name    == gen_name) &&
         (a_at_name == at_name)) {

        // say found
        return true;

    } // endif            

    // not found   
    return false;

} // end-method
/**
 *
 *
 */
         
public void setFailedReason(int why) {
    
    failed_reason = why;
     
} // end-method      
/**
 *
 *
 */    
   
public void setFree() {

    
    entered       = 0;   // no time
    gen_name      = -1;   // Async table name is null
    at_name       = 0;   // AT generated name             
    status        = 0;    // status is available         
    times_checked = 0;    // never checked by the monitor 
    failed_reason = 0;    // not failed            
   
} // end-method     
/**
 *
 *
 */
   
public void setUsed(int u_name, long u_at_name, int why) {

    
    // get the time in milliseconds
   entered = System.currentTimeMillis();
      
    gen_name      = u_name;   // Async table name             
    at_name       = u_at_name; // AT entry name
    status        = 1;        // status is used         
    times_checked = 0;          // never checked by the monitor
    failed_reason = why;      // why failed             
   
} // end-method        
} // end-class
