/*
file of LaBeeFramework
https://www.bee-wkspace.com/

Copyright (C) 2016- Naoki Yoshioka (ARTS Laboratory)
http://www.arts-lab.net/dev/

This library is free software; you can redistribute it and/or 
modify it under the terms of the GNU Lesser General Public License 
as published by the Free Software Foundation; 
either version 3 of the License, or any later version.

This library is distributed in the hope that it will be useful, 
but WITHOUT ANY WARRANTY; without even the implied warranty of 
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
See the GNU Lesser General Public License for more details.
*/
package com.bee_wkspace.labee_fw.common;

import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.bee_wkspace.labee_fw.common.context.MailServerContext;

/**
 * メール送信イティリティクラス。
 *
 * @author ARTS Laboratory
 *
 * $Id:
 */
public class MailSenderUtil {

	/** メール送信時の文字コード */
	public static final String MAIL_ENCODE = "ISO-2022-JP";

	/** メールヘッダー1 */
	private static final String HEADER1 = "X-Mailer";

	/** メールヘッダー2 */
	private static final String HEADER2 = "LaBeeMailSender 0.1";

	/** メールサーバ接続情報 */
	private MailServerContext serverContext = null;

	/**
	 * コンストラクタ
	 * 
	 * @param serverContext メールサーバ接続情報
	 */
	public MailSenderUtil(MailServerContext serverContext) {
		this.serverContext = serverContext;
	}

	/**
	 * メール送信実行。
	 * 
	 * @param destMailAddr 送信先メールアドレス
	 * @param subject メール題名
	 * @param mailText メール本文
	 * @throws Exception 例外
	 */
	public void sendMail(String destMailAddr, String subject, String mailText) throws Exception {
		if (serverContext.isSendFlg() == false) {
			return;
		}
		final Properties props = new Properties();
		props.setProperty("mail.smtp.host", serverContext.getServerHost());
		props.setProperty("mail.smtp.port", serverContext.getServerPort());
		props.setProperty("mail.smtp.connectiontimeout", serverContext.getTimeOut());
		props.setProperty("mail.smtp.timeout", serverContext.getTimeOut());

		// 認証アリの場合
		if (serverContext.isAuthFlg() && StringUtil.isNull(serverContext.getMailAccount())
				&& StringUtil.isNull(serverContext.getMailPassWord())) {
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.transport.protocol", "smtps");

			// SSL暗号化する場合
			if (serverContext.isSslFlg()) {
				props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
				props.put("mail.smtp.ssl.enable", "true");
				props.put("mail.smtp.socketFactory.port", serverContext.getServerPort());
			} else {
				props.setProperty("mail.smtp.starttls.enable", "true");
			}

			props.setProperty("mail.smtp.socketFactory.fallback", "false");

		} else {
			props.setProperty("mail.smtp.auth", "false");
		}

		Session session = Session.getInstance(props, new smtpAuth());
		session.setDebug(false);
		final MimeMessage message = new MimeMessage(session);
		try {
			Address addrFrom = new InternetAddress(serverContext.getSenderMailAddr(),
					serverContext.getSendersName(), MAIL_ENCODE);
			message.setFrom(addrFrom);

			Address addrTo = new InternetAddress(destMailAddr, destMailAddr, MAIL_ENCODE);
			message.addRecipient(Message.RecipientType.TO, addrTo);
			message.setSubject(subject, MAIL_ENCODE);

			message.setText(mailText, MAIL_ENCODE);
			message.setHeader("Content-Transfer-Encoding", "7bit");
			message.addHeader(HEADER1, HEADER2);
			message.setSentDate(new Date());

			Transport.send(message);

		} catch (AuthenticationFailedException e) {
			LogWriter.error(e, "認証失敗");
			throw e;

		} catch (MessagingException e) {
			LogWriter.error(e, "SMTPサーバー接続失敗");
			throw e;

		} catch (Exception e) {
			LogWriter.error(e);
			throw e;
		}
	}

	/**
	 * SMTP認証承認クラス
	 * 
	 */
	class smtpAuth extends Authenticator {
		@SuppressWarnings("synthetic-access")
		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(serverContext.getMailAccount(), serverContext.getMailPassWord());
		}
	}

}
