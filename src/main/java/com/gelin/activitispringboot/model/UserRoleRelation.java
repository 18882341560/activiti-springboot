package com.gelin.activitispringboot.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther 葛林
 * @Date 2019/2/18 22:29
 * @describe 用户角色关联表
 */
@Data
@Builder
public class UserRoleRelation implements Serializable {

    private static final long serialVersionUID = -5441168862199153469L;

    private Integer id;
    private Integer userId;
    private Integer roleId;
}
