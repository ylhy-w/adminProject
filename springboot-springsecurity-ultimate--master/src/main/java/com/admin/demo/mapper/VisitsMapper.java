package com.admin.demo.mapper;

import com.admin.demo.entity.Visits;

import java.util.List;

public interface VisitsMapper {
    Visits findByDate(String date);

    void save(Visits visits);

    List<Visits> findAllVisits(String date1, String date2);

    void update(Visits visits);
}
