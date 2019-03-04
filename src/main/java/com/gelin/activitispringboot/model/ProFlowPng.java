package com.gelin.activitispringboot.model;


import lombok.Data;

import java.io.Serializable;

@Data
public class ProFlowPng implements Serializable {
    private Integer id;
    private String processInstanceId;
    private String fileUrl;
}
