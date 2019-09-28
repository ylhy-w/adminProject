package com.admin.demo.service.impl;

import com.admin.common.utils.StringUtils;
import com.admin.demo.entity.Visits;
import com.admin.demo.mapper.ScheduledMapper;
import com.admin.demo.mapper.VisitsMapper;
import com.admin.demo.service.ScheduledService;
import com.admin.log.LogAspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ScheduledServiceImpl implements ScheduledService {
    private final static Logger logger = LoggerFactory.getLogger(LogAspect.class);
@Autowired
    ScheduledMapper scheduledMapper;

    @Autowired
    VisitsMapper visitsMapper;
    @Override
    @Scheduled(cron = "0 0 0 * * ?") //每天0点重置
    public void resetAllLock() {
        scheduledMapper.resetAllLock();
        logger.info("凌晨重置所有锁定账号错误密码次数");
    }

    //定时任务记录每天情况
    @Override
    @Scheduled(cron = "0 0 0 * * ?") //每天0点更新
    public void save() {
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsMapper.findByDate(localDate.toString());
        if(visits == null){
            visits = new Visits();
            visits.setWeekDay(StringUtils.getWeekDay());
            visits.setPvCounts(1L);
            visits.setIpCounts(1L);
            visits.setDate(localDate.toString());
            scheduledMapper.save(visits);
        }
    }

}
