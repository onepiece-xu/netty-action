package com.ydw.control.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydw.control.model.db.Meterage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ydw.control.model.vo.MeterageListVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xulh
 * @since 2021-04-14
 */
public interface MeterageMapper extends BaseMapper<Meterage> {

    Meterage getUnCompleteMeterageByMac(@Param("macAddr") String macAddr);

    Meterage getUnCompleteMeterageById(@Param("deviceId") String deviceId);

    IPage<MeterageListVO> getMeterageList(@Param("search") String search,
                                          @Param("beginDateTime") LocalDateTime beginDateTime,
                                          @Param("endDateTime") LocalDateTime endDateTime,
                                          Page buildPage);
}
