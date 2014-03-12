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

public final class AgentThread
	      extends Thread {
	

		private FrameWorkBase	T;    		 // base storage
		private QueueHeader 	area; 		 // the QueueHeader for this thread
		private QueueDetail 	me;			   // the QueueDetail for this thread
		private AsyncHeader 	async_tbl; // async request table
		
		private int que_nbr;					// int name of this queue
   	private int th_nbr;						// int name of this thread
		private int instance_nbr;			// current instance number
   	private boolean had_problem;	// internal problem

		private Object o_args;		// passed object array 
		private FrameWorkInterface fw = null; // passed pointer to remote object

		private volatile boolean posted;  // posted indicator

/**
 * 
 * @param Ty TyBase
 */
 
public AgentThread (FrameWorkBase Ty, 
										QueueHeader AH, 
										QueueDetail AD,
										String tname,
										int int_nbr) {
	
		// name of thread
		super(tname);		
	
		// set pointer to base storage
	  T    = Ty;  

		// set pointer to Queue header to which this thread belongs
	  area = AH; 

		// set pointer to Queue detail to which this thread belongs
	  me   = AD; 

		// set pointer to async table
	  async_tbl = T.async_tbl; 

		// set reference to remote object
	  fw = T.Ti; 

		// current instance number
		instance_nbr = int_nbr;
	  
		que_nbr	= area.getQueNumber();	// int name of this que 
		th_nbr	= me.getName();					// int name of this thread
	  
		// the concatenated outputs, now input	 
		o_args = null;	  

		had_problem = false;	// no problem 
		posted 			= false;  // not posted
	  
} // end-constructor
/**
 * For the caught exception
 * 
 */

private void caught(String msg, int reason) {

			  			
		// send notification and log the msg 
		sendNotify(msg);
		writeLog(msg);
	 					
		// lock on Queue
		synchronized (area) {
		  	  								  			
				// this entry is logically disabled
   			me.setDisabled(reason);

		} // end-sync

		// all done
		had_problem = true;


} // end-method
/**
 *
 * Check the wait lists. 
 *
 *   When there is work to do, call doWork. 
 */
 
private void checkWork ( ) {
	  	  
	  int     sub, type;
	  boolean	iswork = true;
	  
		// spin when more work	 	  
		while (iswork) {
	  		
	  		// get a lock on the queue Area 
	  		synchronized (area) {
						
	  				// status is "actively processing"
	  				me.setProcessing();
	  	  				
	   	     	// find first busy in waitlists
	       		sub = area.getFirstBusy();
	  	        
	       		// When zero, nothing.
	       		if  (sub  == 0) {
	  	        	
	   	  	     	// no work
	   	  	     	iswork = false;
	       		} 
	       		else {
	     	      	// async is complement
	      	    	sub = (sub * -1);

	      		} // endif 	   
			
						// When no work was found: 	        
							if  (!iswork) {
	  	
									// set status to Waiting 
	     						me.setWaiting();

							} // endif
				} // end-sync	  
			    
	  		// When work was found    	        
				if  (iswork) {

						// do the request
	     			doWork(sub);

						// When something went wrong in doWork, get out
	     			if  (had_problem)  {

								// get out
	     					return;

	     			} // endif			
	     } // endif
	 } // end-while 
		  
} // end-method
/**
 *
 * This is the Agent Queue
 *
 * 
 */

private void doWork (int sub) {	  

		// async table
		int  ar_nbr = 0;

		// passed to appl
		Object[] pass = null;

		// error msg
	 	String L_msg; 
	 
	  
	  // Sub contains the request, pick up all the outputs from
	  //   previous tasks and pass this to the Agent class
	  
	  Object[] outputs = null; 
	  
	 	//   get a lock on the table
	  synchronized (async_tbl) {		

				// When there are outputs stored
	 			if  (async_tbl.getNextOut(sub) >= 0) {
	  	    
	 					// get the array of outputs
	   				outputs	= async_tbl.getOutput(sub);

				} // endif
		} // end-sync

	       
	  // When there are output objects
		if  (outputs != null) {

				// work
				int i, nbr_outs	= outputs.length, max_outs = 0;

				// find out how many real outputs
				for  (i = 0; i < nbr_outs; i++) {

							// When not a valid output
							if  (outputs[i] == null) {

									// get out
									break;
							}
							else {
									// count of good outputs
									max_outs++;

							} // endif
				} // end-for					

				// When some outputs
				if  (max_outs > 0) {

						// get the new obj[]
						pass = new Object[max_outs];
				
						// concatenate all outputs stored
	 		  	  for  (i = 0; i < max_outs; i++) {
	            							
									// get the output array
			 						pass[i] = outputs[i]; 

						} // end-for
				} // endif
		} // endif	 	
	  
		// the concatenated outputs, now input	 
		o_args = pass;
	  
		// get a lock on the queue Area table
	  synchronized (area) {	
  
	 			// status is "in Link to Appl"
				me.setLinkAppl();

		} // end-sync
	  	
		//             
		try {
				// invoke the Application Class		           
	  		area.to_call.doWork(o_args, fw);
					
	  } // end-try     
   
   catch (java.lang.Throwable e) {
					
	  			L_msg = "error";
	  			
	  			// common error handling
					caught(L_msg, 7020);

		} // end-catch 
   

		// When normal end
		if  (!had_problem) {
	
			  // get a lock on the queue Area table
	 			synchronized (area) {	  
						
						// accum number processed
 						me.addProcessed();

						// thread processing
 						me.setProcessing();

				} // end-sync
		} // endif
	  
	  // Function is complete:  
	  //   get a lock on the table
	  synchronized (async_tbl) {	
				
				// Delete the async table entry.	  	
	  		async_tbl.freeEntry(sub);
	  		
	  } // end-sync	
	  	  	  	  
		// the concatenated outputs no longer needed	 
		o_args = null;
	  
} // end-method
/**
 * 
 */

public void run () {
	  
		long wait_time = 0;
		int  my_wait	 = 0;
	  int  working 	 = 0;
		int  detached	 = 0;	

		// until shut down					  	  
		while  (true) {

				// sync on area
				synchronized (area) {

						// get status
						working = me.getStatus();

						// max wait time in seconds
						my_wait = area.getWaitTime(); 

				} // end-sync
																								     
				// When 'posted' or 'started'     
				if  ((working == QueueDetail.POSTED) ||
	     			 (working == QueueDetail.STARTED)) {

						// reset the posted indicator
						posted = false;
	     				    
						// check for work to do
						checkWork();
						
	  	    	// When something went wrong, (status=disabled), get out
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
						} // end-if							
				}
				else {	 
							// When disabled
							if  (working == QueueDetail.DISABLED) {
						
									// all done 
									return;

							} // endif
		
	  	 	  	 	// wait for activation
							// sync on this
							synchronized (this) {
		  	  						
									// wait for a posting
									while (!posted) {
										
											try {
													// wait for a post
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
 * @param msg java.lang.String
 */
private void sendNotify (String msg ) {
		
			
		// do a notify
		
} // end-method
/**
 * Wake up the posted thread
 * 
 */
public synchronized void wakeUp() {

		// set the posted indicator
		posted = true;

		// wake up
		notify();

} // end-method
/**
 * 
 * @param msg java.lang.String
 */
private void writeLog (String msg ) {
				
		

				// log msg
		
} // end-method
} // end-class
