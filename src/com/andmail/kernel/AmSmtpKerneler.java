package com.andmail.kernel;

import com.andmail.api.MailKerneler;
import com.andmail.exception.MailException;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 基于SMTP的邮件
 */
public class AmSmtpKerneler implements MailKerneler {
	
	private String host;// smtp服务器
	private String auth;// 验证

	private String mailAcount;// 邮件用户名
	private String password;// 密码

	// 共同变量
	private Properties props;
	private Session session;
	private MimeMessage mimeMessage;

	/**
	 * 构造函数
	 */
	AmSmtpKerneler(String host, String mailAcount, String password) {
		this.host = host;
		this.auth = "true";
		this.mailAcount = mailAcount;
		this.password = password;
	}

	/**
	 * 变量初始化
	 */
	protected void initialize() {
		props = System.getProperties();
		// 指定smtp服务器
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", auth);
		// 新建一个包含smtp会话的MimeMessage对象
		session = Session.getDefaultInstance(props, null);
		mimeMessage = new MimeMessage(session);
	}

	/**
	 * 设定邮件信息
	 */
	@Override
	public MailKerneler create(String from, String to) throws Exception {
		// 初始化
		initialize();
		if (from.equals("") || to.equals("")) {
			throw new MailException("输入有误");
		} else {
			// 指定送信人
			mimeMessage.setFrom(new InternetAddress(from));
			// 对方邮件地址
			mimeMessage.setRecipients(Message.RecipientType.TO, to);
		}
		return this;
	}

	/**
	 * 邮件格式，和内容指定
	 */
	@Override
	public MailKerneler setContent(String subject, String content) throws Exception {
		// 邮件标题
		mimeMessage.setSubject(subject, "GBK");
		// 指定邮件格式
		mimeMessage.setHeader("Content-Type", "text/html");
		// 邮件内容
		mimeMessage.setText(content);
		return this;
	}

	/**
	 * 发信
	 */
	@Override
	public MailKerneler send() throws Exception {
		// 指定送信日期
		mimeMessage.setSentDate(new Date());
		// 送信
		Transport transport = session.getTransport("smtp");
		transport.connect(host, mailAcount, password);
		transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
		transport.close();
		return this;
	}

}
