package com.ran.util;

import com.ran.domain.Employee;
import com.ran.domain.Menu;
import com.ran.domain.Permission;
import com.ran.service.IPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PermissionUtil {

    private static IPermissionService permissionService;

    @Autowired
    public void setPermissionService(IPermissionService permissionService) {
        PermissionUtil.permissionService = permissionService;
    }

    public static boolean checkPermission(String function) {

        System.out.println(function);

        // 如果是超级管理员，直接放行
        Employee currentUser = (Employee) UserContext.get().getSession().getAttribute(UserContext.USER_IN_SESSION);
        if (currentUser.getAdmin()) {
            return true;
        }

        // 得到当前系统所有权限（需要权限验证的url的）
        if (CommonUtil.ALL_PERMISSIONS.size() == 0) {
            // 从数据库查询当前系统所有权限资源，封装到ALL_PERMISSIONS
            List<Permission> permissions = permissionService.selectAll();
            for (Permission permission : permissions) {
                CommonUtil.ALL_PERMISSIONS.add(permission.getResource());
            }
        }

        // 如果当前访问的url不在系统所有权限中，说明不需要验证，否则进行验证
        if (CommonUtil.ALL_PERMISSIONS.contains(function)) {
            // 当前访问的方法需要权限验证，所以要查看当前用户是否拥有该权限
            List<String> userPermissions = (List<String>) UserContext.get().getSession().getAttribute(UserContext.PERMISSION_IN_SESSION);
            if (userPermissions.contains(function)) {
                return true;
            } else {
                // ALL权限匹配
                String allPermission = function.split(":")[0] + ":ALL";
                if (userPermissions.contains(allPermission)) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            // 当前访问的方法不需要权限验证，直接放行
            return true;
        }
    }



    public static void checkMenuPermission(List<Menu> menuList) {

        // 用户拥有的权限
        List<String> userPermissions = (List<String>) UserContext.get().getSession().getAttribute(UserContext.PERMISSION_IN_SESSION);

        // 遍历系统菜单，与当前用户拥有的权限进行比对
        for (int i = 0; i < menuList.size(); i++) {

            String menuPermission = menuList.get(i).getFunction();
            // 菜单需要访问权限
            if (StringUtils.isNotBlank(menuPermission)) {
                // 如果用户没有该菜单权限，删除它，这样前台就不会显示
                if (!userPermissions.contains(menuPermission)) {
                    menuList.remove(i);
                    // 注意，如果是从前往后遍历，一定要i--，否则会出错
                    i--;
                }
                // else 就说明用户有权限，那么就不做处理，菜单保留着
            }
            // else 说明该菜单根本不要求访问权限，谁来都可以访问，那么当前用户也可以，还是不做处理，菜单保留着

            // 递归处理子菜单
            List<Menu> childrenMenuList = menuList.get(i).getChildren();
            if (!childrenMenuList.isEmpty()) {
                checkMenuPermission(childrenMenuList);
            }
        }

    }
}
