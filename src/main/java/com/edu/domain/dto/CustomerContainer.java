package com.edu.domain.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CustomerContainer {
    @NotNull
    private String openCode;

    @NotNull
    private String name;

    @NotNull
    private String mobilePhone;

    @NotNull
    private String address;
    
    private Long verifyCodeId;

    private String verifyCode;

    private List<ChildContainer> children = new ArrayList<>();
}
