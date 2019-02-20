package com.gelin.activitispringboot.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther 葛林
 * @Date 2019/2/20/020 17
 * @Remarks 首检计划表
 */
@Data
@Builder
@NoArgsConstructor
public class FirstPlan implements Serializable {
    private Integer id;
    private String processInstanceId; // 流程实例id
    private String siteName;   // 站点名称
    private String chanceName;   // 作业区名称
    private String detailedDescription;   // 详细说明
    private String arrange; // 初步安排时间
    private Integer createUserId;//创建人
    private String createDateTime; //创建时间
}
