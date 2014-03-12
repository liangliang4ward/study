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

public final class QueueHeader {
    
    // instance fields 
    private String  que_name;   // name of this queue                  
    private String  pap_name;   // name of class to invoke
    private FrameWorkBase  T;   // addr of base storage
    private WaitLists waitlist;  // addr of wait lists   
    private int que_number;     // seq number from zero
    private int nbr_waitlists;  // number of wait lists
    private int nbr_wl_entries; // number of entries in a waitlist 
    private int wait_time;      // time to wait when no work
    private int nbr_threads;    // total threads for this queue 
    private int que_type;       // type of queue 0=normal, 1=agent 
    
    private QueueDetail[] details;   // detail array of thread info
    
    // public data for use by the Queue Thread program and startup
    public DemoCall to_call; // class to call   
    
/**
 * On an overall status request check for non-disabled threads
 * 
 */
 
public boolean checkThreads () {
         
    // loop thru the details  
    for  (int i = 0; i < nbr_threads; i++) {
           
          // When a thread is not disabled
          if  (details[i].getStatus() != QueueDetail.DISABLED) {
              
              // found a live one
              return true;

          } // endif
    } // end-for  

    // no threads are active    
    return false;
    
} // end-method                                                     

/**
 * 
 */

public String[] fetchThreads () {
    
    int i, proc, nbr = 0;
    String[] th_data = new String[nbr_threads];    
    
    // loop thru the details  
    for  (i = 0; i < nbr_threads; i++, nbr++) {
            
          // get the number processed
          proc = details[i].getTotProc();
                      
          // get the status of the thread 
          switch (details[i].getStatus()) {
            
              case QueueDetail.INITIALIZED :

                    // say in english, done
                    th_data[i] = + nbr
                                 + ", ("
                                 + proc
                                 + ") - Never used";
                    // get out
                    break;                                              
                        
              case QueueDetail.DETACHED :

                    // say in english, done
                    th_data[i] = nbr
                                + ", ("
                                + proc 
                                + ") - Inactive";
  
                    // get out
                    break;                                               
                  
              case QueueDetail.WAITING :
                      
                        // say in english, done
                        th_data[i] = nbr 
                                        + ", ("
                                        + proc
                                        + ") - Waiting for work";
                        // get out
                        break;                                            
                  
              case QueueDetail.PROCESSING :  
                      
                        // say in english, done
                        th_data[i] = nbr 
                                        + ", ("
                                        + proc
                                        + ") - Thread processing";
                        // get out
                        break;                                             
                  
              case QueueDetail.INLINK_APPL : 
                      
                        // say in english, done
                        th_data[i] = nbr 
                                        + ", ("
                                        + proc
                                        + ") - In application Class";
                        // get out
                        break;                                              
                  
              case QueueDetail.INLINK_SCHD : 
                      
                        // say in english, done
                        th_data[i] = nbr 
                                        + ", ("
                                        + proc
                                        + ") - Scheduling Output Agent";
                        // get out
                        break;                                               
                  
              case QueueDetail.POSTED :      
                      
                        // say in english, done
                        th_data[i] = nbr 
                                        + ", ("
                                        + proc
                                        + ") - Notified, awaiting execution";
                        // get out
                        break;                                                
                  
              case QueueDetail.STARTED :     
                      
                        // say in english, done
                        th_data[i] = nbr 
                                        + ", ("
                                        + proc
                                        + ") - Reactivated, awaiting execution";
                        // get out
                        break;    
              
              case QueueDetail.DISABLED :

                      // SAY 
                      th_data[i] = nbr 
                                 + ", ("
                                 + proc
                                + ") - Disabled";
  
                      // get out
                       break;              
                                                
            } // end-switch
    } // end-for 
            
    return th_data;
    
} // end-method
/**
 *
 * get all the wait list entries that are busy.
 *   this means looping thru all the wait lists and accum those busy
 *
 * 
 * @return int
 */

public int getAllWlBusy () {
    
    // addr of first waitlist
    WaitLists WL = waitlist;
    
    int i, count = 0;
    
    // loop thru waitlists
    for  (i = 0; i < nbr_waitlists; i++) {
      
           // accum the busy in the waitlist
          count += WL.getWlBusy();
          
          // get the next addr in the linked list
          WL = WL.getNext();

    } // endfor 
    
    return count;
    
} // end-method
/**
 *
 * Get the first available detail that can accept a new thread
 *
 * 
 * @return int
 */
 
public int getAvailable ( ) {    
    
    int i, status;
    
    // look at all the threads
    for  (i = 0; i < nbr_threads; i++) {

          // thread status
          status = details[i].getStatus();  

          // When
          if  ((status == QueueDetail.INITIALIZED) ||
               (status == QueueDetail.DETACHED)) {

              // return position  
                 return i;

          } // endif          
    } // end-for

    // none available   
    return -1;
  
} // end-method

/**
 * 
 * 
 * @return int
 */

public int getBusy () {
    
    int i, count = 0;
    int status;
    
    // count the threads that are busy with a request
    for  (i = 0; i < nbr_threads; i++) {

          // status
          status = details[i].getStatus();  

          // When:
          if  ((status == QueueDetail.PROCESSING)     ||
               (status == QueueDetail.INLINK_APPL)  ||
               (status == QueueDetail.INLINK_SCHD)  ||
               (status == QueueDetail.POSTED)         ||
               (status == QueueDetail.STARTED)) {

              // one found
              count++;

           } // endif         
    } // end-for
    
    return count;
    
} // end-method

/**
 *
 * Get the first wait list entry that is busy.  This means starting
 *   from the first waitlist, (priority 1), and working thru all the
 *   waitlists until a busy is found, or, there are no more waitlists.  
 *
 * @return int
 */

public int getFirstBusy () {
    

    // addr of first waitlist
    WaitLists WL = waitlist; 

    int k = 0;
    
    // until found
    while  (true) {
  
        // get the first busy in this waitlist
        k = WL.fetchFirstBusy();
        
        // When found one
        if  (k != 0) {

            // get out
            break;

        } // endif
        
        // bump to next waitlist
        WL = WL.getNext();

        //  When no more lists
        if  (WL == null)  {

            // get out 
            break;

       } // endif            
    } // end-while               
                  
    return k;
  
  }// end-method              

/**
 *
 *
 */
        
public String getName() {
   
   return que_name;        
                      
} // end-method     
/**
 * 
 * @return int
 */
  
public int getNbrInWl ( ) {
  
    return nbr_wl_entries;
    
} // end-method
/**
 * 
 * @return int
 */
 
public int getNbrThreads ( ) {
  
    return nbr_threads;
    
} // end-method
/**
 * 
 * @return int
 */

public int getNbrWl ( ) {
  
    return nbr_waitlists;
    
} // end-method                

/**
 * 
 * @return java.lang.String
 */

public String getPaClass ( ) {
  
    return pap_name;
    
} // end-method
/**
 * 
 * @return int
 */
 
public int getQueNumber ( ) {
  
    return que_number;
    
} // end-method
/**
 * 
 * @return int
 */
  
public int getQueType ( ) {
  
    return que_type;
    
} // end-method
/**
 * 
 * @return int
 */

public int getThAvail ( ) {
    
    // the status of a detail entry must be other than disablef 
    for  (int i = 0; i < nbr_threads; i++) {

          // When less than cancelled
          if  (details[i].getStatus() < QueueDetail.DISABLED) {

              // found one
              return 0;

          } // endif          
    } // end-for
   
    // none available
    return 1;
    
} // end-method

/**
 *
 * 
 * 
 * @return int
 */
  
public int getWaitTime ( ) {
    
    
    return wait_time;
  
} // end-method
/**
 * 
 * @return int
 */

public int getWlAvail ( ) {
    
    
    // addr of first waitlist
    WaitLists WL = waitlist;
    
    int i;
    
    // loop thru waitlists
    for  (i = 0; i < nbr_waitlists; i++) {
      
          // look for an available entry in the waitlist
          if  (WL.getAvail() == 0) {

              // found  
              return 0;

          } // endif
           
          // get the next addr in the linked list
          WL = WL.getNext();

    } // endfor 

    // none available   
    return 1;
    
} // end-method
/**
 * 
 * @return int
 * @param priority int
 */

public int getWlBusy (int priority ) {    
    
    int prty = priority;
      
    // verify wait list number, (priority)
    if  ((priority < 1) ||
         (priority > nbr_waitlists)) {

        // no good, use one           
        prty = 1;

   } // endif                              
     
    // addr of first waitlist
    WaitLists WL = waitlist;
    
    int i;
    
    // loop thru waitlists until the requested
    for  (i = 1; i < prty; i++) {
                           
           // get the next addr in the linked list
          WL = WL.getNext();
          
          // verify wait list addr
          if  (WL == null) {

              // no good
            return 0;

          } // endif  
    } // end-for                            
      
    // return the count of busy entries
    return WL.getWlBusy();
    
} // end-method
/**
 * 
 * @return int
 */
 
public int getWlEntries ( ) {
  
    return nbr_wl_entries;
    
} // end-method
/**
 * 
 * @return int
 * @param int
 */
 
public int matchRequest (int req)  {         
   
    // addr of first waitlist
    WaitLists WL = waitlist;
        
    // loop thru waitlists until found
    for  (int i = 0; i < nbr_waitlists; i++) {
           
          // When a matching entry is found, return
          if  (WL.matchEntry(req) == 1) {

              return 1;
          } // endif
                 
          // get the next addr in the linked list
          WL = WL.getNext();
          
    } // end-for 

    // no match   
    return 0;
   
} // end-method

/**
 * 
 * This schedules the queue
 *
 * 
 */

public int schedule ( int req_nbr,    // request number
                      int priority,   // priority
                      byte type) {    // type 0=sync, 1=async
                                          
    // put the request in a wait list
    int newW = setNewWaitlist(req_nbr, priority, type);

    // When < 0 failed
    if  (newW < 0) {

        // no wait list available 
        return 25;

    } // endif
                                                                          
    // when an area thread entry 'posts', all done
    if  (setPosted() == 0) {
            
        // get out    
        return 0;

    } // endif
            
    // get a count of the number of threads processing another
    //   request and a pointer, (subscript), to an available
    //   slot for a new thread                 
    int busy_count = getBusy();       
    int avail_ptr  = getAvailable();

                                            
        /** What to do when tasks are:
         *
         *  BUSY  Threads are busy processing another request.
         *  AVAIL Thread entry is available for a new thread.
         *
         *                
         *  BUSY AVAIL    ACTION
         *                    
         *    N     N     Error, cannot schedule.
         *    N     Y     Start new thread.
         *    Y     N     Nothing to do.
         *    Y     Y     Start new thread if a waitlist overflowed otherwise
         */
                 
    // First two possibilities:
    //  When none are busy
    if  (busy_count == 0) {

        // When no thread is available to process 
        if  (avail_ptr  <  0) {
                            
            // Error, cannot continue
            return 20;
        }
        else {
            // When starting a new thread is successful                      
            if  (setNewThread(avail_ptr) == 0) {

                // ok get out
                return 0;
            }
            else {
                // start failed
                return 30;

            } // endif
        } // endif 
    } // endif 
                                                         
    // Third possibility: some are busy & none are available.
    //    nothing to do
    if  (avail_ptr  <  0) {

        // get out
        return 0;

    } // endif 
                             
    // Forth possibility: some are busy & one is available.

    // When adding to a waitlist had an overflow
    if  (newW > 0) {
                
        // start a new thread, error is ignored 
        setNewThread(avail_ptr);

        // get out
        return 0;

    } // endif

  // *--- add threshold processing here ---*

    // all done       
    return 0;
    
} // end-method
/**
 * 
 * @param msg java.lang.String
 */
private void sendNotify (String msg ) {
    
      
    
    // do a notify
    
} // end-method                           

/**
 *
 * @return int
 * @param ptr int
 */

public int setNewThread (int ptr) {
    
    // verify area detail number    
    if  ((ptr < 0) ||
         (ptr >= nbr_threads)) {

        // no good      
         return -1;

    } // endif      
    
    // set the new thread
  
    // When a normal
    if  (que_type < 1) {

        // normal
        return details[ptr].setNewThread();
    }
    else {      
        // agent thread
        return details[ptr].setNewAgentThread();

    } // endif
    
} // end-method
/**
 * 
 * @return int
 * @param req int
 * @param priority int
 * @param type byte
 */
 
public int setNewWaitlist (int req, int priority, byte r_type ) {    
    
    int prty = priority;
         
    // When wait list number, (priority), < 1, set 1
    if  (prty < 1) {

        // set one
        prty = 1;
    }
    else {
        // When > max, set max           
        if  (prty > nbr_waitlists) {

            // set to max           
            prty = nbr_waitlists;

        } // endif    
    } // endif        
    
     
    // addr of first waitlist
    WaitLists WL = waitlist;
    
    // work
    int i = 1;

    // no overflow yet
    int over = 0;
    
    // loop thru waitlists until the requested
    for  (; i < prty; i++) {
                     
          // get the next addr in the linked list
          WL = WL.getNext();
          
          // verify wait list addr
          if  (WL == null) {

              // no good
              return -1;

          } // endif
    } // end-for 
    
    // try to add an entry to this waitlist
    for  (i = i - 1; i < nbr_waitlists; i++) {
           
           // When successfull, return
          if  (WL.setNewWaitlist(req, r_type) == 0) {         

              // When an overflow occured, use 1
              if  (over > 0) {

                  // return, good w/overflowed
                  return 1;
              }
              else {      
                  // return good
                  return 0;

              } // endif     
          } // endif
                     
          // this one overflowed
          if  (over == 0) {

              // set is an overflow
              WL.setOverflow();

              // set overflowed at least once
              over = 1;

          } // endif           
                           
          // get the next addr in the linked list
          WL = WL.getNext();
          
          // verify wait list addr
          if  (WL == null) {

              // did not complete
              return -1;

          } // endif
    } // end-for 
    
    // did not complete
    return -1;
    
} // end-method

/**
 *
 * Find a waiting thread and "post" that thread
 *
 *
 * 
 * @return int
 * @param req int
 * @param time long
 * @param type byte
 */
 
public int setPosted () {
    
    
    // loop thru the details looking for a status of "waiting"
    for  (int i = 0; i < nbr_threads; i++) {
           
          // When a waiting thread, post it
          if  (details[i].getStatus() == QueueDetail.WAITING) {
                
              // When normal, post normal, when Agent, post Agent
                if  (que_type < 1) {

                  // post normal
                  details[i].setPosted();
              }
              else {
                  // post OA
                  details[i].setAgentPosted();

              } // endif        

              // all done               
              return 0;

           } // endif     
    } // endfor   

    // none posted   
    return -1;
    
} // end-method
/**
 *
 * 
 */
  
public void setQueType (int new_type) {
  
  que_type = new_type;
  
} // end-method

/**
 * 
 * @return int
 */

public void setStartThreads () {                                      
    
    int i;
    
    // When a normal queue use thread, else use A thread
    if  (que_type < 2) {
          
          // thread definition
          QueThread thread;
    
          // loop thru the details starting each new thread
          for  (i = 0; i < nbr_threads; i++) {
              
                // new thread          
                thread = details[i].getThread();
           
                // start it
                thread.start();
           
          } // endfor
    }
    else {
          // thread definition
          AgentThread a_thread;
    
          // loop thru the details starting each new thread
          for  (i = 0; i < nbr_threads; i++) {
    
                // new thread      
                a_thread = details[i].getAgentThread();
           
                // start it
                a_thread.start();
           
          } // endfor
    } // endif
    
} // end-method
/**
 * 
 * @param nbr int
 */
public void setWaiting (int nbr ) {
  
    // When nbr is invalid, return
    if  ((nbr < 0) || (nbr >= nbr_threads)) {

        // back
        return;

    } // endif  
    
    // set status of waiting
    details[nbr].setWaiting();
        
} // end-method
/**
 *
 * 
 * 
 */
  
public void setWaitTime (int p_wait_time) {
    
    // new wait time
    wait_time = p_wait_time;
  
} // end-method
/**
 * Set the new number of Wait List entries 
 * 
 */
 
public void setWlEntries (int entries) {
  
    // new int entries
    nbr_wl_entries = entries;

    
} // end-method                                                    

/**
 * 
 * @param msg java.lang.String
 */

private void writeLog (String msg ) {
  
      
    // do logging
  
} // end-method 

/**
 * 
 *
 */

public QueueHeader(FrameWorkBase  c_t,
                  int     c_type,
                  String  c_que_name, 
                  String  c_pap_name,
                  int     c_que_numb,
                  int     c_nbr_waitlists,  
                  int     c_wait_time,  
                  int     c_nbr_threads,
                  int     c_nbr_wl_entries) {
    
    // all those instance fields
    T           = c_t;   
    que_name    = c_que_name;   
    pap_name    = c_pap_name;
    que_number  = c_que_numb;
            
    nbr_waitlists   = c_nbr_waitlists;

    nbr_wl_entries  = c_nbr_wl_entries;

    wait_time       = c_wait_time;
    nbr_threads     = c_nbr_threads;
    que_type        = c_type;
    
    // get the first Wait List.  The Waitlist constructor will get the
    //  subsequent, (chained), waitlists
    waitlist = new WaitLists(T, null, nbr_waitlists, c_nbr_wl_entries);
   
    // the detail array
    details  = new QueueDetail[nbr_threads];
    
    // length of the que name
    int qlen = que_name.length();
    
    // thread name
    String dbase = que_name + "-";
    String tname;
        
    // initialize the detail entries
    for (int i = 0; i < nbr_threads; i++) {
            
        // final name
        tname = dbase + i;
            
        // construct new entry
        details[i] = new QueueDetail(T, this, i, que_type, tname);
   
    } // end-for 
         
} // end-constructor           

/**
 * 
 * @return QueueDetail
 * @param nbr int
 */

public QueueDetail getNextEntry (int nbr ) {
  
    // When nbr is invalid, 
    if  ((nbr < 0) || (nbr >= nbr_threads)) {

        // return null
        return null;

    } // endif  

    // return this detail area      
    return details[nbr];
    
} // end-method

/**
 * 
 * @return WaitLists
 */
 
public WaitLists getWaitlist ( ) {
  
    return waitlist;
    
} // end-method
} // end-class 
