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

public final class QueueDetail {

    // instance fields
    
    private int   status;       // status of this entry 
   
    public static final int INITIALIZED = 0;
    public static final int DETACHED    = 1;
    public static final int WAITING     = 2;
    public static final int PROCESSING  = 3;
    public static final int INLINK_APPL = 4;
    public static final int INLINK_SCHD = 5;
    public static final int POSTED      = 6;
    public static final int STARTED     = 7;

    public static final int DISABLED    = 9;
   
   
                   //   0  - Initialized, never used.  Set by startup
                   //   1  - Detatched.  Set by Monitor after a timeout
                   //   2  - Waiting for post, (resume).
                   //   3  - Processing         (busy)
                   //   4  - In ling to appl  (busy)
                   //   5  - In link to Schd    (busy) 
                   //   6  - Posted, not active (busy)
                   //   7  - Started, not active.  Resumed, but not exec yet.
                   //   9  - Disabled. Set by Startup, Monitor, or Que Task.
                   
    
    private String tname;       // name of this thread                               
    private int  name;          // name of this entry
    private int  totl_proc;     // total requests processed
    private int  totl_new;      // total times instantiated
    
    private FrameWorkBase T;        // base storage
    private QueueHeader   AH;       // The Queue area this belongs to
    private QueThread     thread;   // the Que Task thread for this entry         
    private AgentThread   a_thread; // the Agent thread when this is so


/**
 * 
 * 
 */
 
public void addNew ( ) {
    
    totl_new++;
    
} // end-method

/**
 *
 *
 */
        
public void addProcessed() {
      
   totl_proc++;      
                     
} // end-method                                             

/**
 *
 *
 */
        
public int getName() {
      
   return name;        
                     
} // end-method
/**
 *
 *
 */
      
public int getStatus() {
      
   return status;        
                     
} // end-method                                              

/**
 *
 *
 */
        
public int getTotNew() {
      
   return totl_new;      
                     
} // end-method

/**
 *
 *
 */
        
public int getTotProc() {
      
   return totl_proc;      
                     
} // end-method


/**
 * 
 * @return boolean
 */
 
public boolean isAlive () {
  
    // When the instance is alive
    if  ((thread == null) && 
         (a_thread == null)) {
        
            // dead
            return false;
        }
        else {
            // alive
            return true;

    } // endif
    
} // end-method
                                                              
/**
 * 
 * 
 */
  
public void setAgentPosted () {
        
    // status is posted 
    status = POSTED;
        
    // wake up the thread
    a_thread.wakeUp(); 
    
} // end-method
                                                               
/**
 * 
 *
 */

public void setDetached () {    
    
    // set detached:
    status = DETACHED;
    
} // end-method
/**
 * 
 * 
 * 
 */
 
public void setDisabled (int reason) {

    // set disabled
    status = DISABLED;
    
} // end-method                                                 

/**
 * 
 * 
 */
 
public void setLinkAppl () {

    // status is in link to appl
    status = INLINK_APPL;
    
} // end-method
/**
 * 
 * 
 */
 
public void setLinkSchd () {

    // status is in link to schduling
    status = INLINK_SCHD;
    
} // end-method
/**
 * 
 * @return int
 * 
 */
 
public int setNewAgentThread () {                                    
        
    // the status of this entry must be
    if  ((status == INITIALIZED)  || 
         (status == DETACHED)     ||
         (status == DISABLED)) {

        // ok         
        ;
    }       
    else {
        // go back      
        return 1;

    } // endif     

    // set started
    setStarted();
    
       
    // When dead
    if  (a_thread == null) {

        // up count
        addNew();

        // get a new one
        try {
            // hello
            a_thread = new AgentThread(T, AH, this, tname, getTotNew());

            // not a daemon
            a_thread.setDaemon(false);

            // start the thread
            a_thread.start(); 
                        
            // ALL done
            return 0;

        } // end-try

        catch (Exception e) {

            // errir
            return 1;

        } // end-catch
    } // endif      
    
    // wake up the thread
    a_thread.wakeUp();  

    // done           
    return 0;
    
} // end-method
/**
 * 
 * @return int
 * 
 */
 
public int setNewThread () {                                          
    
    // the status of this entry must be that no thread is attached
    if  ((status == INITIALIZED)  ||
         (status == DETACHED)     ||
         (status == DISABLED))  {

        // ok
        ;
    }       
    else {
        // error
        return 1;

    } // endif
   
    // set started
    setStarted();           
   
    // When dead
    if  (thread == null) {
  
        // up count
        addNew();

        // get a new one
        try {
            // hello
            thread = new QueThread(T, AH, this, tname, getTotNew());

            // not a daemon
            thread.setDaemon(false);

            // start the thread
            thread.start();

            // ALL done
            return 0; 
    
        } // end-try

        catch (Exception e) {
  
            // error
            return 1;

        } // end-catch
    } // endif     
          
    // wake up the thread
    thread.wakeUp();         
    
    // good
    return 0;
    
} // end-method
                                                                   
/**
 * 
 */
 
public void setPosted () {
        
    // status is posted 
    status = POSTED;    
            
    // wake up the thread
    thread.wakeUp();  
    
} // end-method
/**
 * 
 * 
 */

public void setProcessing () {

    // set status
    status = PROCESSING;
    
} // end-method

/**
 * 
 * 
 */
 
public void setStarted () {

    // say started
    status = STARTED;
    
} // end-method                                                     

/**
 * 
 * 
 */

public void setWaiting () {

    // status is Thread waiting for work
    status = WAITING;
    
} // end-method
                                                                     
/**
 *
 * @return AgentThread
 */
  
public AgentThread getAgentThread ( ) {
  
    return a_thread;
  
} // end-method

/**
 *
 * @return QueThread
 */
 
public QueThread getThread ( ) {
  
    return thread;
  
} // end-method

/**
 *
 *
 */
 
public QueueDetail(FrameWorkBase Ty, 
                  QueueHeader Ah, 
                  int c_name, 
                  int type,
                  String th_name) {                         
    
    T       = Ty;       // Base Storage
    AH      = Ah;       // Queue Area to which this entry belongs  
    name    = c_name;   // sequence number
    tname   = th_name;  // thread name 
    status  = INITIALIZED; // initialized, never used  
                 
} // end-constructor  
} // end-class
