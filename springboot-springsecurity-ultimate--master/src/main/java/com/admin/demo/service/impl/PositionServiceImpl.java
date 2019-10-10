package com.admin.demo.service.impl;

import com.admin.common.exception.BadRequestException;
import com.admin.demo.entity.Department;
import com.admin.demo.entity.Position;
import com.admin.demo.entity.QueryVo;
import com.admin.demo.mapper.DepartmentMapper;
import com.admin.demo.mapper.PositionMapper;
import com.admin.demo.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@CacheConfig(cacheNames = "permission")
public class PositionServiceImpl implements PositionService {

    @Autowired
    PositionMapper positionMapper;
    @Autowired
    DepartmentMapper departmentMapper;

    @Override
    @Cacheable(key = "#root.targetClass.simpleName+':'+#root.methodName+':'+#root.args[0] ",unless="#result == null")
    public Object queryAll(QueryVo queryVo) {
        Map<String,Object> map = new HashMap<>();
        if (queryVo.getDeptIds().size()==0){
            List<Position> posList = positionMapper.query(queryVo);
            List<Position> positions = new ArrayList<>();
            for (Position position:posList){
                if (position.getDept().getPid()!=0){
                    Department department=departmentMapper.findById(position.getDept().getPid());
                    position.setSuperDeptName(department.getName());
                }
                positions.add(position);
            }
            Integer total =positionMapper.count(queryVo);
            map.put("posList",positions);
            map.put("total",total);
            return map;
        }

        List<Position> posList = positionMapper.queryAll(queryVo);
        Integer total =positionMapper.countAll(queryVo);
        map.put("posList",posList);
        map.put("total",total);
        return map ;
    }
    @Cacheable(key = "#root.targetClass.simpleName+':'+#root.methodName+':'+#root.args[0] ",unless="#result == null")
    @Override
    public List<Position> getAll() {
        return positionMapper.getAll();
    }


    @Cacheable(key = "#p0",unless="#result == null")
    @Override
    public List<Position> findById(Long id) {
        return positionMapper.findById(id);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "pos",allEntries=true),
            }
    )
    public void addPos(Position position) {
        Integer i = positionMapper.check(position);
        if (i>0){
            throw new BadRequestException("该职位已存在");
        }
         positionMapper.addPos(position);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "pos",allEntries=true),
            }
    )
    public void updatePos(Position position) {
        Integer i = positionMapper.check(position);
        if (i>0){
            throw new BadRequestException("该职位已存在");
        }
        positionMapper.updatePos(position);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "pos",allEntries=true),
            }
    )
    public void delPos(Long id) {
        Integer i = positionMapper.checkRelated(id);
        if (i>0){
            throw new BadRequestException("该职位存在用户关联");
        }
        positionMapper.delPos(id);
    }
}
