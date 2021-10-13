package com.example.demo.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendMailAllRequest {
    private String sendMailSubject;
    private String templateFileName;
}
