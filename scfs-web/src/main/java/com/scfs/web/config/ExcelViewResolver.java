package com.scfs.web.config;

import org.springframework.beans.BeanUtils;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.util.Locale;

/**
 * Created by Administrator on 2016/11/3.
 */
public class ExcelViewResolver extends UrlBasedViewResolver {

	@Override
	protected View loadView(String viewName, Locale locale) throws Exception {
		JxlsView view = buildJxlsView(viewName);
		View result = applyLifecycleMethods(viewName, view);
		return result;
	}

	private JxlsView buildJxlsView(String viewName) {
		JxlsView view = (JxlsView) BeanUtils.instantiateClass(getViewClass());
		view.setUrl(getPrefix() + viewName + getSuffix());
		String contentType = getContentType();
		if (contentType != null) {
			view.setContentType(contentType);
		}
		view.setRequestContextAttribute(getRequestContextAttribute());
		view.setAttributesMap(getAttributesMap());
		return view;
	}

	private View applyLifecycleMethods(String viewName, AbstractView view) {
		return (View) getApplicationContext().getAutowireCapableBeanFactory().initializeBean(view, viewName);
	}
}
