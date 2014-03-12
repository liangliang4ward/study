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

public class Demo3 
		implements DemoCall {

/**
 * 
 */

public Demo3() {
	
} // end-constructor
/**
 * 
 * @return java.lang.Object
 * @param in java.lang.Object
 * @param FrameWorkInterface
 * @exception java.lang.Throwable Your exception
 */

public Object doWork(Object in, FrameWorkInterface fw) 
				throws  java.lang.Throwable {

		// return string
		String S = "(Demo3="; 

		try {
				// Should be a string passed
				if  (in instanceof String)	{
						
						// ok
						S = S + (String) in + ")";
				}
				else {
						// nothing passed							
						S = S + "nothing passed)";

				} // endif
	
				// back to caller
				return (Object)S;  

		} // end-try
	 
		catch (java.lang.Throwable e) {
					
				//e.printStackTrace(System.out);
				
				throw  e; 
		} // end-catch	

} // end-method
} // end-class
