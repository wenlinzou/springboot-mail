package com.mq.mail.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mq.mail.constant.Constant;
import com.mq.mail.dto.AttachExcelDto;
import com.mq.mail.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wenlinzou
 */
@Slf4j
@Service
public class SendMailService {
    @Resource
    MailService mailService;

    @Value("${json.mail.send}")
    private String mailSettingStr;


    public boolean send() {
        log.info("+++++++send mail+++++++");
        boolean sendResult = false;
        JSONObject settingJson = JSONObject.parseObject(mailSettingStr);
        boolean sendSwitch = settingJson.getBoolean(Constant.MAIL_SWITCH);
        if (sendSwitch) {
            String to = settingJson.getString(Constant.MAIL_TO);
            String cc = settingJson.getString(Constant.MAIL_CC);
            String subject = settingJson.getString(Constant.MAIL_SUBJECT);
            String contentSimple = settingJson.getString(Constant.MAIL_CONTENT_SIMPLE);

            int type = settingJson.getIntValue(Constant.MAIL_TYPE);
            if (0 == type) {
                sendResult = mailService.sendSimpleMail(to, cc, subject, contentSimple);
            } else {
                String contentAtt = settingJson.getString(Constant.MAIL_CONTENT_ATT);
                String tableHeader = settingJson.getString(Constant.MAIL_TABLE_HEADER);
                String[] tableHeaders = tableHeader.split(Constant.COMMA);
                sendResult = mailService.sendAttachmentMail(to, cc, subject, contentAtt, tableHeaders, getExcelList());
            }
        }
        return sendResult;

    }

    private List<AttachExcelDto> getExcelList() {
        AttachExcelDto dto = new AttachExcelDto("name1", "content1");
        List<AttachExcelDto> list = new ArrayList<>(1);
        list.add(dto);
        return list;
    }
}
