package org.nutz.ztask.util;

import java.util.Date;
import java.util.List;

import org.apache.commons.mail.SimpleEmail;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.mail.MailObj;
import org.nutz.ztask.api.SmtpInfo;
import org.nutz.ztask.api.User;
import org.nutz.ztask.impl.mongo.MongoMailObj;

/**
 * zTask 的一些帮助函数和常量
 * 
 * @author zozoh(zozohtnt@gmail.com)
 */
public abstract class ZTasks {

	public static final String REG_NOWORD = "[ \t\r\b\n~!@#$%^&*()+=`:{}|\\[\\]\\\\:\"';<>?,./-]";

	public static final String REG_D = "[0-9]{4}-[01][0-9]-[0-3][0-9][ ][0-2][0-9]:[0-5][0-9]:[0-5][0-9]";

	/**
	 * TASK 的 stack 字段，什么值表示 null
	 */
	public static final String NULL_STACK = "--";

	/**
	 * 生成一个邮件对象
	 * 
	 * @param subject
	 *            邮件标题
	 * @param text
	 *            邮件正文
	 * @return 邮件对象
	 */
	public static MailObj textMail(String subject, String text) {
		MongoMailObj mo = new MongoMailObj();
		mo.setSubject(subject);
		mo.setMailBody(text);
		return mo;
	}

	/**
	 * 根据 smtp 发送邮件
	 * 
	 * @param smtp
	 *            SMTP 服务信息
	 * @param subject
	 *            邮件主体
	 * @param text
	 *            邮件正文
	 * @param usrs
	 *            收信人
	 * @return 发送结果，null 表失败
	 */
	public static String sendTextMail(SmtpInfo smtp, String subject, String text, List<User> usrs) {

		if (!smtp.isAvaliable())
			return null;

		// 准备返回
		String re = null;

		try {
			SimpleEmail email = new SimpleEmail();

			if (smtp.getTimeout() > 0)
				email.setSocketTimeout(smtp.getTimeout());

			// 邮件基本设置
			email.setCharset("UTF-8");
			email.setHostName(smtp.getHost());
			email.setAuthentication(smtp.getAccount(), smtp.getPassword());

			// 发件人
			if (Strings.isBlank(smtp.getAlias()))
				email.setFrom(smtp.getAccount());
			else
				email.setFrom(smtp.getAccount(), smtp.getAlias());

			// 收件人
			int count = 0;
			for (User u : usrs) {
				if (null != u.getEmail()) {
					count++;
					email.addTo(u.getEmail().toString(), u.getName());
				}
			}

			// 撰写发送
			if (count > 0) {
				email.setSubject(subject);
				email.setMsg(text);
				re = email.send();
			}
		}
		catch (Throwable e) {}

		// 返回发送结果
		return re;
	}

	/**
	 * 将一段 comment 文本进行包裹，在其开头加上 '@xxx:', 在其结尾加上时间戳
	 * <p>
	 * 比如:
	 * 
	 * <pre>
	 * 文本 :  "Hello"  包裹后将变成 "@zzh: Hello //2012-02-07 00:00:00"
	 * 文本 :  "@zzh: Hello"  包裹后将变成 "@zzh: Hello //2012-02-07 00:00:00"
	 * 文本 :  "@zzh: Hello //2011-12-07 06:12:00"  包裹后将变成 "@zzh: Hello //2012-02-07 00:00:00"
	 * </pre>
	 * 
	 * @param text
	 *            要被包裹的文本
	 * @param unm
	 *            用户，如果为 null，将不处理前缀
	 * @param d
	 *            时间戳，如果为 null，将不处理后缀
	 * @return 包裹后的字符串
	 */
	public static String wrapComment(String text, String unm, Date d) {
		// 处理空串
		text = Strings.sNull(Strings.trim(text), "");
		// 处理前缀
		if (!Strings.isBlank(unm)) {
			text = "@" + unm + ": " + text.replaceAll("^@[^ :,]+: ", "");
		}
		// 处理后缀
		if (null != d) {
			String ds = Times.sDT(d);
			text = text.replaceAll("[ \t]?//[ \t]*" + REG_D + "$", "") + " //" + ds;
		}
		return text;
	}

	/**
	 * 用当前时间作为时间戳，包裹 comment 文本
	 * 
	 * @param text
	 *            要被包裹的文本
	 * @param unm
	 *            用户，如果为 null，将不处理前缀
	 * @return @return 包裹后的字符串
	 * @see org.nutz.ztask.util.ZTasks#wrapComment(String, String, Date)
	 */
	public static String wrapComment(String text, String unm) {
		return wrapComment(text, unm, Times.now());
	}

	/**
	 * 判断一个堆栈名是否为空
	 * 
	 * @param stackName
	 *            堆栈名
	 * @return 是否是有效堆栈名
	 */
	public static boolean isBlankStack(String stackName) {
		return Strings.isBlank(stackName) || ZTasks.NULL_STACK.equals(stackName);
	}

}