package com.gelin.activitispringboot.dao;

import com.gelin.activitispringboot.model.AuditProcess;
import com.gelin.activitispringboot.model.AuditRecords;
import com.gelin.activitispringboot.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Auther 葛林
 * @Date 2019/2/18 22:34
 * @describe
 */
@Repository
public interface BaseDao {
    int insertAuditProcess(AuditProcess auditProcess);
    int updateAuditProcess(AuditProcess auditProcess);
    AuditProcess findOneAuditProcessById(Integer id);
    AuditProcess findOneAuditProcessByProcessInstanceId(String id);
    List<AuditProcess> findAll();
    List<AuditProcess> findAllByCreateUserId(Integer userId);
    User findOneByLogin(User user);
    User findOneById(Integer id);
    List<User> findAllUserByRoleName(String name);

    /*审核记录*/
    int insertAuditRecords(AuditRecords auditRecords);
}
