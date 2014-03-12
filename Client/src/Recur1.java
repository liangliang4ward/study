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
 */

public class Recur1 
		implements DemoCall {

		// remote object
		FrameWorkInterface fsi = null;

/**
 * 
 */

public Recur1() {
	
} // end-constructor
/**
 * 
 * @return java.lang.Object
 * @param in java.lang.Object
 * @param fw FrameWorkInterface
 * @exception java.lang.Throwable Your exception
 */

public Object doWork(Object in, FrameWorkInterface fw) 
				throws  java.lang.Throwable {

		// return string
		String S = "(Recur1="; 

		try {
				// Should be a string passed
				if  (in instanceof String)	{
						
						// ok
						S = S + (String) in + ")";
				}
				else {
						// nothing passed							
						S = S + "nothing passed)";

				} // endif


				/*
		 			* The basic return string is finished.  Now, to demonstrate the
		 			*   recurrsive ability.  The method acts as a Client and
		 			*   calls the Server for F2 with a synchronous
		 			*   request.  The return string from this call is appended to the
		 			*   basic return string and all passes back to the original client.
					* 
					*/
	 
					// new string 
					String x = "From recursion";
					Object pass = (Object) x;
					Object back[] = null;
	 
					// form a parameter for Server
					FrameWorkParm TP = new FrameWorkParm(pass,				 // data 
	 																			"F2",   // function name
	 																			10, 					 // wait time
	 																			1);						 // priority
	 																			

					// do a sync request	 
					back = new SvrComm(fw).syncRequest(TP);
	
					// Should be an array
					if  (back == null)	{
						
							String Z = S + " Did not complete properly";
							return (Object) Z;
					} // endif			
				
					// number of objects in array
					int nbr = back.length;

					// concatenate all the strings
					for  (int i = 0; i < nbr; i++) {

								// When a good string
								if  (back[i] != null) {

										// concat
										S = S.concat((String) back[i]);

								} // endif
					} // end-for
		
	  			// return all values
	 				return (Object) S;

		} // end-try
	 
		catch (java.lang.Throwable e) {
					
				//e.printStackTrace(System.out);
				
				throw  e; 
		} // end-catch	

} // end-method
} // end-class
