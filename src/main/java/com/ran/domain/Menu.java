package com.ran.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter@Setter@JsonIgnoreProperties(value = {"handler"})
public class Menu {
    private Long id;

    // 菜单名称
    private String text;

    private String iconCls;

    private Boolean checked;

    private String state;

    // 当菜单展示在页面后，点击该菜单发出的请求url
    private String attributes;


    private List<Menu> children;

    // 访问当前菜单需要的权限。
    private String function;
}