package com.mq.mail.service.impl;

import com.mq.mail.constant.Constant;
import com.mq.mail.dto.AttachExcelDto;
import com.mq.mail.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author wenlinzou
 */
@Slf4j
@Service
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username:xx@xx.com}")
    private String from;


    @Resource
    private JavaMailSender mailSender;


    @Override
    public boolean sendSimpleMail(String to, String cc, String subject, String content) {
        log.info("==发送普通邮件开始==");
        SimpleMailMessage message = new SimpleMailMessage();
        String[] tos = to.split(Constant.COMMA);
        String[] ccs = cc.split(Constant.COMMA);
        message.setTo(tos);
        message.setCc(ccs);
        message.setSubject(subject);
        message.setText(content);
        message.setFrom(from);
        try {
            mailSender.send(message);
            log.info("发送人={}，抄送人={}，发送普通邮件成功", to, cc);
            return true;
        } catch (MailSendException e) {
            log.warn("发送人={}，抄送人={}，发送普通邮件失败：{}", to, cc, e);
            return false;
        }
    }

    @Override
    public boolean sendAttachmentMail(String to, String cc, String subject, String mailContent, String[] tableHeader, List<AttachExcelDto> tableRowData) {
        log.info("==发送附件邮件开始==");
        MimeMessage message = mailSender.createMimeMessage();
        String[] tos = to.split(Constant.COMMA);
        String[] ccs = cc.split(Constant.COMMA);
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(tos);
            helper.setCc(ccs);
            helper.setSubject(subject);

            helper.setText(mailContent, true);

            InputStream in = getWriteExcel("Sheet1", tableHeader, tableRowData);

            String fileName = "mailSendAttach.xlsx";
            helper.addAttachment(fileName, new ByteArrayResource(IOUtils.toByteArray(in)), "application/vnd.ms-excel;charset=UTF-8");
            mailSender.send(message);
            log.info("发送人={}，抄送人={}，发送附件到邮件成功", to, cc);
            return true;
        } catch (MessagingException e) {
            log.warn("发送人={}，抄送人={}，发送附件到邮件失败：{}", to, cc, e);
        } catch (IOException e) {
            log.warn("发送人={}，抄送人={}，发送附件到邮件失败io：{}", to, cc, e);
        } catch (MailSendException e) {
            log.warn("发送人={}，抄送人={}，发送附件到邮件失败：{}", to, cc, e);
        }
        return false;
    }

    private InputStream getWriteExcel(String tableTitle, String[] tableHeader, List<AttachExcelDto> rowData) {
        // 创建工作簿
        XSSFWorkbook wb;
        // 创建表对象
        XSSFSheet sheet;
        // 创建行
        XSSFRow row;
        // 输入输出流
        ByteArrayOutputStream out;
        InputStream in;

        try {
            // 不存在，则新建excel
            // 1.获取excel的book对象
            wb = new XSSFWorkbook();
            // 2.创建sheet
            sheet = wb.createSheet(tableTitle);
            // 居中样式
            XSSFCellStyle style = wb.createCellStyle();
            // 字体样式
            XSSFFont font = wb.createFont();
            font.setBold(true);
            font.setItalic(true);
            font.setFontHeight((short) 200);
            font.setColor(HSSFFont.COLOR_NORMAL);
            style.setFont(font);
            // 4.创建第一行，作为表头
            int rowIndex = 0;
            if (null != tableHeader) {
                XSSFRow row1 = sheet.createRow(0);
                for (int i = 0; i < tableHeader.length; ++i) {
                    XSSFCell row1cell = row1.createCell(i);
                    row1cell.setCellValue(tableHeader[i]);
                    row1cell.setCellStyle(style);
                }
                rowIndex++;
            }
            // 5创建数据行，插入数据
            if (!CollectionUtils.isEmpty(rowData)) {
                for (AttachExcelDto dto : rowData) {
                    row = sheet.createRow(rowIndex++);
                    XSSFCell row1cell = row.createCell(0);
                    row1cell.setCellValue(dto.getName());
                    row1cell.setCellStyle(style);
                    XSSFCell row1cel2 = row.createCell(1);
                    row1cel2.setCellValue(dto.getContent());
                    row1cel2.setCellStyle(style);
                }
            }

            // 写文件
            out = new ByteArrayOutputStream();
            wb.write(out);
            byte[] bookByteAry = out.toByteArray();
            in = new ByteArrayInputStream(bookByteAry);
            return in;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
