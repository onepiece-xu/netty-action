package com.ydw.control.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydw.control.model.db.Meterage;
import com.ydw.control.dao.MeterageMapper;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.IMeterageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.control.utils.SequenceGenerator;
import com.ydw.control.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xulh
 * @since 2021-04-14
 */
@Service
public class MeterageServiceImpl extends ServiceImpl<MeterageMapper, Meterage> implements IMeterageService {

    @Autowired
    private MeterageMapper meterageMapper;

    @Override
    public Meterage getUnCompleteMeterageByMac(String macAddr) {
        return meterageMapper.getUnCompleteMeterageByMac(macAddr);
    }

    @Override
    public Meterage getUnCompleteMeterageById(String deviceId) {
        return meterageMapper.getUnCompleteMeterageById(deviceId);
    }

    @Override
    public void endMeterage(Meterage meterage) {
        meterage.setStatus(1);
        meterage.setEndTime(LocalDateTime.now());
        meterage.setTotalTime((int) Duration.between(meterage.getBeginTime(), meterage.getEndTime()).getSeconds());
        updateById(meterage);
    }

    @Override
    public Meterage beginMeterage(String deviceId, String appId, String userId) {
        Meterage meterage = new Meterage();
        meterage.setStatus(0);
        meterage.setAppId(appId);
        meterage.setBeginTime(LocalDateTime.now());
        meterage.setDeviceId(deviceId);
        meterage.setUserId(userId);
        meterage.setId(SequenceGenerator.sequence());
        save(meterage);
        return meterage;
    }

    @Override
    public ResultInfo getMeterageList(String search, Integer status, String beginDate, String endDate, Page buildPage) {
        LocalDateTime beginDateTime = null;
        LocalDateTime endDateTime = null;
        if (StringUtil.isNotBlank(beginDate) && StringUtil.isNotBlank(endDate)){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            beginDateTime = LocalDate.parse(beginDate, dateTimeFormatter).atTime(LocalTime.MIN);
            endDateTime = LocalDate.parse(endDate, dateTimeFormatter).atTime(LocalTime.MAX);
        }
        IPage page = meterageMapper.getMeterageList(search, status, beginDateTime, endDateTime, buildPage);
        return ResultInfo.success(page);
    }

    @Override
    public ResultInfo getMeterageCount(String search, Integer status, String beginDate, String endDate) {
        LocalDateTime beginDateTime = null;
        LocalDateTime endDateTime = null;
        if (StringUtil.isNotBlank(beginDate) && StringUtil.isNotBlank(endDate)){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            beginDateTime = LocalDate.parse(beginDate, dateTimeFormatter).atTime(LocalTime.MIN);
            endDateTime = LocalDate.parse(endDate, dateTimeFormatter).atTime(LocalTime.MAX);
        }
        Map<String, Integer> map = meterageMapper.getMeterageCount(search, status, beginDateTime, endDateTime);
        return ResultInfo.success(map);
    }
}
