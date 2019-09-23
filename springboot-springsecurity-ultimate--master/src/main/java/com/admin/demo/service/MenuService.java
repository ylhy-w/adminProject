package com.admin.demo.service;

import com.admin.demo.entity.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> buildTree(Long userId);

    Object menusTree(List<Menu> menus);

    List<Menu> findByPid(long l);
}
