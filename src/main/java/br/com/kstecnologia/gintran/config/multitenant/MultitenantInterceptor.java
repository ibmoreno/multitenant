package br.com.kstecnologia.gintran.config.multitenant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class MultitenantInterceptor extends HandlerInterceptorAdapter {

	private static final String TENANT_HEADER_NAME = "X-TENANT-ID";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String tenantId = request.getHeader(TENANT_HEADER_NAME);
		MultitenantContext.setTenantId(tenantId);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		MultitenantContext.clear();
	}

}
