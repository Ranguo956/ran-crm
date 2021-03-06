package com.ran.service.impl;

import com.ran.domain.Employee;
import com.ran.domain.Role;
import com.ran.mapper.EmployeeMapper;
import com.ran.page.PageResult;
import com.ran.query.EmployeeQueryObject;
import com.ran.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        // 先根据eid删除中间表emp_role的信息
        employeeMapper.deleteRelation(id);
        // 删除employee信息
        int effectedCount = employeeMapper.deleteByPrimaryKey(id);
        return effectedCount;
    }

    @Override
    public int insert(Employee record) {
        // 先把员工插入employee表
        int effectedCount = employeeMapper.insert(record);
        // 把员工拥有的角色插入emp_role，以此关联员工和角色
        for (Role role : record.getRoles()) {
            employeeMapper.insertRelation(record.getId(), role.getId());
        }
        return effectedCount;
    }

    @Override
    public Employee selectByPrimaryKey(Long id) {
        return employeeMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Employee> selectAll() {
        return employeeMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Employee record) {
        // 更新employee表
        int effectedCount = employeeMapper.updateByPrimaryKey(record);
        // 根据eid删除emp_role中该员工原有角色（旧）
        employeeMapper.deleteRelation(record.getId());
        // 把员工拥有的角色（新）插入emp_role，以此关联员工和角色
        for (Role role : record.getRoles()) {
            employeeMapper.insertRelation(record.getId(), role.getId());
        }
        return effectedCount;
    }

    @Override
    public Employee getEmployeeForLogin(String username, String password) {
        return employeeMapper.getEmployeeForLogin(username, password);
    }

    @Override
    public PageResult queryForPage(EmployeeQueryObject queryObject) {
        // 查询总记录数
        Long count = employeeMapper.queryForPageCount(queryObject);
        if (count == 0) {
            return new PageResult(0, Collections.emptyList());
        }

        List<Employee> result = employeeMapper.queryForPage(queryObject);
        return new PageResult(count.intValue(), result);
    }

    @Override
    public int updateState(Long id) {
        return employeeMapper.updateState(id);
    }

    @Override
    public List<Long> queryRoleByEid(Long eid) {
        return employeeMapper.queryRoleByEid(eid);
    }

}
