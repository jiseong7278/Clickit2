package com.project.clickit.util;

import com.project.clickit.exceptions.ErrorCode;
import com.project.clickit.exceptions.login.MailSendFailedException;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String text) {
        try{
            MimeMessage message = createEmailForm(to, subject, text);
            javaMailSender.send(message);
        }catch (Exception e) {
            throw new MailSendFailedException(ErrorCode.MAIL_SEND_FAILED);
        }
    }

    private MimeMessage createEmailForm(String to, String subject, String text) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject(subject);
        message.setText(text);

        message.setFrom(new InternetAddress("jisung7278@naver.com"));

        return message;
    }
}
