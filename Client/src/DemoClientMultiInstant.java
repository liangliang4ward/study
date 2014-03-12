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
 * Multiple test, thread iniatiator for all threads. 
 *
 */

public final class DemoClientMultiInstant 
        extends Thread {

    // the base storage
    DemoClientMultiBase base;

    // nbr of threads
    int nbr;

/**
 * This is the thread run method.
 */

public void run() {

    // instantiate a new thread
    // wait one half second
    for  (int i = 0; i < nbr; i++) {

          // instantiate a new thread: (Base storage, sequence nbr.) & start it
          new DemoClientMultiThread(base, i).start();

          // wait a half second to avoid initiation storm
          try {
              Thread.sleep(500);
          } // end-try

          catch (InterruptedException e) {
          } // end-catch  

    } // end-for  

    // end of thread    
    return;

} // end-method

/**
 * multi test thread initiator constructor:
 *
 *
 */
 
public DemoClientMultiInstant (DemoClientMultiBase p_base, int p_nbr) {

    // fields
    base = p_base;
    nbr  = p_nbr;
    
} // end-constructor
} // end-class
