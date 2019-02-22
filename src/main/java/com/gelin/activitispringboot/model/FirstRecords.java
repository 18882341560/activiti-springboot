package com.gelin.activitispringboot.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Auther 葛林
 * @Date 2019/2/22/022 09
 * @Remarks 首检操作流程表
 */
@Data
@Builder
public class FirstRecords implements Serializable {
    private static final long serialVersionUID = 2695397846898128298L;
    private Integer id;
    private Integer firstPlanId; //首检计划的id
    private String taskId;//任务的id
    private String remarks; //备注意见
    private Integer examStatus;//审核状态  1 审核通过  2 驳回
    private Integer operationUserId;//操作人
    private String operationDate; //操作时间
}
