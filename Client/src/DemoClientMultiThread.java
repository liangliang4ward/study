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
 *  For each thread in the test, create a new instance of this class.
 * The storage used to hold common storage is Base.
 * 
 */

public final class DemoClientMultiThread
      extends Thread {
    
  // base storage used by all threads.
  private DemoClientMultiBase base = null;
    
  // the parameter
  private FrameWorkParm TP = null;

  // RMI methods 
  private SvrComm TSC = null;
        
  // the index into the base array for this unit
  private int me = -1; // initially invalid               
                                                 

/**
 * Do an asynchronous request on the Server.
 *    format the return code into an integer
 * 
 * @return int
 */
private int asyncRequest ( ) {

  // return array
  Object back[] = null;

  // call the remote method   
  back = TSC.asyncRequest(TP);

  // Should be an array
  if  (back == null)  {
      
    // say no good  
    return 9000;

  } // endif 
  
  return 0;
  
} // end-method
/**
 * The run method for the thread.    
 * 
 */
 
public void run() {                                 
  
   // times used
   int count = 0;
   
   // return code 
   int rc = 0;
   
   // continue until in shut down mode from the base, or, the return
   //   code from the request is not zero.
   
  while (true) {
    
    // When in shut down mode:
    if  (base.getShutdown()) {
        
        // say finished
        base.setDone(me);
        
        // all done
        break;
    } // endif  
    
    // set new function name 
    TP.setFuncname("F1");
  
    // do a request
    rc = syncRequest(); 
    
    //  When the request was not successfull:
    if  (rc != 0) {
      
      // update the base: my index, return code, nbr time used
      base.setUpdate(me, rc, count);
      
      // say this thread is finished
      base.setDone(me);

      // get out of the loop  
      break;

    } // endif  
    
    // set new function name 
    TP.setFuncname("F2");
  
    // do a request
    rc = syncRequest();
    
    //  When the request was not successfull:
    if  (rc != 0) {
      
      // update the base: my index, return code, nbr time used
      base.setUpdate(me, rc, count);
      
      // say this thread is finished
      base.setDone(me);
      
      // get out of the loop  
      break;

    } // endif  
    
    // set new function name 
    TP.setFuncname("F3");
  
    // do a request
    rc = syncRequest();
    
    //  When the request was not successfull, all done
    if  (rc != 0) {
    
      // update the base: my index, return code, nbr time used
      base.setUpdate(me, rc, count);
      
      // say this thread is finished
      base.setDone(me);
      
      // get out of the loop  
      break;

    } // endif
  
    // increment times thru here without error      
    count++;
  
    // update the base
    //  my index to the array
    //  the return code
    //  times used
    base.setUpdate(me, rc, count);
            
  } // end-while     
   
  // end this thread 
  return;
   
} // end-method
/**
 * Do a synchronous request on the Server.
 *    format the return code into an integer
 * 
 * @return int
 */
private int syncRequest ( ) { 

  // return array
  Object back[] = null;

  // call the remote method   
  back = TSC.syncRequest(TP);

  // Should be an array
  if  (back == null)  {
      
    // say no good  
    return 9000;

  } // endif 
    
  return 0;
  
} // end-method

/**
 * 
 * @param from Base
 * @param number int
 * 
 * 
 */
public DemoClientMultiThread (DemoClientMultiBase from, int number) {  
  
  // give the thread a name
  super("Demo-" + number);
  
  // base storage used by all threads in the test 
  base = from;
  
  // the unique index for this unit
  me = number;

  // new string 
  String x = "Multi Test";

  // make obj
  Object pass = (Object) x;
  
  // form a parameter    
  TP = new FrameWorkParm(pass, // data 
                        "F1",  // function name
                        30,    // wait time
                        1);    // priority
    
    
  // get the RMI methods
  TSC = new SvrComm();   
    
} // end-constructor
} // end-class
