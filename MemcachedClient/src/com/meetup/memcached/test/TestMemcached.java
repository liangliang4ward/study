/**
 * Copyright (c) 2008 Greg Whalin
 * All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the BSD license
 *
 * This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE.
 *
 * You should have received a copy of the BSD License along with this
 * library.
 *
 * @author greg whalin <greg@meetup.com> 
 */
package com.meetup.memcached.test;

import org.junit.Before;
import org.junit.Test;

import com.meetup.memcached.MemcachedClient;
import com.meetup.memcached.SockIOPool;

public class TestMemcached  {  
	MemcachedClient mcc =null;
	@Before
	public void init(){
		String[] servers = { "192.168.2.166:11211","192.168.2.166:11212"};
		SockIOPool pool = SockIOPool.getInstance();
		pool.setServers( servers );
		pool.setFailover( true );
		pool.setInitConn( 10 ); 
		pool.setMinConn( 5 );
		pool.setMaxConn( 250 );
		pool.setMaintSleep( 30 );
		pool.setNagle( false );
		pool.setSocketTO( 3000 );
		pool.setAliveCheck( true );
		pool.initialize();
		mcc = new MemcachedClient();

	}
	@Test
	public void get(){
		System.out.println(mcc.get("key24"));
		System.out.println(mcc.get("key23"));
	}
	
	@Test
	public void set(){
		mcc.set("key24","test13");
//		mcc.set("key21","test14");
	}
	
//	public static void main(String[] args) {
//		      // memcached should be running on port 11211 but NOT on 11212
//
//	
//
//
//		// turn off most memcached client logging:
////		com.meetup.memcached.Logger.getLogger( MemcachedClient.class.getName() ).setLevel( com.meetup.memcached.Logger.LEVEL_WARN );
//		System.out.println(mcc.set("key20","test13"));
//		System.out.println(mcc.set("key21","test14"));
//		System.out.println(mcc.get("key20"));
//		System.out.println(mcc.get("key21"));
////		for ( int i = 0; i < 10; i++ ) {
////			boolean success = mcc.set( "" + i, "Hello!" );
////			String result = (String)mcc.get( "" + i );
////			System.out.println( String.format( "set( %d ): %s", i, success ) );
////			System.out.println( String.format( "get( %d ): %s", i, result ) );
////		}
////
////		System.out.println( "\n\t -- sleeping --\n" );
////		try { Thread.sleep( 10000 ); } catch ( Exception ex ) { }
////
////		for ( int i = 0; i < 10; i++ ) {
////			boolean success = mcc.set( "" + i, "Hello!" );
////			String result = (String)mcc.get( "" + i );
////			System.out.println( String.format( "set( %d ): %s", i, success ) );
////			System.out.println( String.format( "get( %d ): %s", i, result ) );
////		}
//	}
}
