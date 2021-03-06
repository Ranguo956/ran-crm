package com.ran.service.impl;


import com.ran.domain.Menu;
import com.ran.mapper.MenuMapper;
import com.ran.service.IMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> queryForMenu() {
        return menuMapper.queryForMenu();
    }
}
