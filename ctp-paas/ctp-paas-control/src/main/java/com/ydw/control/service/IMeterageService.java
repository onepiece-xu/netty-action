package com.ydw.control.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydw.control.model.db.Meterage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.control.model.vo.ResultInfo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xulh
 * @since 2021-04-14
 */
public interface IMeterageService extends IService<Meterage> {

    Meterage getUnCompleteMeterageByMac(String macAddr);

    Meterage getUnCompleteMeterageById(String deviceId);

    void endMeterage(Meterage meterage);

    Meterage beginMeterage(String deviceId, String appId, String userId);

    ResultInfo getMeterageList(String search, Integer status, String beginDate, String endDate, Page buildPage);

    ResultInfo getMeterageCount(String search, Integer status, String beginDate, String endDate);
}
