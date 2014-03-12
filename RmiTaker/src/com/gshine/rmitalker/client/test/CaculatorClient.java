package com.gshine.rmitalker.client.test;

import java.rmi.Naming;
import com.gshine.rmitalker.server.test.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: dlut</p>
 * @author g.shine
 * @version 1.0
 */

public class CaculatorClient {
  public CaculatorClient() {
    try {
      Caculator c=(Caculator)Naming.lookup("rmi://192.168.100.105:1099/caculator");
      System.out.println(c.add(12,25));
      System.out.println(c.div(12,2));
      System.out.println(c.sub(12,25));
      System.out.println(c.mul(12,25));
    }
    catch (Exception e) {
          e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    CaculatorClient caculatorClient1 = new CaculatorClient();
  }

}