package com.admin.demo.mapper;

import com.admin.demo.entity.Visits;

public interface ScheduledMapper {
    void resetAllLock();

    void save(Visits visits);
}
