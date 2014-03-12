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
 * Display, (println), the result of Function.
 * 
 */

public class DemoClient_7 {
  

/**
 * Request processing of FrameWork Function 7. 
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {
      
    
    // new string for FrameWork function
    String x = "DemoClient_7 passed object";

    // make obj
    Object pass = (Object) x;

    // return array
    Object back[] = null;
   
    // form a parameter for FrameWork  
    FrameWorkParm TP = new FrameWorkParm(pass,         // data 
                                  "F4", // function name
                                  10,            // wait time
                                  1);            // priority
                                        
    // do a sync request   
    back = new SvrComm().syncRequest(TP);
  
    // Should be an array
    if  (back == null)  {
            
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
    
    
    return;
   
} // end-method
}
