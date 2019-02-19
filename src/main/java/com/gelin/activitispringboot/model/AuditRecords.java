package com.gelin.activitispringboot.model;

import lombok.Builder;
import lombok.Data;
import java.io.Serializable;

/**
 * @Auther 葛林
 * @Date 2019/2/19/019 16
 * @Remarks 请假流程审核记录表
 */
@Data
@Builder
public class AuditRecords implements Serializable {
    private static final long serialVersionUID = 1664165722211201006L;
    private Integer id;
    private Integer auditProcessId; //请假id
    private String remarks; //备注意见
    private Integer examineUserId;
    private Integer examineStatus;//审核状态  1 同意批准  2 驳回
    private String examineTime; //审核时间

    //展示
    private String examineName;//审核人
}
