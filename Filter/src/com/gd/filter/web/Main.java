package com.gd.filter.web;

import com.gd.filter.web.FilterChain;
import com.gd.filter.web.HtmlFilter;
import com.gd.filter.web.Request;
import com.gd.filter.web.Response;
import com.gd.filter.web.SensitiveFilter;

/**
 * 
 * @author Sandy
 * 主客户端测试程序
 */
public class Main {

	/**
	 * @param args
	 */
	public static final String NEW_STRING = "<javaSrcipt>,被抓住，被扣留";

	public static void main(String[] args) {
		Request request = new Request();
		request.setRequestString(NEW_STRING);
		Response response = new Response();
		response.setResponseString("response--------");
		FilterChain chain = new FilterChain();
		chain.add(new HtmlFilter()).add(new SensitiveFilter());
		chain.doFilter(request, response, chain);
		System.out.println(request.getRequestString());
		System.out.println(response.getResponseString());
	}
}
