/*
 * SGX Public License Notice This software is the intellectual property of SGX.
 * The program may be used only in accordance with the terms of the license
 * agreement you entered into with SGX. 2003 Singapore Exchange Pte Ltd (SGX).
 * All rights reserved. 2 Shenton Way / #22-00 SGX Centre 1 Singapore( 068804 )
 */
package com.hitangjun.rxtx.util.demo;

/**
 * @author John
 * @since Jul 30, 2010
 */
import java.util.Observable;
import java.util.Observer;

public class CommDataObserver implements Observer
{
    String name;

    public CommDataObserver( String name )
    {
        this.name = name;
    }

    public void update( Observable o, Object arg )
    {
        System.out.println("读取到的数据位数："+(( byte[] ) arg).length);
        System.out.println( "[" + name + "] GetMessage:\n ["
            + new String( ( byte[] ) arg ) + "]" );
    }
}
