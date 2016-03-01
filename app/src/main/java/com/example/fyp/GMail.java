package com.example.fyp;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Created by jadephong on 21/1/2016.
 */
public class GMail extends javax.mail.Authenticator {

    private String[] recipients;
    private String randomPassword;
    private static String senderEmail = "tarcnavigator2016@gmail.com";
    private static String senderPassword = "navigator2016";
    private static String host = "smtp.googlemail.com";
    private static String port = "587";
    private static String subject = "Tarc Navigator Password Recovery";


    public static void main(String... args) {
        GMail.sendMail(new String[]{"jadephong1008@gmail.com"}, "123");
    }

    public static boolean sendMail(String[] recipients, String randomPassword) {
        String[] s2 = null;
        for (String parts : recipients) {
            s2 = parts.split("@");
        }
        String body = "Hi " + s2[0] + ",\n\n" +
                "We've received a request to reset the password.\n" +
                "A random password had been created. Please use this password to recover your account.\n" +
                "Password:" + randomPassword +
                "\n\nRegards,\nTarcNavigator";
        //This is for google
        if (GMail.sendMail(senderEmail, senderPassword, host,
                port, "true", "true",
                true, "javax.net.ssl.SSLSocketFactory", "false",
                recipients,
                subject,
                body)) {
            return true;
        } else {
            return false;
        }

    }

    public synchronized static boolean sendMail(
            String userName, String passWord, String host,
            String port, String starttls, String auth,
            boolean debug, String socketFactoryClass, String fallback,
            String[] to,
            String subject, String text) {
        Properties props = new Properties();
        //Properties props=System.getProperties();
        props.put("mail.smtp.user", userName);
        props.put("mail.smtp.host", host);
        if (!"".equals(port))
            props.put("mail.smtp.port", port);
        if (!"".equals(starttls))
            props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.auth", auth);
        if (debug) {
            props.put("mail.smtp.debug", "true");
        } else {
            props.put("mail.smtp.debug", "false");
        }

        if (!"".equals(fallback))
            props.put("mail.smtp.socketFactory.fallback", fallback);
        props.put("mail.smtp.starttls.enable", "true");
        try {
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(debug);
            MimeMessage msg = new MimeMessage(session);
            msg.setText(text);
            msg.setSubject(subject);
            msg.setFrom(new InternetAddress(senderEmail));
            for (int i = 0; i < to.length; i++) {
                msg.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(to[i]));
            }
            msg.saveChanges();
            Transport transport = session.getTransport("smtp");
            transport.connect(host, userName, passWord);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            return true;
        } catch (Exception mex) {
            mex.printStackTrace();
            return false;
        }
    }

}