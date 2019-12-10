package com.mq.mail;

import com.mq.mail.service.impl.SendMailService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author wenlinzou
 */
@SpringBootApplication
public class MailApplication implements ApplicationRunner {

    @Resource
    SendMailService sendMailService;

    public static void main(String[] args) {
        SpringApplication.run(MailApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) {
        sendMailService.send();
    }
}
