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
 * This is the Communication Class used by Clients.  This
 *  Class is not used by the Server.  You may make any changes 
 *  for your installation.  
 *
 * Constructor() is for use by all client code.  
 *
 */

import java.rmi.ConnectException;
import java.rmi.Naming;
 
public class SvrComm {                             
  
  // used internally  
  private static FrameWorkInterface Ti = null;
  

/**
 *
 * This constructor is used by the Client applications.
 *
 */

public SvrComm() {

  /*
    // new security manager for RMI
    try {
       System.setSecurityManager( new RMISecurityManager());
    } // end-try
      
    catch (SecurityException e) {
      
        // put your code here
        System.out.println("Error= " + e);
        System.exit(1);
        
    } // end-catch
  */  

    // try to connect this many times
    int count = 10;
   
    // keep trying the conection 
    while  (count > 0) {
  
        try { 
            // rmi naming look up
        	String url = "//localhost:8808/SAMPLE-SERVER";
            Ti = (FrameWorkInterface)     Naming.lookup(url);          
                       
            // got one
            break;
                              
        } // end-try
   
        catch(ConnectException e) {
      
            // add your code here  
            count--;   
            //
        
        } // end-catch
  
        catch(Exception e) {
      
            // add your code here
            //e.printStackTrace(System.out); 
            System.out.println("RMI Naming.lookup Error: " + e); 
        
            // no good
            break;
        
        } // end-catch        
      } // end-while
    
} // end-constructor                               

/**
 *
 * This is the Shut Down Request method for client applications.
 *
 *
 *
 * @return java.lang.String 
 */
 
public String shutRequest() {
   
   // When the connection failed
  if  (Ti == null) {
  
    // all done
    return null;
    
  } // endif
  
  try {     
    // over, returning a string
    return (Ti.shutRequest());
    
  } // end-try
  
  catch(Exception e) {
    // add your code here
    //e.printStackTrace(System.out); 
    System.out.println("shutRequest RMI Error: " + e); 
        
    // no good
    return null;
        
  } // end-catch              
   
} // end-method


/**
 * 
 * @param fw FrameWorkInterface
 */

public SvrComm(FrameWorkInterface fw) {

  // save remote object
  Ti = fw;

} // end-constructor

/**
 *
 * This is the Asynchronous Request method for client applications.
 *
 * @param TP FrameWorkParm
 * @return Object
 * 
 */
 
public Object[] asyncRequest(FrameWorkParm TP) {  

  // When the connection failed
  if  (Ti == null) {
  
    // all done
    return null;
    
  } // endif           
            
  try {     
    // over, returning an object
    return (Ti.asyncRequest(TP));
     
  } // end-try    
  
  catch(Exception e) {
    // add your code here
    //e.printStackTrace(System.out); 
    System.out.println("asyncRequest RMI Error: " + e); 
        
    // no good
    return null;
        
  } // end-catch  
         
} // end-method

/**
 *
 * This is the Request method to get a list of thread data for a Queue
 *
 * @param name String
 * @return String[]
 * 
 */
 
public String[] fetchThreads(String name) {  

  // When the connection failed
  if  (Ti == null) {
  
    // all done
    return null;
    
  } // endif           
            
  try {     
    // over, returning an object
    return (Ti.fetchThreads(name));
     
  } // end-try    
  
  catch(Exception e) {
    // add your code here
    //e.printStackTrace(System.out); 
    System.out.println("fetchThreads RMI Error: " + e); 
        
    // no good
    return null;
        
  } // end-catch  
         
} // end-method

/**
 *
 * This is the Synchronous Request method for client applications.
 *
 * @param TP FrameWorkParm
 * @return Object[]
 * 
 */
 
public Object[] syncRequest(FrameWorkParm TP) {
   
  // When the connection failed
  if  (Ti == null) {
  
    // all done
    return null;
    
  } // endif
  
  try {
    // over, returning an object[]
    return (Ti.syncRequest(TP));  
  
  } // end-try
  
  catch(Exception e) {
    // add your code here
    e.printStackTrace(System.out); 
    System.out.println("syncRequest RMI Error: " + e); 
        
    // no good
    return null;
        
  } // end-catch      
  
} // end-method
} // end-class
