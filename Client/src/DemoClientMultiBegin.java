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
import java.awt.*; 

public final class DemoClientMultiBegin {
    
    
    // the array [nbr of threads] [3 integers, see below] 
    //  1st is status, 0-working, 1-finished
    //  2nd is return code 
    //  3rd is number of times used  
    private int t3[][] = null;
    
    // number of threads
    private int nbr_in_list = 0; 
    
    // the result of the start button
    private int start_result = 0;
    
    // the base storage for the threads
    private DemoClientMultiBase tmt = null;  


/**
 * 
 * @param args java.lang.String[]
 */
public static void main(String[] args) {

      // how many threads to start
    int start_count = 1;

    // work
    int arg_count = 0;

    // how long to keep this going, in minutes
    int timer = 1;

    // number of args passed
    arg_count = args.length;

    // When none use 1
    if  (arg_count > 0) {

        // try to convert 
        try {
            // to an int
            start_count = Integer.parseInt(args[0]);

        } catch (NumberFormatException e) {}

        // When a second arg
        if (arg_count > 1) {
      
            // try to convert 
          try {
              // to an int
              timer = Integer.parseInt(args[1]);

          } catch (NumberFormatException e) {}

        } // endif
    } // endif

    // new instance
    DemoClientMultiBegin me = new DemoClientMultiBegin();

    // what happended
    System.out.println("Started " + start_count + " for " + timer + " minutes");

    // start it up
    me.startSession(start_count);

    // until the end
    long out = (long) (timer * 60) * 1000;

    try {     
      Thread.sleep(out);

    } // end-try
    
    catch (InterruptedException e) {
    } // end-catch

    // what happended
    System.out.println("Calling StopSession");

    // shut it down
    me.stopSession();

} // end-method

/**
 * 
 * start the process.
 */

public void startSession (int number) {
            
    // start it off 
    tmt = new DemoClientMultiBase(number);
    
} // end-method

/**
 * 
 * stop the process
 * 
 */
public void stopSession () {
    
    // When not started
    if  (tmt == null) {

        // get out of here
        return;

    } // endif
    
    // set the shut down indicator in the base storage
    tmt.setShutdown();
    
} // end-method
} // end-class
