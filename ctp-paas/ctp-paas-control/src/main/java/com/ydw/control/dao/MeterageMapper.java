package com.ydw.control.dao;

import com.ydw.control.model.db.Meterage;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

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

}
