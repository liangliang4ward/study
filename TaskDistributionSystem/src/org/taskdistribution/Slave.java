package org.taskdistribution;

public abstract interface Slave
{
  public abstract Object handle(Task paramTask);
}