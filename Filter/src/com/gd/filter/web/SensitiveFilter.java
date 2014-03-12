package com.gd.filter.web;

/**
 * @author Sandy 
 * 替换敏感字符的过滤器
 */
public class SensitiveFilter implements Filter {
	public void doFilter(Request request, Response response, FilterChain chain) {
		request.requestString = request.requestString.replace("被抓住", "抓住")
				.replace("被扣留", "扣留")
				+ "----------SensitiveFilter()";
		chain.doFilter(request, response, chain);
		response.responseString += "SensitiveFilter()";
	}
}
