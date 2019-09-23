package com.admin.demo.mapper;

import com.admin.demo.entity.Menu;
import java.util.List;

public interface MenuMapper {
    Integer[] getMenus(Long userId);
    List<Menu> buildTree(Integer[] ids);


    Integer[] getAllMenus( );
    List<Menu> buildMenusTree(Integer[] ids);

    List<Menu> findByPid(Long id);
}
