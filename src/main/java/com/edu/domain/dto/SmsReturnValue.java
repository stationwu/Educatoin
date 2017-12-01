package com.edu.domain.dto;

import lombok.Data;

@Data
public class SmsReturnValue {
    String returnstatus;
    String message;
    String remainpoint;
    String taskID;
    String successCounts;
}
