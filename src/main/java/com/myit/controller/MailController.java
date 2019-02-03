package com.myit.controller;


import com.alibaba.fastjson.JSON;
import com.myit.service.MailService;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MailController {
    @Autowired
    private MailService mailService;

    //发送普通文本或者html文本邮件
    @RequestMapping("/sendmail")
    public String sendMail() {
        String to = "liyanan2010@sina.cn";
        String subject = "sendmail test";
        String content = "hello Eddy!";
        Boolean isHtml = true;
        String attachmentFile = "f:/timg.jpg";
        Boolean ret = mailService.sendMail(to, subject, content, isHtml, attachmentFile);
        Map<String, Object> result = new HashMap<>();
        result.put("ret", ret);
        return JSON.toJSONString(result);
    }

    /**
     * 使用FreeMarker模板发送邮件
     */
    @RequestMapping("/sendMailFreeMarker")
    public String sendMailFreeMarker() {
        String to = "liyanan2010@sina.cn";
        String subject = "sendMailFreeMarker test";
        Map<String, Object> contentMap = new HashMap<>();
        contentMap.put("user", "Candy");
        String freeMarkerTemplate = "email.ftl";
        String attachmentFile = "f:/timg.jpg";
        Boolean ret = mailService.sendMailFreeMarker(to, subject, contentMap, freeMarkerTemplate, attachmentFile);
        Map<String, Object> result = new HashMap<>();
        result.put("ret", ret);
        return JSON.toJSONString(result);
    }

}
