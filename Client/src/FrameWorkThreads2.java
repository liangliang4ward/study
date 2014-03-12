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
import java.util.*;

public class FrameWorkThreads2 {
    
    // indicators
    private int refresh_Result = 0;  
        
    // old value from the import
    private String O_que_name = null; 
    private String[] from     = null;
        
    // new values from the window
    private String  N_que_name  = null;
    
    // internal comm
    private SvrComm svr = null; 

/**
 * Constructor
 */

public FrameWorkThreads2 ( ) {

    // get the internal svr comm
    svr = new SvrComm();

} // end-constructor
/**
 * 
 * @return int
 */
 
public int getRefResult () {
  
    return refresh_Result;
    
} // end-method
/**
 * 
 */
public void refreshButton(TextField S_que_name,
                          java.awt.List S_list) {                                        
    
    // set the queue name
    O_que_name = S_que_name.getText();
    
    // over to Server
    from = (svr.fetchThreads(O_que_name)); 
        
    // When a commumication failure
    if  (from == null) {

        // error
        refresh_Result = 0;

        // done
        return;

    } // endif

    // remove all from list   
    S_list.removeAll();
    
    // When the que name was not found
    if  (from[0].compareTo("N287") == 0) {

        // error
        refresh_Result = 2;

        // done
        return;

    } // endif       
    
    // length of the string
    int len = from.length;
    
    // add each item to the list
    for  (int i = 0; i < len; i++) {

          // add to list
          S_list.add(from[i]); 

    } // end-for        
        
    // good return
    refresh_Result = 3;
    
} // end-method
} // end-class
