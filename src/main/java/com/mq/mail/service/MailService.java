package com.mq.mail.service;

import com.mq.mail.dto.AttachExcelDto;

import java.util.List;

/**
 * @author wenlinzou
 */
public interface MailService {
    /**
     * 不带附件发送
     *
     * @param to      发送人(多个逗号隔开,)
     * @param cc      抄送人(多个逗号隔开,)
     * @param subject 邮件标题
     * @param content 邮件正文
     */
    boolean sendSimpleMail(String to, String cc, String subject, String content);

    /**
     * 邮件发送excel附件
     *
     * @param to           发送人(多个逗号隔开,)
     * @param cc           抄送人(多个逗号隔开,)
     * @param subject      邮件标题
     * @param mailContent  邮件正文
     * @param tableHeader  附件表头
     * @param tableRowData 附件内容
     */
    boolean sendAttachmentMail(String to, String cc, String subject, String mailContent, String[] tableHeader, List<AttachExcelDto> tableRowData);
}
