package com.gd.filter.web;

/**
 * 
 * @author Sandy
 * 创建一个Filter接口
 */

public interface Filter {
	void doFilter(Request  request,Response  responses,FilterChain chain);

}
