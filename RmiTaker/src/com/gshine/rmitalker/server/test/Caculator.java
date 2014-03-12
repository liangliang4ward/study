package com.gshine.rmitalker.server.test;

import java.rmi.Remote;
import java.rmi.RemoteException;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: dlut</p>
 * @author g.shine
 * @version 1.0
 */

public interface Caculator extends Remote {
  
  public long add(long op1,long op2)
      throws RemoteException;
  
  public long sub(long op1,long op2)
      throws RemoteException;
  
  public long mul(long op1,long op2)
      throws RemoteException;
  
  public long div(long op1,long op2)
      throws RemoteException;
  
}