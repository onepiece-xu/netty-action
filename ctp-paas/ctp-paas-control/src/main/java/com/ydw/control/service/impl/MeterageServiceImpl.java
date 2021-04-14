package com.ydw.control.service.impl;

import com.ydw.control.model.db.Meterage;
import com.ydw.control.dao.MeterageMapper;
import com.ydw.control.service.IMeterageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
