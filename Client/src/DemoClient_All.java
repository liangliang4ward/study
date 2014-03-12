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
 * Display, (println), the result of multiple Functions.
 * 
 */
 
public class DemoClient_All {
  

/**
 * Request processing of Functions 1 - 6. 
 * @param args java.lang.String[]
 */
 
public static void main(String args[]) {
   
    
    // new string for functions
    String x1 = "Passed in";

    // make obj
    Object pass1 = (Object) x1;

    // return array
    Object back[] = null;
    
    // form paramters for each of the functions 
    FrameWorkParm TP1 = new FrameWorkParm(pass1, "F1", 10, 1);
    FrameWorkParm TP2 = new FrameWorkParm(pass1, "F2", 10, 2);
    FrameWorkParm TP3 = new FrameWorkParm(pass1, "F3", 10, 3);
    FrameWorkParm TP4 = new FrameWorkParm(pass1, "F1", 10, 4);
    FrameWorkParm TP5 = new FrameWorkParm(pass1, "F2", 10, 5);
    FrameWorkParm TP6 = new FrameWorkParm(pass1, "F3", 10, 6);
   
    // get a new RMI methods object
    SvrComm TSC = new SvrComm();
         
    // do each function, printing the results 
    back = TSC.syncRequest(TP1);
  
    // Should be an array
    if  (back == null)  {
            
        // say no good  
        System.out.println("TP1 did not complete properly");
    }
    else {
        printBack(back);

    } // endif    

    back = TSC.syncRequest(TP2);
  
    // Should be an array
    if  (back == null)  {
            
        // say no good  
        System.out.println("TP2 did not complete properly");
    }
    else {
        printBack(back);

    } // endif
    
    back = TSC.syncRequest(TP3);
  
    // Should be an array
    if  (back == null)  {
            
        // say no good  
        System.out.println("TP3 did not complete properly");
    }
    else {
        printBack(back);

    } // endif    

    back = TSC.syncRequest(TP4);
  
    // Should be an array
    if  (back == null)  {
            
        // say no good  
        System.out.println("TP4 did not complete properly");
    }
    else {
        printBack(back);

    } // endif  

    back = TSC.asyncRequest(TP5);
  
    // Should be an array
    if  (back == null)  {
            
        // say no good  
        System.out.println("TP5 did not complete properly");
    }
    else {
        printBack(back);

    } // endif    

    back = TSC.asyncRequest(TP6);
  
    // Should be an array
    if  (back == null)  {
            
        // say no good  
        System.out.println("TP6 did not complete properly");
    }
    else {
        printBack(back);

    } // endif  
  
} // end-method
/**
 * 
 * @param pass java.lang.Object[]
 */
private static void printBack(Object[] pass) {


    // number of objects in array
    int nbr = pass.length;

    // Display string
    String S = "";

    // concatenate all the strings
    for  (int i = 0; i < nbr; i++) {

          // concat
          S = S.concat((String) pass[i]);

    } // end-for
    
    // say what
    System.out.println(S);

} // end-method
}
