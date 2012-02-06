package org.nutz.ztask;

import static org.nutz.web.Webs.Err.*;

import org.nutz.web.WebException;
import org.nutz.ztask.api.Hook;

/**
 * 封装了本应用全部得错误
 * 
 * @author zozoh(zozohtnt@gmail.com)
 */
public abstract class Err {

	/**
	 * 钩子服务相关的错误
	 */
	public static class H {

		public static WebException NO_HANDLER(String handler) {
			return create("e.h.no_handler").reason(handler);
		}

		public static WebException NO_HANDLER(Hook hook) {
			return create("e.h.no_handler").reason(hook.toString());
		}

		public static WebException NULL_TYPE(Hook hook) {
			return create("e.h.null_type").reason(hook.toString());
		}

	}

	/**
	 * 任务相关的错误
	 */
	public static class T {

		public static WebException NO_EXISTS(String taskId) {
			return create("e.t.no_exists").reason(taskId);
		}

		public static WebException BLANK_TASK() {
			return create("e.t.blank_task");
		}

		public static WebException SHORT_TASK(int len) {
			return create("e.t.short_task").reason(len);
		}

		public static WebException LONG_TASK(int len) {
			return create("e.t.long_task").reason(len);
		}

		public static WebException SELF_PARENT(String taskId) {
			return create("e.t.self_parent").reason(taskId);
		}
	}

	/**
	 * 任务堆栈相关的错误
	 */
	public static class S {

		public static WebException NO_EXISTS(String name) {
			return create("e.s.no_exists").reason(name);
		}

		public static WebException EXISTS(String name) {
			return create("e.s.exists").reason(name);
		}

		public static WebException BLANK_NAME() {
			return create("e.s.blank_name");
		}

	}

	/**
	 * 用户相关的错误
	 */
	public static class U {

		public static WebException INVALID_LOGIN() {
			return create("e.u.invalid_login");
		}

		public static WebException NO_EXISTS(String name) {
			return create("e.u.no_exists").reason(name);
		}

		public static WebException EXISTS(String name) {
			return create("e.u.exists").reason(name);
		}

	}

}
