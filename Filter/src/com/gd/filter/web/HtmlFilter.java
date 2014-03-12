package com.gd.filter.web;

/**
 * 
 * @author Sandy 
 * html中特殊字符替换过滤器
 */
public class HtmlFilter implements Filter {

	public void doFilter(Request request, Response response, FilterChain chain) {

		request.requestString = request.requestString.replace("<", "[")
				.replace(">", "]")
				+ "------HtmlFilter()";
		chain.doFilter(request, response, chain);
		response.responseString += "-------------+HtmlFilter()";
	}

}
