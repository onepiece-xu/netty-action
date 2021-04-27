package com.ydw.control.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydw.control.model.db.Meterage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ydw.control.model.vo.MeterageListVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    IPage<MeterageListVO> getMeterageList(@Param("clusterIds") List<String> clusterIds,
                                          @Param("search") String search,
                                          @Param("status") Integer status,
                                          @Param("beginDateTime") LocalDateTime beginDateTime,
                                          @Param("endDateTime") LocalDateTime endDateTime,
                                          Page buildPage);

    Map<String, Integer> getMeterageCount(@Param("clusterIds") List<String> clusterIds,
                                          @Param("search") String search,
                                          @Param("status") Integer status,
                                          @Param("beginDateTime") LocalDateTime beginDateTime,
                                          @Param("endDateTime") LocalDateTime endDateTime);

}
