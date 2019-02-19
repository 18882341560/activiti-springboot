package com.gelin.activitispringboot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther 葛林
 * @Date 2019/2/17 18:05
 * @describe 请假类 最好是类名与流程文件的id相同，以后好启动
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditProcess implements Serializable {

    private static final long serialVersionUID = -3421993782504853118L;


    private Integer id;
    private String name; //请假人姓名
    private Integer leaveDay;  //请假天数
    private String cause;  //请假事由
    private String remarks;  //请假备注
    private String leaveTime; //请假时间
    private Integer status; //请假状态  1 初始录入  2 审批中  3 审核通过 4审核不通过
    private String createTime; //创建时间
    private Integer createUserId; //创建人
    private String processInstanceId; //流程实例Id

}
