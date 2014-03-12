package com.gshine.rmitalker.server.test;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: dlut</p>
 * @author g.shine
 * @version 1.0
 */

public class CaculatorImpl extends UnicastRemoteObject implements Caculator {
  public CaculatorImpl() throws RemoteException {
    super();
  }
  public long add(long op1, long op2) throws RemoteException {
    return op1+op2;
  }
  public long sub(long op1, long op2) throws RemoteException {
    return op1-op2;
  }
  public long mul(long op1, long op2) throws RemoteException {
    return op1*op2;
  }
  public long div(long op1, long op2) throws RemoteException {
    return op1/op2;
  }

}