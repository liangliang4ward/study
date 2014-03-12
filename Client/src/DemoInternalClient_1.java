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
 * Display, (println), the result of Function 1.
 * 
 */
 
public class DemoInternalClient_1 {
	

/**
 * 
 * 1. start the server
 * 2. send a sync request for Function_1
 * 3. wait 5 seconds
 * 4. shut down the server
 *
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) { 


// 1.
    // new server instance
    FrameWorkInternalServer impl = new FrameWorkInternalServer();

		// the server
		FrameWorkInterface fwi = null;  

		try {
		    // create the server
  		  fwi = impl.createServer();

		} catch (Throwable e) {

				System.out.println("caught a throwable=" + e);

		} // end-catch

// 2.
	
		// new string for FrameWork function
		String x = "DemoClient_1 passed object";

		// make obj
		Object pass = (Object) x;

		// return array
		Object back[] = null;
	 
		// form a parameter for FrameWork	 
		FrameWorkParm TP = new FrameWorkParm(pass,	// data 
																	"F1", // function name
																	10, 					 // wait time
																	1);						 // priority
	 							
		try {
		    // do a sync request	 
				back = fwi.syncRequest(TP);

		} catch (Exception e) {

				System.out.println("caught an exception on sync request=" + e);

		} // end-catch
		
	
		// Should be an array
		if  (back == null)	{
						
				// say no good	
				System.out.println("Did not complete properly");

				// bye
				return;

		} // endif			
				
		// number of objects in array
		int nbr = back.length;

		// Display string
		String S = "";

		// concatenate all the strings
		for  (int i = 0; i < nbr; i++) {

				// must be a string
				if  ((back[i] != null) &&  
						 (back[i] instanceof String)) {

							// concat
							S = S.concat((String) back[i]);

				} // endif
		} // end-for
		
		// say what
		System.out.println(S);
	 	 
// 3.

    // wait 5 seconds
    try {
        Thread.sleep(5000);

    }
    catch (InterruptedException e) {}

// 4.

    // return string from a shut down
    String end = null;

    // shut down the server
    try {
        end = fwi.shutRequest();
    }
    catch (Exception e) {
				System.out.println("caught an exception on shut request=" + e);

		} // end-catch

    // print the result.     
    System.out.println("Shut request returned message=" + end);
	 
} // end-method
}
