package com.gelin.activitispringboot.dao;

import com.gelin.activitispringboot.model.AuditProcess;
import org.springframework.stereotype.Repository;

/**
 * @Auther 葛林
 * @Date 2019/2/18 22:34
 * @describe
 */
@Repository
public interface BaseDao {
    int insertAuditProcess(AuditProcess auditProcess);
}
