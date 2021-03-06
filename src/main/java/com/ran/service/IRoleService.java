package com.ran.service;

import com.ran.domain.Role;
import com.ran.page.PageResult;
import com.ran.query.QueryObject;

import java.util.List;


public interface IRoleService {

    int deleteByPrimaryKey(Long id);

    int insert(Role record);

    Role selectByPrimaryKey(Long id);

    List<Role> selectAll();

    int updateByPrimaryKey(Role record);

    PageResult queryForPage(QueryObject queryObject);
}
