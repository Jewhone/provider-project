package com.zhp.jewhone.core.util;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * 发送邮件工具类
 */
public class SendMailUtil {
    private static String user = "184687645@qq.com"; // 发件人地址
    private static String pwd = "sqssrpstmmytcacb"; // 密码
    private static String to = "184687645@qq.com"; // 收件人地址

    public void send(String subject, String txt) {
        Properties props = new Properties();
        // 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        String host = "smtp.qq.com";
        props.put("mail.smtp.host", host);
        // 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", "true");
        // 用刚刚设置好的props对象构建一个session
        Session session = Session.getDefaultInstance(props);
        // 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使
        // 用（你可以在控制台（console)上看到发送邮件的过程）
        session.setDebug(true);
        // 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try {
            // 加载发件人地址
            String from = "logmonitor";
            message.setFrom(new InternetAddress(from));
            // 加载收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            // 加载标题
            message.setSubject(subject);
            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();

            // 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setText(txt);
            multipart.addBodyPart(contentPart);

            // 添加附件
            // BodyPart messageBodyPart = new MimeBodyPart();
            // DataSource source = new FileDataSource(affix);
            // 添加附件的内容
            // messageBodyPart.setDataHandler(new DataHandler(source));
            // 添加附件的标题
            // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
            // sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
            // messageBodyPart.setFileName("=?GBK?B?"+
            // enc.encode(affixName.getBytes()) + "?=");
            // multipart.addBodyPart(messageBodyPart);

            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();
            // 发送邮件
            Transport transport = session.getTransport("smtp");
            // 连接服务器的邮箱
            transport.connect(host, user, pwd);
            // 把邮件发送出去
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 发送邮件
     * @param content
     * @return
     */
    public static boolean send(String content) {
    	try {
    		Properties props = new Properties();

            // 开启debug调试
            props.setProperty("mail.debug", "false");
            // 发送服务器需要身份验证
            props.setProperty("mail.smtp.auth", "true");
            // 设置邮件服务器主机名
            props.setProperty("mail.host", "smtp.qq.com");
            // 发送邮件协议名称
            props.setProperty("mail.transport.protocol", "smtp");

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.ssl.socketFactory", sf);

            Session session = Session.getInstance(props);

            Message msg = new MimeMessage(session);
            msg.setSubject("zhp-provider-project error");
            msg.setText(content.toString());
            msg.setFrom(new InternetAddress(user));

            Transport transport = session.getTransport();
            transport.connect("smtp.qq.com", user, pwd);

            transport.sendMessage(msg, new Address[] { new InternetAddress(to) });
            transport.close();
            return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
    }
    public static void main(String[] args) throws MessagingException, GeneralSecurityException {
        Properties props = new Properties();

        // 开启debug调试
        props.setProperty("mail.debug", "true");
        // 发送服务器需要身份验证
        props.setProperty("mail.smtp.auth", "true");
        // 设置邮件服务器主机名
        props.setProperty("mail.host", "smtp.qq.com");
        // 发送邮件协议名称
        props.setProperty("mail.transport.protocol", "smtp");

        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getInstance(props);

        Message msg = new MimeMessage(session);
        msg.setSubject("log zhp-provider-project");
        StringBuilder builder = new StringBuilder();
       builder.append("url = " + "http://blog.csdn.net/never_cxb/article/details/50524571");
        builder.append("\n页面爬虫错误");
        builder.append("\n时间 " + new Date().getTime());
        msg.setText(builder.toString());
        msg.setFrom(new InternetAddress(user));

        Transport transport = session.getTransport();
        transport.connect("smtp.qq.com", user, pwd);

        transport.sendMessage(msg, new Address[] { new InternetAddress(to) });
        transport.close();
      }

}