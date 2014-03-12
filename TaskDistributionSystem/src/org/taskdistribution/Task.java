package org.taskdistribution;

import java.io.Serializable;

public abstract interface Task extends Serializable
{
  public abstract Object execute();
}