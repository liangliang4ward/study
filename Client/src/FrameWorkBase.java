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
 * The base storage for all processing
 *
 */

public final class FrameWorkBase {

   // *-------- All are class fields --------*

 	public static FrameWorkMain	main_tbl	 = null;  // Main table of Queues
	public static FuncHeader   	func_tbl	 = null;  // Function table   
	public static AsyncHeader  	async_tbl	 = null;	// async table
	public static SyncHeader   	sync_tbl	 = null;  // sync table
	public static StallHeader   stall_tbl	 = null;  // stalled async request table
	public static GenTable     	gen_tbl		 = null;  // Number generation table

	public static FrameWorkInterface Ti = null;	// Remote Object myself  
   
/**
 * 
 */
public FrameWorkBase ( ) {
							
} // end-constructor
} // end-class
