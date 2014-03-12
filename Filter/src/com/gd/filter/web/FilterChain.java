package com.gd.filter.web;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Sandy
 * 过滤器链
 */
public class FilterChain implements Filter {
	List<Filter> filters = null;

	int index = 0;

	public FilterChain() {
		super();
		filters = new ArrayList<Filter>();
	}

	/*往filter链中添加一个过滤器，这里用到了个一个技巧就是返回this对象，这样可以实现添加完成一个
	 *过滤器链后，还可以紧接着添加下一个filter
	 *this.add(filter1).add(filter2)的方式
	 */
	public FilterChain add(Filter filter) {
		this.filters.add(filter);
		return this;
	}
	//过滤方法
	public void doFilter(Request request, Response response, FilterChain chain) {
		if (index == filters.size())
			return;
		Filter filter = filters.get(index);
		index++;
		filter.doFilter(request, response, chain);
	}
}
