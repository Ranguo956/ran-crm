package com.ran.web.controller;

import com.ran.domain.Employee;
import com.ran.domain.Menu;
import com.ran.page.PageResult;
import com.ran.query.EmployeeQueryObject;
import com.ran.service.IEmployeeService;
import com.ran.service.IMenuService;
import com.ran.service.IPermissionService;
import com.ran.util.AjaxResult;
import com.ran.util.PermissionUtil;
import com.ran.util.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private IMenuService menuService;

    @RequestMapping("/employee")
    public String index() {
        return "employee";
    }

    @RequestMapping("login")
    @ResponseBody
    public AjaxResult login(String username, String password, HttpServletRequest request) {

        //---------AOP日志相关：把request放入当前线程-----------
        UserContext.set(request);

        //---------登录相关-------
        AjaxResult result = null;
        Employee currentUser = employeeService.getEmployeeForLogin(username, password);
        if (currentUser != null) {
            result = new AjaxResult("登录成功", true);
            // 把用户信息存入session
            request.getSession().setAttribute(UserContext.USER_IN_SESSION, currentUser);
            // 把用户拥有的权限放入session中
            List<String> userPermissions = permissionService.queryResourceByEid(currentUser.getId());
            request.getSession().setAttribute(UserContext.PERMISSION_IN_SESSION, userPermissions);
            //把用户能访问的菜单存入session
            List<Menu> menuList = menuService.queryForMenu();
            PermissionUtil.checkMenuPermission(menuList);// 该方法会从全部菜单中剔除用户无权访问的菜单
            request.getSession().setAttribute(UserContext.MENU_IN_SESSION,menuList);
        } else {
            result = new AjaxResult("账号或密码错误", false);
        }

        return result;
    }

    @RequestMapping("/employee_list")
    @ResponseBody
    public PageResult list(EmployeeQueryObject queryObject) {
        PageResult pageResult = null;
        pageResult = employeeService.queryForPage(queryObject);

        return pageResult;
    }

    @RequestMapping("/employee_save")
    @ResponseBody
    public AjaxResult save(Employee employee) {

        AjaxResult result = null;
        try {
            // 前端只输入了部分字段，剩余字段后端默认设置（密码，是否管理员，状态）
            employee.setPassword("123");
            employee.setAdmin(false);
            employee.setState(true);
            employeeService.insert(employee);
            result = new AjaxResult("保存成功", true);
        } catch (Exception e) {
            result = new AjaxResult("保存异常，请联系管理员", false);
        }
        return result;
    }

    @RequestMapping("/employee_update")
    @ResponseBody
    public AjaxResult update(Employee employee) {

        AjaxResult result = null;
        try {
            employeeService.updateByPrimaryKey(employee);
            result = new AjaxResult("更新成功", true);
        } catch (Exception e) {
            result = new AjaxResult("更新异常，请联系管理员", false);
        }
        return result;
    }

    @RequestMapping("/employee_delete")
    @ResponseBody
    public AjaxResult delete(Long id) {

        AjaxResult result = null;
        try {
            employeeService.updateState(id);
            result = new AjaxResult("删除成功", true);
        } catch (Exception e) {
            result = new AjaxResult("删除异常，请联系管理员", false);
        }
        return result;
    }

    @RequestMapping("/role_queryByEid")
    @ResponseBody
    public List queryByEid(Long eid) {
        return employeeService.queryRoleByEid(eid);
    }

}
