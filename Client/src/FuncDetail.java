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

public final class FuncDetail {

   // instance fields
   private String	name;     // Function name
   private long  	used;     // times used
   private QueueHeader agent;    // Output agent queue 
   private int   	who;      // who i am
   private int		nbr_que;	// number of queues in this entry     
   private QueueHeader[]	qtbl;     // queue table 

/**
 * 
 *
 */

public FuncDetail() {
   
		name    = "";   // no name yet                    
	 	agent   = null;  // no output agent que                           
	 	used    = 0;    // times this func used
		nbr_que = 0;    // no queues yet	              
		qtbl    = null; // no Queue table
	 
} // end-constructor
/**
 * 
 */
 
public synchronized void addUsed ( ) {
		
		// number of times used	
		used++;
  
} // end-method
/**
 * 
 * 
 */

public QueueHeader getAgent ( ) {
	
	 return agent;
	 	
} // end-method
/**
 * 
 * @return java.lang.String
 */
 
public String getName ( ) {
	
	 return name;
	 
} // end-method
/**
 * 
 * @return int
 */
 
public int getNbrQue ( ) {
	
	  return nbr_que;
	  
} // end-method
/**
 *
 * 
 */
 				 
public QueueHeader[] getTbl() {
   	
   return qtbl;
		 
} // end-method       
/**
 *
 *
 */
			  
public long getUsed() {
	
	  return used;    
		 
} // end-method       
/**
 * 
 * @return int
 */
 
public int getWho ( ) {
	
	  return who;
	  
} // end-method
/**
 *
 *
 */
 				
public boolean isEqual(String i_name) {
	  
	  // When the strings are equal
	  if  (i_name.equals(name)) {

				// say is equal
		   	return true;
	  }    
	  else {
				// not equal
		   return false;

	  } // end-if
	  
} // end-method            
/**
 *
 *  set the table, with a queue table
 */
 
public void setNew(String u_name,
					         QueueHeader u_agent,
					         int u_who,
					         int u_que,   
					         QueueHeader[] u_qtbl) {
				  
	  name     = u_name;         
	  agent    = u_agent; 
	  who      = u_who;
	  nbr_que  = u_que;                            
	  qtbl     = u_qtbl;                       
					
} // end-method
} // end-class 
