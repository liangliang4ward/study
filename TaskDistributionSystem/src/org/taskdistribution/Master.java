package org.taskdistribution;

public abstract interface Master
{
  public abstract Object submit(Task paramTask, long paramLong)
    throws Exception;
}