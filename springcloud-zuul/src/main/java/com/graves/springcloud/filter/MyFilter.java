/**
 * 
 */
package com.graves.springcloud.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 
 * filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
	pre：路由之前
	routing：路由之时
	post： 路由之后
	error：发送错误调用
  filterOrder：过滤的顺序
  shouldFilter：这里可以写逻辑判断，是否要过滤，本文true,永远过滤。
  run：过滤器的具体逻辑。可用很复杂，包括查sql，nosql去判断该请求到底有没有权限访问
 *   
 *   
 * <p>zuul Filter</p>  
 * <p>Description: </p>  
 * @author Graves  
 * @date 2019年8月6日  
 */
@Component
public class MyFilter extends ZuulFilter{
	
	private static Logger log = LoggerFactory.getLogger(MyFilter.class);
	
	/**  
	 * shouldFilter：这里可以写逻辑判断，是否要过滤，本文true,永远过滤。
	 * <p>Title: shouldFilter</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年8月6日   
	 * @return  
	 */ 
	@Override
	public boolean shouldFilter() {
		
		RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        Object name = request.getParameter("name");
        
        if (name.toString().equals("graves")) {
			return false;
		}
		return true;
	}

	/**  
	 * run：过滤器的具体逻辑。可用很复杂，包括查sql，nosql去判断该请求到底有没有权限访问
	 * <p>Title: run</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年8月6日   
	 * @return
	 * @throws ZuulException  
	 */ 
	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info(String.format("%s >>> %s", request.getMethod(), request.getRequestURL().toString()));
        Object accessToken = request.getParameter("token");
        if(accessToken == null) {
            log.warn("token is empty");
            ctx.setSendZuulResponse(false);
            ctx.setResponseStatusCode(401);
            try {
                ctx.getResponse().getWriter().write("token is empty");
            }catch (Exception e){}

            return null;
        }
        log.info("ok");
        return null;
	}

	/** 
	 * 
	 *  filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
		pre：路由之前
		routing：路由之时
		post： 路由之后
		error：发送错误调用
	 * <p>Title: filterType</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年8月6日   
	 * @return  
	 */ 
	@Override
	public String filterType() {
		return "pre";
	}

	/**  
	 * filterOrder：过滤的顺序
	 * <p>Title: filterOrder</p>  
	 * <p>Description: </p>  
	 * @author Graves
	 * @date 2019年8月6日   
	 * @return  
	 */ 
	@Override
	public int filterOrder() {
		return 0;
	}
	
}
