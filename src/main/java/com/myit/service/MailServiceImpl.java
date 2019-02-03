package com.myit.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private FreeMarkerConfigurationFactoryBean freeMarkerConfigurationFactoryBean;

    private Configuration freeMarkerConfiguration;

    @Value("${mail.smtp.username}")
    private String from;

    @PostConstruct
    public void init() {
        try {
            freeMarkerConfiguration = freeMarkerConfigurationFactoryBean.createConfiguration();
            System.out.println("freemarker.template.Configuration:初始化成功===>" + freeMarkerConfiguration);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
            System.out.println("freemarker.template.Configuration:初始失败");
        }
    }


    /**
     * Text或者HTML格式邮件的方法
     *
     * @param to             目的地
     * @param subject        邮件的主题也就是邮件的标题
     * @param content        要发送的内容
     * @param isHtml         如果为true则代表发送HTML格式的文本
     * @param attachmentFile 附件文件
     */
    @Override
    public Boolean sendMail(String to, String subject, String content, Boolean isHtml, String attachmentFile) {
        MimeMessage message = javaMailSender.createMimeMessage();// 创建邮件对象
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(from);
            //发送给多个人：setTo(String[] to);
            //cc给一个人:setCc(String cc)
            //CC给多个人：setCc(String[] cc)
            //密送：setBcc(InternetAddress bcc)
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            //邮件的文本内容，true表示文本以html格式打开
            //whether to apply content type "text/html" for an HTML mail, using default content type ("text/plain") else
            if (isHtml) {
                messageHelper.setText(content, true);
            } else {
                messageHelper.setText(content, false);
            }
            File file = new File(attachmentFile);
            messageHelper.addAttachment(file.getName(), file);
            //发送邮件
            javaMailSender.send(message);
            //发送成功
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        //发送失败
        return false;
    }

    @Override
    public Boolean sendMailFreeMarker(String to, String subject, Map contentMap, String freeMarkerTemplate, String attachmentFile) {
        MimeMessage message = javaMailSender.createMimeMessage();// 创建邮件对象
        MimeMessageHelper messageHelper;
        try {
            messageHelper = new MimeMessageHelper(message, true, "UTF-8");
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            File file = new File(attachmentFile);
            messageHelper.addAttachment(file.getName(), file);
            String freeMarker = this.parseFreeMarker(freeMarkerTemplate, contentMap);
            messageHelper.setText(freeMarker, true);
            //发送邮件
            javaMailSender.send(message);
            //发送成功
            return true;
        } catch (MessagingException | TemplateException | IOException e) {
            e.printStackTrace();
        }
        //发送失败
        return false;
    }

    private String parseFreeMarker(String freeMarkerTemplate, Map contentMap) throws IOException, TemplateException {
        Template template = freeMarkerConfiguration.getTemplate(freeMarkerTemplate);
        //解析FreeMarker，将数据填充到FreeMarker中，并返回FreeMarker的字符串
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, contentMap);
    }
}
