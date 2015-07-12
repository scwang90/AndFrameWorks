package com.andmail.kernel;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.andframe.exception.AfToastException;
/**
 * 基于SMTP的邮件
 */
public class MailBySmtp {
	
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
	public MailBySmtp(String host, String mailAcount, String password) {
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
	 * @param from
	 *            ,to,subject
	 * @throws MessagingException
	 * @throws AddressException
	 * @throws UnsupportedEncodingException
	 */
	public void create(String from, String to, String subject) throws Exception {
		// 初始化
		initialize();
		if (from.equals("") || to.equals("") || subject.equals("")) {
			throw new AfToastException("输入有误");
		} else {
			// 指定送信人
			mimeMessage.setFrom(new InternetAddress(from));
			// 对方邮件地址
			mimeMessage.setRecipients(Message.RecipientType.TO, to);
			// 邮件标题
			mimeMessage.setSubject(subject, "GBK");
		}
	}

	/**
	 * 邮件格式，和内容指定
	 * @param content
	 * @throws MessagingException
	 */
	public void addContent(String content) throws MessagingException {
		// 指定邮件格式
		mimeMessage.setHeader("Content-Type", "text/html");
		// 邮件内容
		mimeMessage.setText(content);
	}

	/**
	 * 发信
	 * @throws MessagingException
	 */
	public void send() throws MessagingException {
		// 指定送信日期
		mimeMessage.setSentDate(new Date());
		// 送信
		Transport transport = session.getTransport("smtp");
		transport.connect(host, mailAcount, password);
		transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
		transport.close();
	}

}
