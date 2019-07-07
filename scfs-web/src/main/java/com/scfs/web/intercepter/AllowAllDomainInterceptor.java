package com.scfs.web.intercepter;

import com.scfs.domain.common.entity.MonitorLog;
import com.scfs.service.common.MonitorService;
import com.scfs.service.support.ServiceSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.scfs.service.support.ServiceSupport.moniter;

public class AllowAllDomainInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	private MonitorService monitorService;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// response.addHeader("Access-Control-Allow-Origin","*");//允许所有域名来源访问，仅用于开发链条
		// response.addHeader("Access-Control-Allow-Headers","x-requested-with,content-type");
		MonitorLog monitorLog = new MonitorLog();
		monitorLog.setUrl(request.getRequestURI());
		monitorLog.setStartTime(System.currentTimeMillis());
		moniter.set(monitorLog);
		return true;
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		MonitorLog monitorLog = moniter.get();
		long endTime = System.currentTimeMillis();
		int time = (int) (endTime - monitorLog.getStartTime());
		if (time > 3000) {// 超过1秒的请求记录下来
			monitorLog.setTime(time);
			monitorService.insert(monitorLog);
		}
		moniter.remove();
		ServiceSupport.loginUserContext.remove();
	}

}
