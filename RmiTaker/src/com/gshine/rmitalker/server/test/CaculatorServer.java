package com.gshine.rmitalker.server.test;


import java.rmi.Naming;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: dlut</p>
 * @author g.shine
 * @version 1.0
 */

public class CaculatorServer {
  public CaculatorServer() {
    try{
      CaculatorImpl c=new CaculatorImpl();
      Naming.bind("caculator",c);
    }catch(Exception e){
      e.printStackTrace();
    }
  }
  public static void main(String[] args) {
    CaculatorServer caculatorServer1 = new CaculatorServer();
  }

}