package com.gelin.activitispringboot.actviti;

import lombok.Data;

import java.io.Serializable;

/**
 * create gl  2018/11/15
 **/
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 8670323755158348767L;
    private Integer id;
    private String name;
}
