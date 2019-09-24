package com.admin.demo.mapper;

import com.admin.demo.entity.Position;
import com.admin.demo.entity.QueryVo;

import java.util.List;

public interface PositionMapper {
    List<Position> query(QueryVo queryVo);

    Integer count(QueryVo queryVo);

    List<Position> queryAll(QueryVo queryVo);

    Integer countAll(QueryVo queryVo);

    List<Position> getAll();

    List<Position> findById(Long id);

    Integer check(Position position);

    void addPos(Position position);

    void updatePos(Position position);

    Integer checkRelated(Long id);

    void delPos(Long id);
}
