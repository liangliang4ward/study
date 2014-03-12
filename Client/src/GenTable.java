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

public final class GenTable {

   // instance fields
   private FrameWorkBase T;     // base storage
   private long   next_sync;    // next sync request number   
   private long   next_async;   // next async request number  


/**
 *
 * 
 */

public long getAsync() {

    return next_async;    
                      
} // end-method
/**
 *
 * 
 */

public long getSync() {

    return next_sync;    
                      
} // end-method  
/**
 *
 * 
 */

public synchronized long setAsync() {
    
    // the next available   
    return ++next_async;    
                      
} // end-method 
/**
 *
 * 
 */    

public synchronized long setSync() {
      
    // return the next available     
    return ++next_sync;    
                      
} // end-method          

/**
  *
  * 
  */

public GenTable(FrameWorkBase Ty) {
  
    T           = Ty; // base storage   
    next_sync   = 0;  // initialize                     
    next_async  = 0;  // initialize 
            
} // end-constructor
} // end-class
