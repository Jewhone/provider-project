package com.zhp.jewhone.core.util;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailUtil {
    private MimeMessage mimeMsg;
    private Session session;
    private Properties props;
    private String username;
    private String password;
    private Multipart mp;
    private String smtp;
    private String formAddress;

    public void initMailSession() {
        if (props == null) {
            props = System.getProperties();
        }
        props.put("mail.smtp.host", smtp);
        createMimeMessage();
    }

    public boolean createMimeMessage() {
        try {
            session = Session.getDefaultInstance(props, null);
        } catch (Exception e) {
            return false;
        }
        try {
            mimeMsg = new MimeMessage(session);
            setFrom();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* 定义SMTP是否需要验证 */
    public void setNeedAuth(boolean need) {
        if (props == null)
            props = System.getProperties();
        if (need) {
            props.put("mail.smtp.auth", "true");
        } else {
            props.put("mail.smtp.auth", "false");
        }
    }

    public void setNamePass(String name, String pass) {
        username = name;
        password = pass;
    }

    /* 定义邮件主题 */
    public boolean setSubject(String mailSubject) {
        try {
            mimeMsg.setSubject(mailSubject);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* 定义邮件正文 */
    public boolean setBody(String mailBody) {
        try {
            mp = new MimeMultipart();
            BodyPart bp = new MimeBodyPart();
            bp.setContent(mailBody, "text/html;charset=UTF-8");
            mp.addBodyPart(bp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* 设置发信人 */
    public boolean setFrom() {
        try {
            mimeMsg.setFrom(new InternetAddress(formAddress)); // 发信人
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* 定义收信人 */
    public boolean setTo(String to) {
        if (to == null)
            return false;
        try {
            mimeMsg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* 定义抄送人 */
    public boolean setCopyTo(String copyto) {
        if (copyto == null)
            return false;
        try {
            mimeMsg.setRecipients(Message.RecipientType.CC,
                    (Address[]) InternetAddress.parse(copyto));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /* 发送邮件模块 */
    public boolean sendOut(String to,
                           String copyto, String subject, String content) {
        try {
            setSubject(subject);
            setBody(content);
            setTo(to);
            setCopyTo(copyto);
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            Session mailSession = Session.getInstance(props, null);
            Transport transport = mailSession.getTransport("smtp");
            transport.connect((String) props.get("mail.smtp.host"), username, password);
            transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
            transport.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("邮件失败！" + e);
            return false;
        }
    }

    public boolean sendOut() {
        try {
            mimeMsg.setContent(mp);
            mimeMsg.saveChanges();
            Session mailSession = Session.getInstance(props, null);
            Transport transport = mailSession.getTransport("smtp");
            transport.connect((String) props.get("mail.smtp.host"), username, password);
            transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
            transport.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("邮件失败！" + e);
            return false;
        }
    }


    /* 调用sendOut方法完成发送 */
    public static boolean sendAndCc(String smtp, String from, String to, String copyto, String subject, String content, String username, String password) {
        MailUtil theMail = new MailUtil();
        theMail.setSmtp(smtp);
        theMail.setFormAddress(from);
        theMail.initMailSession();
        theMail.setFrom();
        theMail.setNeedAuth(true); // 验证
        if (!theMail.setSubject(subject))
            return false;
        if (!theMail.setBody(content))
            return false;
        if (!theMail.setTo(to))
            return false;
        if (!theMail.setCopyTo(copyto))
            return false;
        theMail.setNamePass(username, password);
        if (!theMail.sendOut())
            return false;
        return true;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFormAddress() {
        return formAddress;
    }

    public void setFormAddress(String formAddress) {
        this.formAddress = formAddress;
    }
}