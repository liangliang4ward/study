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
 
public final class FrameWorkMonitor
	extends Thread {
   
	// base storage   
	private FrameWorkBase  T;  
   
	// time at each dispatch
	private long time;

	// monitor interval
	private int sleep_time; 
   
	private String error = null; 

	// Async table positioning
	private AsyncHeader ar_curr;
	private AsyncHeader ar_test;

	// error msgs
	private String N_msg = null;
	private String L_msg = null;

/**
 * 
 */
   
public FrameWorkMonitor (FrameWorkBase Ty, int sleep)  {       
  
  super("FrameWorkMonitor");   
  
  // base storage
  T = Ty; 

	// sleep time in milli-seconds
	sleep_time = sleep * 1000;

} // end-constructor
/**
 * 
 * @return int
 * @param ar_nbr int
 */

private int checkAsync(int ar_nbr) {
		  
				
	// put your code here to check how long this entry has been here
	// When it is over the max, add a stall table entry
	
/*				  
		// get a lock on the table
		synchronized (T.stall_tbl) {
				  
			// add a stall table entry
			T.stall_tbl.addEntry(ar_nbr, ar_curr.getName(ar_nbr), 1);

		} // end-sync   
*/

	// no action
	return 0;

} // end-method
/**
 *
 * This is the Monitor 
 * 
 */
	 
private void doWork( )  {    
	
	// get the current time in milliseconds
	time = System.currentTimeMillis();
  
	int i, rc = 0;
	int ar_nbr;
	int nbr_entries;
 	 
		/** 
		 *
		 * Async Table Processing
		 *
		 *
		 */	
		
		// position at first table
		ar_curr = T.async_tbl;
				  
		// start at first
		ar_nbr = 0;
			
		// look at every async detail entry
		while (true) {
			
			// sync on tbl addr  
			synchronized (T.async_tbl) {    
	  
				// get the next used async request
				ar_nbr = ar_curr.getNextUsed(ar_nbr);   
		
				// When found one
				if  (ar_nbr >= 0) {
		
					// check async entry 
					rc = checkAsync(ar_nbr);
			
				} // endif
			} // end-sync

			// When there was a return msg
			if  (rc > 0) {

				// send notify msg
				sendNotify(N_msg);

				// write a log msg
				writeLog(N_msg);

				// reset rc
				rc = 0; 
	  
			} // endif

			// When at end
			if  (ar_nbr < 0) {

				// get out
				break;

			} // endif  

			// next async number past this one
			ar_nbr++;

		} // end-while

} // end-method
/**
 * 
 */
   
public void run () {
					  
	
	// never ending
	while  (true) {   
	  
		try {       
			// wait this long
			sleep(sleep_time);
		
		} // end-try
	
		catch (InterruptedException e) {
	  
			// oops    
			return;
			
		} // end-catch  
	  
		// do the monitor function 
		doWork();
	 
	} // end-while
					   
} // end-method
/**
 * 
 * @param msg java.lang.String
 */
private void sendNotify (String msg )  {
  
	  
		// pass to the notify 
  
} // end-method
/**
 * 
 * @param msg java.lang.String
 */
private void writeLog (String msg ) {
	
	// When logging, do so
  
} // end-method
} // end-class
