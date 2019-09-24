package com.admin.demo.service;

import com.admin.demo.entity.Position;
import com.admin.demo.entity.QueryVo;

import java.util.List;

public interface PositionService {
    Object queryAll(QueryVo queryVo);

    List<Position> getAll();

    List<Position> findById(Long id);

    void addPos(Position position);

    void updatePos(Position position);

    void delPos(Long id);
}
