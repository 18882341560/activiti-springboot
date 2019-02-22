package com.gelin.activitispringboot.dao;

import com.gelin.activitispringboot.model.*;
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
    List<AuditRecords> findAllRecordByAuditId(Integer id);



    /*=====================首检计划流程========================*/

    int insertFirstPlan(FirstPlan firstPlan);
    FirstPlan findOneFirstPlanById(Integer id);
    FirstPlan findFirstPlanByProcessInstanceId(String pid);
    int updateFirstPlan(FirstPlan firstPlan);
    int insertFirstRecords(FirstRecords firstRecords);
    List<FirstRecords> findFirstRecordList(Integer firstPlanId);
    List<FirstPlan> findFirstPlanByCreateUserId(Integer id);
}
