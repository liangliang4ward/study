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
 * Server start up
 * 
 */

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public final class FrameWorkServer {

	// The base for all persistent processing
	private static FrameWorkBase T = null;

	/**
	 * load queues and functions All hard coded.
	 */

	private static int load() throws Throwable {

		// variables
		int i, j;

		// area header used for all queues
		QueueHeader Area;

		// Error string
		String error;

		// the main queue storage table
		T.main_tbl = new FrameWorkMain(5);

		// *--- load all the queues ---*

		// load an Queue header
		Area = new QueueHeader(T, // base
				0, // 0=normal
				"Q1", // que name
				"Demo1", // appl class name
				0, // seq number
				5, // number of wl
				30, // max wait time
				3, // nbr threads
				10); // nbr in wl); // %

		// insert addr in main table
		T.main_tbl.setNew(Area);

		// load the class
		try {
			Area.to_call = new Demo1();

		} // end-try

		catch (Exception e) {

			// print error
			System.out.println("Error; " + e);

			// return
			System.exit(1);

		} // end-catch

		// load an Queue header
		Area = new QueueHeader(T, // base
				0, // 0=normal
				"Q2", // que name
				"Demo2", // appl class name
				1, // seq number
				5, // number of wl
				30, // max wait time
				3, // nbr threads
				10); // nbr in wl); // %

		// insert addr in main table
		T.main_tbl.setNew(Area);

		// load the class
		try {
			Area.to_call = new Demo2();

		} // end-try

		catch (Exception e) {

			// print error
			System.out.println("Error; " + e);

			// return
			System.exit(1);

		} // end-catch

		// load an Queue header
		Area = new QueueHeader(T, // base
				0, // 0=normal
				"Q3", // que name
				"Demo3", // appl class name
				2, // seq number
				5, // number of wl
				30, // max wait time
				3, // nbr threads
				10); // nbr in wl);

		// insert addr in main table
		T.main_tbl.setNew(Area);

		// load the class
		try {
			Area.to_call = new Demo3();

		} // end-try

		catch (Exception e) {

			// print error
			System.out.println("Error; " + e);

			// return
			System.exit(1);

		} // end-catch

		// load an Queue header
		Area = new QueueHeader(T, // base
				1, // 1=agent
				"Q4", // que name
				"Agent1", // appl class name
				3, // seq number
				2, // number of wl
				30, // max wait time
				2, // nbr threads
				10); // nbr in wl);

		// insert addr in main table
		T.main_tbl.setNew(Area);

		// load the class
		try {
			Area.to_call = new Agent1();

		} // end-try

		catch (Exception e) {

			// print error
			System.out.println("Error; " + e);

			// return
			System.exit(1);

		} // end-catch

		// load an Queue header
		Area = new QueueHeader(T, // base
				0, // 0=normal
				"Q5", // que name
				"Recur1", // appl class name
				4, // seq number
				2, // number of wl
				30, // max wait time
				2, // nbr threads
				10); // nbr in wl); // %

		// insert addr in main table
		T.main_tbl.setNew(Area);

		// load the class
		try {
			Area.to_call = new Recur1();

		} // end-try

		catch (Exception e) {

			// print error
			System.out.println("Error; " + e);

			// return
			System.exit(1);

		} // end-catch

		// *--- end of Queue Section ---*

		// temporary array for the queue names
		QueueHeader[] que_names;

		// initialize the function table
		T.func_tbl = new FuncHeader(4);

		// *--- load all the functions ---*

		// new queue list
		que_names = new QueueHeader[1];

		// que is Q1
		que_names[0] = T.main_tbl.getArea(0);

		// build an entry
		T.func_tbl.setNew("F1", // name
				T.main_tbl.getArea(3), // agent
				1, // single queue
				que_names); // que list

		// new queue list
		que_names = new QueueHeader[2];

		// que is Q1, Q2
		que_names[0] = T.main_tbl.getArea(0);
		que_names[1] = T.main_tbl.getArea(1);

		// build an entry
		T.func_tbl.setNew("F2", // name
				T.main_tbl.getArea(3), // agent
				2, // 2 queues
				que_names); // que list

		// new queue list
		que_names = new QueueHeader[3];

		// que is Q1, Q2, Q3
		que_names[0] = T.main_tbl.getArea(0);
		que_names[1] = T.main_tbl.getArea(1);
		que_names[2] = T.main_tbl.getArea(2);

		// build an entry
		T.func_tbl.setNew("F3", // name
				T.main_tbl.getArea(3), // agent
				2, // 2 queues
				que_names); // que list

		// new queue list
		que_names = new QueueHeader[1];

		// que is Q5
		que_names[0] = T.main_tbl.getArea(4);

		// build an entry
		T.func_tbl.setNew("F4", // name
				T.main_tbl.getArea(3), // agent
				1, // single queue
				que_names); // que list

		// all done
		return 0;

	} // end-method

	/**
	 * main entrypoint - starts the application
	 * 
	 * @param args
	 *            java.lang.String[]
	 */
	public static void main(java.lang.String[] args) throws Throwable {

		// starting
		System.out.println("FrameWork Server initializing");

		// the base for all processing
		T = new FrameWorkBase();

		// *--- Internal tables ---*

		// Async Table
		T.async_tbl = new AsyncHeader(T, null, 32);

		// Sync Table
		T.sync_tbl = new SyncHeader(T, null, 32);

		// Stall Table
		T.stall_tbl = new StallHeader(T, null, 32);

		// Number Generation Table
		T.gen_tbl = new GenTable(T);

		// *--- Queues and Functions ---*

		// load the queues and functions
		if (load() != 0) {

			// done
			System.exit(0);

		} // endif

		// *--- Remote Object ---*

		// the remote object
		FrameWorkImpl Ty1 = new FrameWorkImpl(T);

		// set global remote object
		T.Ti = (FrameWorkInterface) Ty1;

		// *--- RMI Section ---*

		// rebind
		try {
			// rebind
		    LocateRegistry.createRegistry(8808);   
            Naming.rebind("//localhost:8808/SAMPLE-SERVER" , Ty1);

		} // end-try

		catch (Exception e) {

			System.out.println("Error: " + e);

			// get out
			return;

		} // end-catch

		// use a monitor at 60 second interval
		new FrameWorkMonitor(T, 60).start();

		// *--- all done message ---*
		System.out.println("FrameWork Server started successfully");

	} // end-method
} // end-class
