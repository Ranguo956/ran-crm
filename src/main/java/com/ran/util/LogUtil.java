package com.ran.util;

import com.ran.domain.Employee;
import com.ran.domain.Log;

import com.ran.service.ILogService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

public class LogUtil {

    @Autowired
    private ILogService logService;


    public void writeLog(JoinPoint joinPoint) {

        // 防止AOP拦截logService本身，造成死循环
        if (joinPoint.getTarget() instanceof ILogService) {
            return;
        }

        Log log = new Log();
        log.setOptime(new Date());

        HttpServletRequest request = UserContext.get();
        Employee currentUser = (Employee) request.getSession().getAttribute(UserContext.USER_IN_SESSION);
        log.setOpuser(currentUser);

        log.setOpip(request.getRemoteAddr());

        // 完整function应该是 类名+方法名
        String className = joinPoint.getTarget().toString();
        String functionName = joinPoint.getSignature().toString();
        log.setFunction(className + ":" + functionName);

        // 讲请求参数转为JSON串
        ObjectMapper objectMapper = new ObjectMapper();
        String params = null;
        try {
            params = objectMapper.writeValueAsString(joinPoint.getArgs());
            log.setParams(params);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 插入日志记录到数据库
        logService.insert(log);
    }
}
