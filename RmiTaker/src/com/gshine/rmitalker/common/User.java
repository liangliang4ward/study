package com.gshine.rmitalker.common;

import java.io.Serializable;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2008</p>
 * <p>Company: dlut</p>
 * @author g.shine
 * @version 1.0
 */

public class User
    implements Serializable {

  private String id;
  private String name;
  private String realname;
  private String pwd;
  private String sex;
  private boolean online;
  
  private String message=null;

  public User() {
  }

  public User(String id, String name, String realname, String pwd, String sex) {
    this.id = id;
    this.name = name;
    this.realname = realname;
    this.pwd = pwd;
    this.sex = sex;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRealname() {
    return this.realname;
  }

  public void setRealname(String realname) {
    this.realname = realname;
  }

  public String getPwd() {
    return this.pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public String getSex() {
    return this.sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public boolean getOnline() {
    return this.online;
  }

  public void setOnline(boolean online) {
    this.online = online;
  }

  public String toString() {
    String s = "(id=" + id + ";name=" + name + ";realname=" + realname +
        ";sex=" + sex + ";online=" + online + ")";
    return s;
  }
  
  public void setMessage(String msg){
	  this.message=msg;
  }
  public String getMessage(){
	  return this.message;
  }
}