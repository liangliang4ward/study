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
import java.rmi.*;
import java.rmi.server.*;

public final class FrameWorkImpl extends UnicastRemoteObject implements
		FrameWorkInterface {

	// base of shared storage
	private FrameWorkBase Ty;

	// table pointers
	private FrameWorkMain main_tbl = null; // Main table of Queues
	private FuncHeader func_tbl = null; // Function table
	private AsyncHeader async_tbl = null; // async table
	private SyncHeader sync_tbl = null; // sync table

	private GenTable gen_tbl = null; // Number generation table

	/**
	 * 
	 * @param FrameWorkBase
	 *            T
	 * 
	 */

	public FrameWorkImpl(FrameWorkBase T) throws RemoteException {

		// load base address to shared storage
		Ty = T;

		// set the other pointers
		main_tbl = Ty.main_tbl;
		func_tbl = Ty.func_tbl;
		async_tbl = Ty.async_tbl;
		sync_tbl = Ty.sync_tbl;
		gen_tbl = Ty.gen_tbl;

	} // end-constructor

	/**
	 * @return Object
	 * @param Req
	 */

	public Object[] asyncRequest(FrameWorkParm Req) throws RemoteException {

		// return stuff
		Object[] back = new Object[1];

		// get the function name from the passed parm
		String F_name = Req.getFuncname();

		// get the detail entry from the function table
		FuncDetail F = func_tbl.getEntry(F_name);

		// not found, is error
		if (F == null) {

			// error msg
			back[0] = "Error: Invalid Function name";

			// send it back
			return back;

		} // endif

		// increment times used
		F.addUsed();

		// get the Que Table for this Function
		QueueHeader[] qtbl = F.getTbl();

		// number of queues
		int nbr_que = qtbl.length;

		// none, is error
		if (nbr_que == 0) {

			// error msg
			back[0] = "Error: Function damaged";

			// send it back
			return back;

		} // endif

		// get next async seq number and bump up by one
		long next_seq = gen_tbl.setAsync();

		// get the time in milliseconds
		long time = System.currentTimeMillis();

		int ar_nbr;
		int i, schd_rc = 0, fail_que = 0, back_rc;

		// type of sched is async
		byte type_schd = 1;

		// Priority, (wait list number)
		int priority = Req.getPriority();

		// sync on table
		synchronized (async_tbl) {

			// get a new Async Request Table entry
			ar_nbr = async_tbl.setNew(time, // time entered
					next_seq, // name of this req.
					nbr_que, // number of queues
					Req.getInput(), // input
					F.getAgent(), // agent
					F.getWho()); // function

		} // end-sync

		// Schedule each Queue
		for (i = 0; i < nbr_que; i++) {

			// schedule
			schd_rc = schedule(qtbl[i], ar_nbr, priority, type_schd);

			// when return is not zero,
			if (schd_rc != 0) {

				// backout all previously scheduled Queues

				// get out
				break;

			} // endif
		} // end-for

		// When scheduling did not complete normally
		if (schd_rc != 0) {

			// back with reason why
			String tt = "Error: Scheduling failed";
			back[0] = tt;

			// send it back
			return back;

		} // endif

		// successfull return message
		String S = "Return: Request successfully scheduled";

		// put in first position
		back[0] = S;

		// send it back
		return back;

	} // end-method

	/**
	 * 
	 * @return Obj[]
	 * @param output
	 *            Object[]
	 */

	private Object[] concatObjects(Object[] output) {

		// When there are no outputs
		if (output == null) {

			// only one occurance
			return new Object[1];

		} // endif

		// return objects[]
		Object[] back;

		// work
		int i, j;
		int nbr_outs = output.length;
		int new_outs = 0;

		// count outputs stored
		for (i = 0; i < nbr_outs; i++) {

			// When used
			if (output[i] != null) {

				// bump count
				new_outs++;
			} else {
				// all done, not every slot used
				break;

			} // endif
		} // end-for

		// When there are no outputs
		if (new_outs == 0) {

			// all done, return one occurance
			return new Object[1];

		} // endif

		// form the new Object[]
		back = new Object[new_outs + 1];

		// move all outputs stored
		for (i = 0, j = 1; i < new_outs; i++, j++) {

			// get the output
			back[j] = output[i];

		} // end-for

		// give back what was found
		return back;

	} // end-method

	/**
	 * 
	 * @return java.lang.String[]
	 * @param name
	 *            java.lang.String
	 * @exception java.rmi.RemoteException
	 *                The exception description.
	 */
	public String[] fetchThreads(String name) throws java.rmi.RemoteException {

		// area header
		QueueHeader area;

		// find the queue in the list of all queues
		int que_nbr = main_tbl.isEqual(name);

		// When not there, que name error
		if (que_nbr < 0) {

			// new string with error msg
			String[] S = new String[1];
			S[0] = "N287";

			// send it back
			return S;

		} // endif

		// get the que area
		area = main_tbl.getArea(que_nbr);

		// When any error, say its a que name error
		if (area == null) {

			// new string with error msg
			String[] S = new String[1];
			S[0] = "N287";

			// send it back
			return S;

		} // endif

		// return data
		return area.fetchThreads();

	} // end-method

	/**
	 * Schedule
	 * 
	 */

	private int schedule(QueueHeader que, // queue
			int req_nbr, // request number
			int priority, // priority
			byte type) { // type 0=sync, 1=async

		// return value
		int R = 0;

		// single thread this request
		synchronized (que) {

			// schedule on the queue.
			R = que.schedule(req_nbr, priority, type);

		} // end-sync

		// return what came back
		return R;

	} // end-method

	/**
	 * 
	 * Shut down Request.
	 * 
	 * 
	 * @return java.lang.String
	 * 
	 */

	public String shutRequest() throws RemoteException {

		// sync on the top level. Prevents multiple threads and the monitor.
		synchronized (Ty) {

			// start the shut down thread
			new ShutThread().start();

		} // end-sync

		String S1 = "FrameWork Server shutdown";

		// print
		System.out.println(S1);

		// bye msg
		return S1;

	} // end-method

	/**
	 * 
	 * Synchronous Request -- Waiting for a reply.
	 * 
	 * 
	 * @return Object[]
	 * 
	 */

	public Object[] syncRequest(FrameWorkParm Req) throws RemoteException {

		// return stuff
		Object[] back = new Object[1];

		// time
		long time_now = 0;

		// sync request number
		int sr_nbr = 0;

		// work
		int i, schd_rc = 0, out_nbr = 0;

		// type of schedule is sync
		byte type_schd = 0;

		// get the function name from the passed parm
		String F_name = Req.getFuncname();

		// get the detail entry from the function table
		FuncDetail F = func_tbl.getEntry(F_name);

		// not found, is error
		if (F == null) {

			// error
			back[0] = "Error: Invalid Function name";

			// send back
			return back;

		} // endif

		// increment times used
		F.addUsed();

		// get the Que Table for this Function
		QueueHeader[] qtbl = F.getTbl();

		// save number of queues
		int nbr_que = qtbl.length;

		// none, is error
		if (nbr_que == 0) {

			// error
			back[0] = "Error: Function damaged";

			// send back
			return back;

		} // endif

		// get next sync seq number
		long next_seq = gen_tbl.setSync();

		// Priority, (wait list number)
		int priority = Req.getPriority();

		// Wait time
		int waitime = Req.getTime();

		// posted/not posted indicator: 0=not, 1=yes
		// This is for the appl thread to post. Java passes arrays by reference,
		// so the
		// appl thread may have access to this object.
		int[] ecb = new int[1];
		ecb[0] = 0;

		// get a new Sync Request Table entry for the request
		// sync on table addr
		synchronized (sync_tbl) {

			// Get a new entry
			sr_nbr = sync_tbl.setNew(Req.getInput(), // input
					next_seq, // uni name
					nbr_que, // number of queues
					waitime, // wait time
					this, // this object (for sync)
					ecb); // for posting
		} // end-sync

		// schedule each Queue in the request
		for (i = 0; i < nbr_que; i++) {

			// schedule the request
			schd_rc = schedule(qtbl[i], sr_nbr, priority, type_schd);

			// when return is not zero, backout all previously scheduled
			// Queues
			if (schd_rc != 0) {

				// do a backout of the the prior
				// your code here

				// free the sync request table entry
				// sync on table addr
				synchronized (sync_tbl) {

					// free it
					sync_tbl.setFree(sr_nbr);

				} // end-sync

				// get out
				break;

			} // endif
		} // end-for

		// When scheduling did not complete normally
		if (schd_rc != 0) {

			// say why
			String tt = "Error: Scheduling failure";
			back[0] = tt;

			// send back
			return back;
		} // endif

		// output array
		Object[] sr_output = null;

		// start of the wait
		long start_time = System.currentTimeMillis();

		// number not processed
		int nbr_remaining = 0;

		// wait time seconds to milliseconds
		long time_wait = (long) (waitime * 1000);

		/*
		 * 
		 * See if we're done yet
		 */

		// sync on table addr
		synchronized (sync_tbl) {

			// get the nbr remaining to be processed
			nbr_remaining = sync_tbl.getRemaining(sr_nbr);

			// When none remain, get the stuff
			if (nbr_remaining < 1) {

				// get the outputs
				sr_output = sync_tbl.getOutput(sr_nbr);

				// free the sync table entry
				sync_tbl.setFree(sr_nbr);

			} // endif
		} // end-sync

		// When none remain, all done
		if (nbr_remaining < 1) {

			// return with successfull message and concatenated outputs
			back = concatObjects(sr_output);

			// top msg
			String t1 = "Return: ";
			back[0] = t1;

			// send back
			return back;

		} // endif

		/**
		 * 
		 * Wait for the request to complete, or, time out.
		 * 
		 */

		// get the lock for this RMI object
		synchronized (this) {

			// until work finished
			while (ecb[0] == 0) {

				// wait for a post or timeout
				try {
					// max wait time is the time passed
					wait(time_wait);

				} // end-try

				catch (InterruptedException e) {
				} // end-catch

				// When not posted
				if (ecb[0] == 0) {

					// current time
					time_now = System.currentTimeMillis();

					// decrement wait time
					time_wait -= (time_now - start_time);

					// When no more seconds remain
					if (time_wait < 1) {

						// get out of the loop, timed out
						break;
					} else {
						// new start time
						start_time = time_now;

					} // endif
				} // endif
			} // end-while
		} // end-sync

		// sync on table addr
		synchronized (sync_tbl) {

			// get the nbr remaining to be processed
			nbr_remaining = sync_tbl.getRemaining(sr_nbr);

			// get the outputs
			sr_output = sync_tbl.getOutput(sr_nbr);

			// When none remain
			if (nbr_remaining == 0) {

				// free the sync table entry
				sync_tbl.setFree(sr_nbr);

			} // endif
		} // end-sync

		// When none remain, success, even though the time elapsed
		if (nbr_remaining < 1) {

			// return with successfull message and concatenated outputs
			String t3 = "Return: ";

			back = concatObjects(sr_output);
			back[0] = t3;

			// send back
			return back;

		} // endif

		/**
		 * 
		 * timed out.
		 * 
		 */

		// returned id
		long sr_id = 0;

		// sync on table addr
		synchronized (sync_tbl) {

			// returned id
			sr_id = sync_tbl.getUni(sr_nbr);

			// When the same as what was set, above
			if (sr_id == next_seq) {

				// Backout everything that can be
				// add your code here

				// get the finished outputs
				sr_output = sync_tbl.getOutput(sr_nbr);

			} // endif
		} // end-sync

		// *--- sync table request remains, you must add code to remove it ---*

		// When the same as what was set above
		if (sr_id == next_seq) {

			// timed out: return with timed out message and concatenated outputs
			back = concatObjects(sr_output);
		} else {
			// no outputs
			back = new Object[1];

		} // endif

		// error in first position
		String t5 = "Error: Timed-out";
		back[0] = t5;

		// send back
		return back;

	} // end-method
} // end-class
