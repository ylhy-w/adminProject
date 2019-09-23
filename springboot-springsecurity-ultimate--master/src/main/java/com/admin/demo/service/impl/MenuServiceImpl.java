package com.admin.demo.service.impl;

import com.admin.demo.entity.Menu;
import com.admin.demo.mapper.MenuMapper;
import com.admin.demo.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class MenuServiceImpl  implements MenuService {
    @Autowired
    MenuMapper menuMapper;

    @Override
    public List<Menu> buildTree(Long userId)
    {
        Integer[] ids=menuMapper.getMenus(userId);
        List<Menu> menus=menuMapper.buildTree(ids);
        List<Menu> trees = new ArrayList<Menu>();
        //一级菜单
        for (Menu m:menus){
            if ("0".equals(m.getPid().toString())){
                trees.add(m);
            }
            //子菜单
            for (Menu it : menus) {
                if (it.getPid().equals(m.getId())) {
                    if (m.getChildren() == null) {
                        m.setChildren(new ArrayList<Menu>());
                    }
                    m.getChildren().add(it);
                }
            }
        }
return trees;
    }



    @Override
    public Object menusTree(List<Menu> menus) {

        List<Map<String,Object>> list = new LinkedList<>();
        menus.forEach(menu -> {
                    if (menu!=null){
                        List<Menu> menuList = menuMapper.findByPid(menu.getId());
                        Map<String,Object> map = new LinkedHashMap<>();
                        map.put("id",menu.getId());
                        map.put("label",menu.getName());
                        map.put("pid",menu.getPid());
                        if(menuList!=null && menuList.size()!=0){
                            map.put("children",menusTree(menuList));
                        }
                        list.add(map);
                    }
                }
        );
        return list;
    }

    @Override
    public List<Menu> findByPid(long l) {
        return menuMapper.findByPid(l);
    }


}
