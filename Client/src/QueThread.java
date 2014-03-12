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
 * 
 */

public final class QueThread
	      extends Thread {
	
   	private FrameWorkBase	T;     // base storage
   	private QueueHeader 	area;  // the QueueHeader for this thread
   	private QueueDetail		me;    // the QueueDetail for this thread

		private AsyncHeader async_tbl; // async request table
		private SyncHeader 	sync_tbl;  // sync request table

		private FrameWorkInterface fw = null; // passed pointer to remote object
   
   	private int que_nbr;					// int name of this queue
   	private int th_nbr;						// int name of this thread
		private int instance_nbr;			// current instance number
		private boolean had_problem; 	// something went wrong in doWork

		private Object o_args;	// passed object to appl  
   
   	private volatile boolean posted; 	// been posted


/**
 * 
 * @param Ty FrameWorkBase
 */
 
public QueThread (FrameWorkBase Ty, 
									QueueHeader AH, 
									QueueDetail AD,
									String tname,
									int int_nbr) {
				
		// name of thread
		super(tname);	
	
		// set pointer to base storage
	  T    = Ty; 

		// set pointer to area header to which this thread belongs 	
	  area = AH;

		// set pointer to area detail to which this thread belongs  	
	  me = AD;

		// set pointer to async table  	
	  async_tbl = T.async_tbl;

		// set pointer to sync table  	
	  sync_tbl = T.sync_tbl;

		// set reference to remote object
	  fw = T.Ti;

		// current instance number
		instance_nbr = int_nbr; 	
	  
		que_nbr 	= area.getQueNumber();	// int name of this que 
		th_nbr		= me.getName();					// int name of this thread

		// put passed obj in 1st position
	  o_args = null;
			
		had_problem = false;  // no problem
		posted 			= false;  // not posted  
	   
} // end-constructor
/**
 * For the caught exception
 * 
 */

private void caught(String msg, boolean syncReq, int sub, long async, int reason) {

	  			
		// send notification and log the msg 
		sendNotify(msg);
		writeLog(msg);
	  			
		// When an Async request 
		if  (!syncReq) {
								
				// get a lock on the table
				synchronized (T.stall_tbl) {
										
						// add a stall table entry
	 				T.stall_tbl.addEntry(sub, async, reason);

	 		} // end-sync																
   	} // endif  				
				
		// all done
		had_problem = true;


} // end-method
/**
 *
 * Check the request in the wait lists. 
 *
 *   When there is work to do, call doWork. 
 */
 
private void checkWork ( ) {
	  	  
		// the request from a table
	  int	sub;

		// 0 is sync, 1 is async
	  int request = 0;

	  boolean	iswork  = true;
	  

		while (iswork) {
					      
	  		// get a lock on the Area Table
				synchronized (area) {

	  				// status is "actively processing"
	  				me.setProcessing();
	       			
	       		// check in the waitlists
  	   			sub = area.getFirstBusy();
  	        
  	   			// zero is nowork
  	   			if  (sub  == 0) {
	  	        	
  	  	     		// no work
  	  	     		iswork = false;
	     			} 
	     			else {
	     	     		// + is sync, - is async
	        			if  (sub < 0) {

										// stored compliment  
		        			sub = (sub * -1);

										// is async
		        			request = 1;
 	        			}
 	        			else {
										// is sync	
 	    	         		request = 0;

 	        			} // endif
						} // endif	   

						// When no work was found: 	        
						if  (!iswork) {
	  
								// set status to Waiting
	   						me.setWaiting();

						} // endif
				} // end-sync	      
					     
	     	// When work was found: 	        
				if  (iswork) {
					
						// process the request
	          doWork(sub, request);	 

						// When something went wrong in doWork, (status=disabled), get out
	    			if  (had_problem)  {

								// done here
	     					return;

	     			} // endif
	    	} // endif
		} // end-while 
	  
} // end-method
/**
 *  
 * 
 */
 
private void doWork (int sub, int request) {
		
	
		// input 
	 	Object input_nbr = null;
			  
	  // unique name of sync request
	  long Uni = -1;

		// unique name of Async request
		long async_name = 0;
	  
		// error msg
	  String L_msg = null;

		// type of request
	  boolean	syncReq = true;
	  
	  
	  // When Sync request 
		if  (request == 0) {
		
				// set is sync request
				syncReq = true;

				// lock the table
				synchronized (sync_tbl) {

						// get unique number
 						Uni = sync_tbl.getUni(sub);

						// get input 
 						input_nbr = sync_tbl.getInput(sub);

				} // end-sync
	  }
	  else {	
				// not sync request, async
				syncReq = false;
				
				// lock the table
				synchronized (async_tbl) {
				 
						// get input string number
	  				input_nbr = async_tbl.getInput(sub);

						// get unique name	
						async_name = async_tbl.getName(sub);

				} // end-sync
	  } // endif

		// When input 
		if  (input_nbr != null) {
				
				// get the input
				 o_args = input_nbr; 
		}
		else {
				// no input
				 o_args = null;

		} // endif
	  
	  // obj to pass back
		Object back = null;

	  // get a lock on the Area Table
		synchronized (area) {

			  // status is "in Link to Appl"
	 			me.setLinkAppl();

		} // end-sync
	  
	  try {				      	           
  			// "Link" to the user appl class
	   	back = area.to_call.doWork(o_args, fw);

	  } // end-try
	  
		catch (java.lang.Throwable e) {
	  		
	  		L_msg = "error";
	  			
				// common error handling
				caught(L_msg, syncReq, sub, async_name, 6010);

		} // end-catch 

		// no longer needed	 
		o_args = null;

	  		
		// When normal ending
		if  (!had_problem) {
			
			 	// get a lock on the Area Table
				synchronized (area) { 

						// accum number processed
	 					me.addProcessed();

						// set thread processing
	 					me.setProcessing();
	
				} // end-sync
		} // endif
	 		
		// When a sync request
		if  (syncReq) {
	  
				// sync on table
				synchronized (sync_tbl) {	

						// When the original matches now, (there may have been a timeout and the
						//    sync table entry was purged and re-used by another request)
						if  (Uni == sync_tbl.getUni(sub)) {
														
								// When a valid output exists
								if  (back != null) {

										// save the output number
   									sync_tbl.setOutput(sub, back);

								} // endif

								// decrement and get number remaining queues
								// When finished
								if  (sync_tbl.setDecrement(sub) < 1) {

										// post the waiting thread 
	 									sync_tbl.setPosted(sub);
					
								} // endif
						} // endif
 				} // end-sync

				// all done with a sync request
				return;

		} // endif


		// *--- is an async request ---*

		// did not complete, no agent
	  int fini = 1; 
		QueueHeader agent = null;

		// result of scheduling Agent, in backout, no
		int	schd_result = 0;

		// type is async used for schedule
	  byte type_schd = 1; 

		// sync on table
		synchronized (async_tbl) {	

				// returns number remaining 			
 	 			fini = async_tbl.setDecrement(sub);
	  				
				// When normal ending
				if  (!had_problem) {

						// get the agent name, (-1 is none)				
	 					agent = async_tbl.getAgent(sub);

				} // endif
  	  
				// When a valid output exists
				if  (back != null) {

						// update the output data 							 		
 						async_tbl.setOutput(sub, back);

				} // endif
 		} // end-sync		
					  
		// When completing an async request, (fini=0), and there is an
		//   agent queue, (> -1), then schedule the Agent. 
		if  (fini == 0) {
				
				// But only -- When normal ending
				if  (!had_problem) {
						  			
		  	    // When an agent que:
	 				  if  (agent != null) {
	       	    
			   		   // get a lock on the Area Table
								synchronized (area) {

			          		// status is "in Link to Schd"
	 			         		me.setLinkSchd();

								} // end-sync
	       	
			       	  // *--- Schedule the sucker ---*
					      schd_result = schedule(agent, sub, 1, type_schd);

	  						// get a lock on the Area Table
								synchronized (area) {

					         	// status is "actively processing"
	 				        	me.setProcessing();	
					
								} // end-sync	   
			            
	  		      	// When scheduling failed:
	 	  	      	if  (schd_result != 0) {
												           				
	     			   			// get a lock on the table
										synchronized (T.stall_tbl) {
										
	       								// add a stall table entry
	       								T.stall_tbl.addEntry(sub, async_name, schd_result);

	       						} // end-sync														
	           				
	       						// log the error
	       						L_msg = "error";

										// log
	      		   			writeLog(L_msg);	
	            			        		 			
								} // endif
						}
						else {
									// purge the async table request	
									//   get a lock on the table
	  							synchronized (async_tbl) {	

											// purge the request				            
							   			async_tbl.freeEntry(sub);

								  } // end-sync 
	 		     } // endif
	  		} // endif
		} // endif
	  
} // end-method
/**
 * 
 *	 
 */

public void run () {
	  

		long wait_time = 0;	// for waiting for a post or activate
		int  my_wait	 = 0; // from que area
	  int  working 	 = 0; // temp
		int  detached  = 0; // temp
	  

		// spin until shutdown		  	  
		while  (true) {	

				// lock on Queue
				synchronized (area) {
		  	  						
						// get current status
						working = me.getStatus();

						// que wait time in seconds
						my_wait = area.getWaitTime();
						
				} // end-sync				
						     
				// When 'posted' or 'started'     
				if  ((working == QueueDetail.POSTED) ||
	     			 (working == QueueDetail.STARTED)) {

						// reset the posted indicator
						posted = false;

						// check for work to do
						checkWork();

	  	    	// When something went wrong, get out
	     			if  (had_problem)  {

								// finished
	     					return;

	     			} // endif
								  	    	  
	  	  		// compute the max time to wait = seconds to milli-
						wait_time = my_wait * 1000;

						// sync on this
						synchronized (this) {
		  	  						
								// spin until posted
								while (!posted) {
										
										try {
												// wait for a posting or time out
												wait(wait_time);
															
												// When timed out		
												if  (!posted) {
								
														// set temp status as inactive				
														detached = 1;

														// set is posted
														posted = true;	
															
												} // endif
										} // end-try

										catch (InterruptedException e) {
										} // end-catch
								} // end-while

								// reset the posted indicator
								posted = false;

						} // end-sync

						// When a time out occurred
						if  (detached == 1) {
															
								// reset
								detached = 0;	

								// sync on area
								synchronized (area) {
												
										// When still in waiting status
										if  (me.getStatus() == QueueDetail.WAITING) { 

												// set status as inactive				
												me.setDetached();						

										} // endif
								} // end-sync
						} // endif
				}
				else {							
  	 	  	 	// *--- wait for activation ---*

						// sync on myself
						synchronized (this) {
		  	  						
								// spin until set true
								while (!posted) {
										
										try {
												// wait for re-activation
												wait();
										} // end-try

										catch (InterruptedException e) {
										} // end-catch
								} // end-while

								// reset the posted indicator
								posted = false;

						} // end-sync
 	  	  } // endif
	  } // end-while 
	  	
} // end-method
/**
 * 
 * schedule the agent  
 *
 * 
 */
private int schedule (QueueHeader que, // queue
	                    int req_nbr,    // request number
	                    int priority,   // priority
	                    byte type) {    // type 0=sync, 1=async
	  
	 	 	 
		// return value                       
		int R = 0; 
		
		// When not an OA bye
		if  (que.getQueType() == 0) { 

				// error
		    return 15;

   	} // endif
   
   	// single thread this request
   	synchronized (que) {	       
					     
	  		// schedule the request
				R = que.schedule(req_nbr, priority, type); 

		} // end-sync

		// give back the result
		return R;
	  
} // end-method
/**
 * 
 * @param msg java.lang.String
 */
private void sendNotify (String msg ) {
		
		// send a notification
		
} // end-method
/**
 * Wake up this posted thread 
 *
 */
public synchronized void wakeUp() {
		
		// set posted indicator
		posted = true;		

		// wake up
		notify(); 

}  // end-method
/**
 * 
 * @param msg java.lang.String
 */
private void writeLog (String msg ) {
				
		// When logging, do so
		
} // end-method
} // end-class
