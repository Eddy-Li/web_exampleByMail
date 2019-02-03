package com.myit.service;

import java.util.Map;

public interface MailService {
    /**
     * Text或者HTML格式邮件的方法
     *
     * @param to      目的地
     * @param subject 邮件的主题也就是邮件的标题
     * @param content 要发送的内容
     * @param isText  如果为true则代表发送HTML格式的文本
     * @param attachmentFile 附件文件
     */
    public Boolean sendMail(String to, String subject, String content, Boolean isText, String attachmentFile);

    Boolean sendMailFreeMarker(String to, String subject, Map contentMap, String freeMarkerTemplate, String attachmentFile);
}
