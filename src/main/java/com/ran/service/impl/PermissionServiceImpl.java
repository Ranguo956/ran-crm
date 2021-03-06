package com.ran.service.impl;

import com.ran.domain.Permission;
import com.ran.mapper.PermissionMapper;
import com.ran.page.PageResult;
import com.ran.query.QueryObject;
import com.ran.service.IPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return 0;
    }

    @Override
    public int insert(Permission record) {
        return permissionMapper.insert(record);
    }

    @Override
    public Permission selectByPrimaryKey(Long id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Permission> selectAll() {
        return permissionMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Permission record) {
        return permissionMapper.updateByPrimaryKey(record);
    }

    @Override
    public PageResult queryForPage(QueryObject queryObject) {
        // 查询总记录数
        Long count = permissionMapper.queryForPageCount(queryObject);
        if (count == 0) {
            return new PageResult(0, Collections.emptyList());
        }

        List<Permission> result = permissionMapper.queryForPage(queryObject);
        return new PageResult(count.intValue(), result);
    }

    @Override
    public List<String> queryResourceByEid(Long id) {
        // 有些人可能会想着在这里分两步走：先根据eid查出用户角色，再根据用户角色查出对应的权限。
        // 既然是后台，又允许关联查询，就直接join吧
        return permissionMapper.queryResourceByEid(id);
    }

}
