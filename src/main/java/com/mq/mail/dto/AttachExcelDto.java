package com.mq.mail.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wenlinzou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachExcelDto {
    private String name;
    private String content;
}
