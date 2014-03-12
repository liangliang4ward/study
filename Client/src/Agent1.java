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

public class Agent1 
    implements DemoCall {

/**
 * 
 */

public Agent1() {
  
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

    try {

        // number of objects passed
        int nbr = 0;

        // the passed object array
        Object[] what = null;

        // arg is really an object[]        
        what = (Object[]) in; 

        // length of array
        nbr = what.length;

        // When none, bye
        if  (nbr < 1) {

            // all done here
            return null;

        } // endif

        // base data          
        String out = "Agent1 ==>";

        // concat all the passed strings  
        for  (int i = 0; i < nbr; i++) {

            // each string
            out = out.concat((String) what[i]);

        } // end-for
    
        // put out
        System.out.println(out);

    } // end-try
   
    catch (java.lang.Throwable e) {
          
        //e.printStackTrace(System.out);
        
        throw  e; 
    } // end-catch

    return null;  

} // end-method
} // end-class
