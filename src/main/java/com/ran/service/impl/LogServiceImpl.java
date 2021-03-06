package com.ran.service.impl;

import com.ran.domain.Log;
import com.ran.mapper.LogMapper;
import com.ran.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogServiceImpl implements ILogService {

    @Autowired
    private LogMapper LogMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return LogMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Log record) {
        return LogMapper.insert(record);
    }

    @Override
    public Log selectByPrimaryKey(Long id) {
        return LogMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Log> selectAll() {
        return LogMapper.selectAll();
    }

    @Override
    public int updateByPrimaryKey(Log record) {
        return LogMapper.updateByPrimaryKey(record);
    }

}
