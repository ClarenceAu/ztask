package org.nutz.ajax;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.ViewWrapper;

public class AjaxCheckSession implements ActionFilter {

	private String name;

	public AjaxCheckSession(String name) {
		this.name = name;
	}

	public View match(ActionContext context) {
		Object obj = context.getRequest().getSession().getAttribute(name);
		if (null == obj) {
			View v = new AjaxView();
			AjaxReturn re = Ajax.expired();
			return new ViewWrapper(v, re);
		}
		return null;
	}

}
