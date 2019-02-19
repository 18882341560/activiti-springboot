package com.gelin.activitispringboot.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther 葛林
 * @Date 2019/2/18 22:26
 * @describe 角色表
 */
@Data
@Builder
public class Role implements Serializable {

    private static final long serialVersionUID = -7940738874723129067L;

    private Integer id;
    private String name;

}
