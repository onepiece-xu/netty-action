package com.ydw.monitor.service.impl;

import com.ydw.monitor.model.vo.ResultInfo;
import com.ydw.monitor.model.vo.StreamInfo;
import com.ydw.monitor.service.IStreamService;
import org.springframework.stereotype.Service;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:11
 */
@Service
public class ArmStreamServiceImpl implements IStreamService {
    /**
     * 开流服务
     *
     * @param param
     * @return
     */
    @Override
    public ResultInfo startStream(StreamInfo param) {
        return null;
    }

    /**
     * 关闭流服务
     *
     * @param param
     * @return
     */
    @Override
    public ResultInfo stopStream(StreamInfo param) {
        return null;
    }
}
