package com.admin.demo.service.impl;

import com.admin.demo.entity.Visits;
import com.admin.demo.mapper.LogMapper;
import com.admin.demo.mapper.VisitsMapper;
import com.admin.demo.service.VisitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VisitsServiceImpl implements VisitsService {

    @Autowired
    VisitsMapper visitsMapper;

    @Autowired
    LogMapper logMapper;




    //每次登录记录
    @Override
    public void count(HttpServletRequest request) {
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsMapper.findByDate(localDate.toString());
        visits.setPvCounts(visits.getPvCounts()+1);
        long ipCounts = logMapper.findIp(localDate.toString(), localDate.plusDays(1).toString());
        visits.setIpCounts(ipCounts);
        visitsMapper.update(visits);
    }

    @Override
    public Object get() {
        Map map = new HashMap();
        LocalDate localDate = LocalDate.now();
        Visits visits = visitsMapper.findByDate(localDate.toString());
        List<Visits> list = visitsMapper.findAllVisits(localDate.minusDays(6).toString(),localDate.plusDays(1).toString());

        long recentVisits = 0, recentIp = 0;
        for (Visits data : list) {
            recentVisits += data.getPvCounts();
            recentIp += data.getIpCounts();
        }
        map.put("newVisits",visits.getPvCounts());
        map.put("newIp",visits.getIpCounts());
        map.put("recentVisits",recentVisits);
        map.put("recentIp",recentIp);
        return map;
    }

    @Override
    public Object getChartData() {
        Map map = new HashMap();
        LocalDate localDate = LocalDate.now();
        //当前天+1 -6得到一周范围
        List<Visits> list = visitsMapper.findAllVisits(localDate.minusDays(6).toString(),localDate.plusDays(1).toString());
     /*   List<String> weekDays =new ArrayList<String>();
        for(int i=0;i<list.size();i++){
　　weekDays.add(list.get(i).getWeekDay());
        }*/
        //jdk1.8,lambda表达式
        map.put("weekDays",list.stream().map(Visits::getWeekDay).collect(Collectors.toList()));
        map.put("visitsData",list.stream().map(Visits::getPvCounts).collect(Collectors.toList()));
        map.put("ipData",list.stream().map(Visits::getIpCounts).collect(Collectors.toList()));
        return map;
    }
}
