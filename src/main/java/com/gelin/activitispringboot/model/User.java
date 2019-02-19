package com.gelin.activitispringboot.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther 葛林
 * @Date 2019/2/18 22:22
 * @describe 用户表
 */
@Data
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = -1983372737732618686L;

    private Integer id;
    private String username; //账号
    private String password;  //密码
    private String name;  //名字
}
